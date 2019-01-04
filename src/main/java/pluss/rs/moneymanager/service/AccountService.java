package pluss.rs.moneymanager.service;

import com.google.gson.Gson;
import pluss.rs.moneymanager.database.DataBaseConnector;
import pluss.rs.moneymanager.database.view.AccountView;
import pluss.rs.moneymanager.json.request.MoneyTransferRequest;
import pluss.rs.moneymanager.model.Account;
import pluss.rs.moneymanager.json.response.ResponseStatus;
import pluss.rs.moneymanager.json.response.StandardResponse;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountService {
    class Messages {
        public static final String ACCOUNT_NOT_FOUND = "Unable to find account.";
        public static final String SENDER_HAVE_NOT_ENOUGH_MONEY = "Sender account don't have enough money.";
        public static final String TRANSFER_SUCCESS = "Transfer success.";
    }


    /**
     * Prepare data and execute request to transfer money
     *
     * @param request
     * @return
     */
    public String executeMoneyTransferRequest(MoneyTransferRequest request) {
        Account sender = AccountView.loadAccount(request.getSenderId());
        Account receiver = AccountView.loadAccount(request.getReceiverId());
        StandardResponse response = transferMoneyBetweenAccounts(sender, receiver, request.getAmount());

        return new Gson().toJson(response);
    }


    /**
     * try to transfer money between accounts
     *
     * @param sender
     *         sender account
     * @param receiver
     *         receiver account
     * @param amount
     *         value on transfer money
     * @return response
     */
    private StandardResponse transferMoneyBetweenAccounts(Account sender, Account receiver, BigDecimal amount) {
        StandardResponse response;
        if (sender != null && receiver != null) {
            if (sender.getBalance().compareTo(amount) >= 0) {
                sender.setBalance(sender.getBalance().subtract(amount));
                receiver.setBalance(receiver.getBalance().add(amount));
                DataBaseConnector.save();
                response = new StandardResponse(ResponseStatus.SUCCESS, Messages.TRANSFER_SUCCESS);

            } else {
                response = new StandardResponse(ResponseStatus.ERROR, Messages.SENDER_HAVE_NOT_ENOUGH_MONEY);
            }
        } else {
            response = new StandardResponse(ResponseStatus.ERROR, Messages.ACCOUNT_NOT_FOUND);
        }
        return response;
    }


    /**
     * Init simple database for test
     *
     * @return result
     */
    public String initTestDataBase() {
        Statement createStatement = DataBaseConnector.createStatement();
        StandardResponse response;
        try {
            createStatement.executeUpdate("CREATE TABLE ACCOUNT(ID INT PRIMARY KEY, NAME VARCHAR(255), BALANCE DECIMAL);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(1, 'Rin', 12500);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(2, 'Sun', 9500);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(3, 'Propan', 6200);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(4, 'Pooran', 900);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(5, 'Richi', 99900);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(6, 'Sauan', 3700);");
            DataBaseConnector.save();
            response = new StandardResponse(ResponseStatus.SUCCESS, "Test database was created.");
        } catch (SQLException e) {
            System.err.print(e);
            response = new StandardResponse(ResponseStatus.ERROR, "Unable to create test database.");
        }

        return new Gson().toJson(response);
    }
}
