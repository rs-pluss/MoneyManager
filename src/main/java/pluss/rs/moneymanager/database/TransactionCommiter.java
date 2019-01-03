package pluss.rs.moneymanager.database;

import pluss.rs.moneymanager.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionCommiter {

    public static final String CHANGE_BALANCE_QUERY = "UPDATE "
            + AccountView.TABLE
            + " SET " + AccountView.BALANCE + " = ? "
            + " WHERE " + AccountView.ID + " = ? ";

    public static void moneyTransferTransaction(Account sender, Account reciever, BigDecimal value) {
        try {
            Connection connection = DataBaseConnector.getDedicatedConnection();
            connection.setAutoCommit(false);
            try {
                PreparedStatement subtractBalanceStatement = connection.prepareStatement(CHANGE_BALANCE_QUERY);
                subtractBalanceStatement.setBigDecimal(1, (sender.getBalance().subtract(value)));
                subtractBalanceStatement.setLong(2, sender.getId());
                subtractBalanceStatement.executeUpdate();

                PreparedStatement addBalanceStatement = connection.prepareStatement(CHANGE_BALANCE_QUERY);
                addBalanceStatement.setBigDecimal(1, (reciever.getBalance().add(value)));
                addBalanceStatement.setLong(2, reciever.getId());
                addBalanceStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("can't commit changes: " + e);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
