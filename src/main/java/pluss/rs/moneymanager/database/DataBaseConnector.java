package pluss.rs.moneymanager.database;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseConnector {

    public static final String DB_CONFIG_PROPS = "db-config.properties";
    public static final String JDBC_URL = "url";
    public static final String JDBC_USERNAME = "username";
    public static final String JDBC_PASSWORD = "password";

    private static DataBaseConnector instance;
    String url;
    String username;
    String password;

    Connection mainConnection;

    private DataBaseConnector() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Properties prop = new Properties();
            classLoader.getResourceAsStream(DB_CONFIG_PROPS);
            prop.load(classLoader.getResourceAsStream(DB_CONFIG_PROPS));
            url = prop.getProperty(JDBC_URL);
            username = prop.getProperty(JDBC_USERNAME);
            password = prop.getProperty(JDBC_PASSWORD);
            mainConnection = DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static DataBaseConnector getInstance() {
        if (instance == null) {
            instance = new DataBaseConnector();
        }
        return instance;
    }

    public static Connection getDedicatedConnection() {
        return getInstance().createConnection();
    }


    public static Connection getMainConnection() {
        return getInstance().mainConnection;
    }


    public static Statement createStatement() {
        try {
            return getMainConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Can't create SQL statement");
        }
    }

    public static PreparedStatement getPrepared(String preparedQuery) {
        try {
            return getMainConnection().prepareStatement(preparedQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Can't create SQL statement " + e);
        }
    }
}
