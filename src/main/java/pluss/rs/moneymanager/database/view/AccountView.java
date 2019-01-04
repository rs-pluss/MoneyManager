package pluss.rs.moneymanager.database.view;

import pluss.rs.moneymanager.database.DataBaseConnector;
import pluss.rs.moneymanager.model.Account;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for representation account model on database
 */
public class AccountView {

    /**
     * Name of the field on databace
     */
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BALANCE = "balance";
    public static final String TABLE = "account";

    /**
     * Query templates
     */
    class Query {
        /**
         * Query for select single account on database
         */
        public static final String GET_ACCOUNT_QUERY = "SELECT "
                + ID + ", "
                + NAME + ", "
                + BALANCE + " "
                + " FROM " + TABLE
                + " WHERE " + ID
                + " = ?;";

        /**
         * Query for update balance of account
         */
        public static final String CHANGE_BALANCE_QUERY = "UPDATE "
                + AccountView.TABLE
                + " SET " + BALANCE + " = ? "
                + " WHERE " + ID + " = ? ";
    }


    /**
     * Found and loan account from database
     *
     * @param request_id
     *         account id
     * @return account model
     */
    public static Account loadAccount(String request_id) {
        try {
            PreparedStatement preparedStatement = DataBaseConnector.createPreparedStatement(Query.GET_ACCOUNT_QUERY);
            preparedStatement.setLong(1, Long.valueOf(request_id));

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
    public static void saveModel(Account account) {
        try {
            PreparedStatement changeBalanceStatement = DataBaseConnector.createPreparedStatement(Query.CHANGE_BALANCE_QUERY);
            changeBalanceStatement.setBigDecimal(1, (account.getBalance()));
            changeBalanceStatement.setLong(2, account.getId());
            changeBalanceStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
