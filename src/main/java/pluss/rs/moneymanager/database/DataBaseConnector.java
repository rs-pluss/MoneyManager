package pluss.rs.moneymanager.database;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * H2Database connector
 * Connect to the database and create statements
 */
public class DataBaseConnector {

    public static final String DB_CONFIG_PROPS = "db-config.properties";
    public static final String JDBC_URL = "url";
    public static final String JDBC_USERNAME = "username";
    public static final String JDBC_PASSWORD = "password";

    private static DataBaseConnector instance;

    private Connection mainConnection;

    private DataBaseConnector() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Properties prop = new Properties();
            classLoader.getResourceAsStream(DB_CONFIG_PROPS);
            prop.load(classLoader.getResourceAsStream(DB_CONFIG_PROPS));
            String url = prop.getProperty(JDBC_URL);
            String username = prop.getProperty(JDBC_USERNAME);
            String password = prop.getProperty(JDBC_PASSWORD);
            mainConnection = DriverManager.getConnection(url, username, password);
            mainConnection.setAutoCommit(false);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataBaseConnector getInstance() {
        if (instance == null) {
            instance = new DataBaseConnector();
        }
        return instance;
    }

    /**
     * @return connection to database
     */
    private static Connection getConnection() {
        return getInstance().mainConnection;
    }

    /**
     * Commit connection to database
     */
    public static void save() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException e1) {
                throw new RuntimeException("Can't rollback connection" + e1);
            }
            throw new RuntimeException("Can't commit connection" + e);
        }
    }

    /**
     * Create statement in main connection
     *
     * @return statement in main connection
     */
    public static Statement createStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Can't create SQL statement");
        }
    }

    /**
     * Create prepared statement in main connection from prepared query
     *
     * @param preparedQuery
     *         query template for prepared statement
     * @return PreparedStatement in main connection
     */
    public static PreparedStatement createPreparedStatement(String preparedQuery) {
        try {
            return getConnection().prepareStatement(preparedQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Can't create SQL statement " + e);
        }
    }
}
