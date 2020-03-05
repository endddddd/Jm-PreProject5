package dao;

import exceptions.DBException;
import model.User;
import util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserJdbcDAO implements UserDAO {
    private static volatile UserJdbcDAO instance;

    public static UserJdbcDAO getInstance() {
        if (instance == null) {
            synchronized (UserJdbcDAO.class) {
                if (instance == null) {
                    instance = new UserJdbcDAO();
                }
            }
        }
        return instance;
    }

    private Connection connection;

    private UserJdbcDAO() {
        this.connection = DBHelper.getInstance().getConnection();
    }

    @Override
    public void createTable() throws DBException {
        try (PreparedStatement ps = connection.prepareStatement("create table if not exists users (id bigint auto_increment, name varchar(256), password varchar(256), role varchar(256), primary key (id))")) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public void dropTable() throws DBException {
        try (PreparedStatement ps = connection.prepareStatement("DROP TABLE IF EXISTS users")) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new LinkedList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
             ResultSet res = ps.executeQuery()) {
            while (res.next()) {
                User user = new User();
                user.setId(res.getLong("id"));
                user.setName(res.getString("name"));
                user.setPassword(res.getString("password"));

                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return list;
    }

    // Проверка имени на уникальность
    @Override
    public boolean isNotReg(String name) {
        return getAllUsers()
                .stream()
                .anyMatch((e) -> e.getName().hashCode() == name.hashCode());
    }

    @Override
    public void addUser(User user) {

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO users (name , password,role) Values (?, ?,?)")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean removeUser(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            return preparedStatement.execute();
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    @Override
    public void updateUser(String name, String password, Long id) {

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET name = ? , password = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setLong(3, id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception es) {
            es.getMessage();
        }
    }

    @Override
    public List<User> getUserById(long id) {
        List<User> list = new LinkedList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from users where id = ?")) {
            ps.setLong(1, id);
            ResultSet res = ps.executeQuery();
            res.next();
            User user = new User();
            user.setId(id);
            user.setName(res.getString("name"));
            user.setPassword(res.getString("password"));
            list.add(user);

        } catch (Exception ex) {
            ex.getMessage();
        }
        return list;
    }

    @Override
    public User getUserByName (String name ) {
        User user = new User();
        try (PreparedStatement ps = connection.prepareStatement("select * from users where name = ?")) {
            ps.setString(1, name);
            ResultSet res = ps.executeQuery();
            res.next();
            user.setId(res.getLong("id"));
            user.setName(name);
            user.setPassword(res.getString("password"));
            user.setRole(res.getString("role"));


        } catch (SQLException e) {
            e.printStackTrace();

        }
        return user;
    }
    public boolean validateUser (String name, String password) {
        String pass = "";
        try (PreparedStatement ps = connection.prepareStatement("select * from users where name = ?")) {
            ps.setString(1, name);
            ResultSet res = ps.executeQuery();
            res.next();
            pass = res.getString("password");
            res.close();

        } catch (Exception ex) {
            ex.getMessage();
        }
        return pass.equals(password);
    }
}
