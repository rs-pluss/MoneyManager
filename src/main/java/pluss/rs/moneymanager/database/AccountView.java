package pluss.rs.moneymanager.database;

import pluss.rs.moneymanager.model.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountView {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BALANCE = "balance";
    public static final String TABLE = "account";
    public static final String QUERY = "SELECT "
            + ID + ", "
            + NAME + ", "
            + BALANCE + " "
            + " FROM " + TABLE
            + " WHERE " + ID
            + " = ?;";


    public static Account getAccount(String id) {
        try {
            PreparedStatement preparedStatement = DataBaseConnector.getPrepared(QUERY);
            preparedStatement.setLong(1, Long.valueOf(id));

            ResultSet rs = preparedStatement.executeQuery();
            Account account = new Account();
            while (rs.next()) {
                account.setId(rs.getLong("id"));
                account.setName(rs.getString("name"));
                account.setBalance(rs.getBigDecimal("balance"));
            }
            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Can't load model " + e);
        }
    }
}
