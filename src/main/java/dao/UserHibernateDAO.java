package dao;

import exceptions.DBException;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.DBHelper;

import java.util.List;

public class UserHibernateDAO implements UserDAO {

    private static volatile UserHibernateDAO instance;
    private SessionFactory sessionFactory;

    private UserHibernateDAO() {
        this.sessionFactory = DBHelper.getInstance().getSessionFactory();
    }

    public static UserHibernateDAO getInstance() {
        if (instance == null) {
            synchronized (UserHibernateDAO.class) {
                if (instance == null) {
                    instance = new UserHibernateDAO();
                }
            }
        }
        return instance;
    }

    @Override
    public void createTable() throws DBException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("create table if not exists users (id bigint auto_increment, name varchar(256), password varchar(256), role varchar(256), primary key (id))");
        transaction.commit();
        session.close();
    }

    @Override
    public void dropTable() throws DBException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("DROP TABLE IF EXISTS users");
        transaction.commit();
        session.close();

    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<User> users = session.createQuery("FROM User").list();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public boolean isNotReg(String name) {
        return getAllUsers()
                .stream()
                .anyMatch((e) -> e.getName().hashCode() == name.hashCode());
    }

    @Override
    public void addUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();

    }

    @Override
    public boolean removeUser(long id) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            session.close();
            return true;
        } catch (Exception ex) {
            ex.getMessage();
            return false;
        }
    }

    @Override
    public void updateUser(String name, String password, Long id) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("UPDATE User SET name =:pName, password =:pPass WHERE id =:pId");
            query.setParameter("pName", name);
            query.setParameter("pPass", password);
            query.setParameter("pId", id);
            query.executeUpdate();
            session.close();
            transaction.commit();
        } catch (Exception es) {
            es.getMessage();
        }

    }

    @Override
    public List<User> getUserById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from User where id =:pId");
        query.setParameter("pId", id);
        List<User> user = query.list();
        return user;
    }

    @Override
    public User getUserByName(String name) {
        User user = null;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("select id from User where name =:paramN");
            query.setParameter("paramN", name);
            List<Long> id = query.list();
            user = (User) session.get(User.class, id.get(0));
            transaction.commit();
            session.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
        return user;
    }

    @Override
    public boolean validateUser(String name, String password) {
        String pass = "";
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            User user = getUserByName(name);
            pass = user.getPassword();
            transaction.commit();
            session.close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return pass.equals(password);
    }
}
