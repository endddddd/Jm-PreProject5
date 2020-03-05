package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class UserDaoFactory {
    final static String HibernateDao = "Hibernate";
    final static String JdbcDao = "Jdbc";

    public static String getTypeDao() {

        try (FileInputStream fileInputStream = new FileInputStream(
                UserDaoFactory.class.getClassLoader().getResource("TypeDAO.property").getPath())) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties.getProperty("TypeDAO");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static UserDAO getUserDao() {
        String typeDao = getTypeDao();
        if (typeDao.equals(HibernateDao)) {
            return UserHibernateDAO.getInstance();
        } else if (typeDao.equals(JdbcDao)) {
            return UserJdbcDAO.getInstance();
        }

        return null;

    }
}
