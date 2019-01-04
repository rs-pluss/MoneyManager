package pluss.rs.moneymanager.database;

import pluss.rs.moneymanager.model.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

/**
 * H2Database connector
 * Manage connection to the database and create statements
 */
public class DataBaseConnector {

    public static final String DB_CONFIG_PROPS = "db-config.properties";
    public static final String JDBC_URL = "url";
    public static final String JDBC_USERNAME = "username";
    public static final String JDBC_PASSWORD = "password";

    private static DataBaseConnector instance;

    private Connection mainConnection;

    /**
     * Constructor, initialize connection to database
     */
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

    public Connection getConnection() {
        return mainConnection;
    }

    /**
     * Load account from database
     *
     * @param accountId
     *         account identifier
     * @return Account model
     */
    public Account loadAccount(String accountId) {

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(AccountDbConstants.GET_ACCOUNT_QUERY);
            preparedStatement.setLong(1, Long.valueOf(accountId));

            ResultSet rs = preparedStatement.executeQuery();
            Account account;
            if (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                BigDecimal balance = rs.getBigDecimal("balance");
                account = new Account(id, name, balance);
            } else {
                account = null;
            }
            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Can't load model " + e);
        }
    }


    /**
     * Save account model on database
     *
     * @param account
     *         model for save
     */
    public void saveModel(Account account) {
        try {
            PreparedStatement changeBalanceStatement = getConnection().prepareStatement(AccountDbConstants.CHANGE_BALANCE_QUERY);
            changeBalanceStatement.setBigDecimal(1, (account.getBalance()));
            changeBalanceStatement.setLong(2, account.getId());
            changeBalanceStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Commit connection to database
     */
    public void commit() {
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
     * @return singleton of the Connector
     */
    public static DataBaseConnector getInstance() {
        if (instance == null) {
            instance = new DataBaseConnector();
        }
        return instance;
    }
}
