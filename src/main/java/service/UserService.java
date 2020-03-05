package service;

import dao.UserDaoFactory;
import dao.UserDAO;
import dao.UserHibernateDAO;
import dao.UserJdbcDAO;
import exceptions.DBException;
import model.User;

import java.util.List;

public class UserService {
    private static volatile UserService instance;

    private UserDAO dao = UserDaoFactory.getUserDao();

    private UserService() {

    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    public void createTable() throws DBException {

        dao.createTable();
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public boolean isNotReg(String name) {
        return dao.isNotReg(name);
    }

    public boolean addUser(User user) {

        if (isNotReg(user.getName())) {
            return false;
        } else {
            dao.addUser(user);
            return true;
        }
    }

    public boolean reomveUser(long id) {
        return dao.removeUser(id);
    }

    public void updateUser(String name, String password, Long id) {

        dao.updateUser(name, password, id);
    }

    public List<User> getUserById(long id) {
        return dao.getUserById(id);
    }


    // Проверяем на нулл и совпадение логина и пароля, в случае удачи получаем пользователя
    public User validateUser(String name, String password) {
        if (name.trim().length() == 0 || password.trim().length() == 0 || !dao.validateUser(name, password)) {
            return null;
        } else {
            return dao.getUserByName(name);
        }
    }

}


