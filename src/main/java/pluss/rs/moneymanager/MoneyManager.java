package pluss.rs.moneymanager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pluss.rs.moneymanager.database.DataBaseConnector;
import pluss.rs.moneymanager.database.view.AccountView;
import pluss.rs.moneymanager.model.Account;
import pluss.rs.moneymanager.response.ResponseStatus;
import pluss.rs.moneymanager.response.StandardResponse;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.get;
import static spark.Spark.post;

public class MoneyManager {
    class Messages {
        public static final String ACCOUNT_NOT_FOUND = "Unable to find account";
        public static final String SENDER_DONT_HAVE_ENOUGH_MONEY = "Sender account don't have enough money.";
        public static final String TRANSFER_SUCCESS = "Transfer success";
    }

    public static void main(String[] args) {
        get("/create", MoneyManager::initializeDatabase);
        post("/transfer", MoneyManager::transfer);
    }

    /**
     * Prepare data and transfer money between accounts
     *
     * @param request
     *         request spark data
     * @param response
     *         response spark data
     * @return json response
     */
    private static String transfer(Request request, Response response) {

        JsonElement jsonElement = new JsonParser().parse(request.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String senderId = jsonObject.get("sender").getAsString();
        String receiverID = jsonObject.get("receiver").getAsString();
        BigDecimal amount = new BigDecimal(jsonObject.get("amount").getAsString());

        response.type("application/json");

        StandardResponse standardResponse = doTransferMoney(senderId, receiverID, amount);
        return new Gson().toJson(standardResponse);

    }

    /**
     * Load accounts by ID's and try to transfer money
     *
     * @param senderId
     *         id of sender account
     * @param receiverId
     *         id of reciever account
     * @param amount
     *         value on transfered money
     * @return response
     */
    private static StandardResponse doTransferMoney(String senderId, String receiverId, BigDecimal amount) {
        Account sender = AccountView.loadAccount(senderId);
        Account receiver = AccountView.loadAccount(receiverId);

        StandardResponse response;
        if (sender != null && receiver != null) {
            if (sender.getBalance().compareTo(amount) >= 0) {
                sender.setBalance(sender.getBalance().subtract(amount));
                receiver.setBalance(receiver.getBalance().add(amount));
                DataBaseConnector.save();
            } else {
                response = new StandardResponse(ResponseStatus.ERROR, Messages.SENDER_DONT_HAVE_ENOUGH_MONEY);
                return response;
            }
        } else {
            response = new StandardResponse(ResponseStatus.ERROR, Messages.ACCOUNT_NOT_FOUND);
            return response;
        }
        response = new StandardResponse(ResponseStatus.SUCCESS, Messages.TRANSFER_SUCCESS);
        return response;
    }


    /**
     * Create table and initialize data on database. Implements just for tests and debug
     *
     * @param request
     * @param response
     * @return status
     */
    private static String initializeDatabase(Request request, Response response) {
        Statement createStatement = DataBaseConnector.createStatement();
        try {
            createStatement.executeUpdate("CREATE TABLE ACCOUNT(ID INT PRIMARY KEY, NAME VARCHAR(255), BALANCE DECIMAL);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(1, 'Rin', 12500);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(2, 'Sun', 9500);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(3, 'Propan', 6200);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(4, 'Pooran', 900);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(5, 'Richi', 99900);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(6, 'Sauan', 3700);");
            DataBaseConnector.save();
        } catch (SQLException e) {
            System.err.print(e);
            return "failed ";
        }

        return "Success.";
    }
}