package pluss.rs.moneymanager;

import pluss.rs.moneymanager.database.AccountView;
import pluss.rs.moneymanager.database.DataBaseConnector;
import pluss.rs.moneymanager.model.Account;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.get;
import static spark.Spark.post;

public class MoneyManager {
    public static void main(String[] args) {
        get("/create", MoneyManager::createDatabase);
        post("/transfer", MoneyManager::transfer);
    }


    private static String createDatabase(Request request, Response response) {

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


    private static Response transfer(Request request, Response response) {
        String result = "";
        String senderId = request.queryParams("sender");
        String receiverID = request.queryParams("receiver");
        BigDecimal amount = new BigDecimal(request.queryParams("amount"));

        Account sender = AccountView.loadAccount(senderId);
        Account receiver = AccountView.loadAccount(receiverID);

        if (sender != null && receiver != null) {
            if (sender.getBalance().compareTo(amount) >= 0) {
                sender.setBalance(sender.getBalance().subtract(amount));
                receiver.setBalance(receiver.getBalance().add(amount));
                DataBaseConnector.save();
                response.body("Success.");
                response.status(200);
            } else {
                result = "Sender don't have enough money.";
                response.status(409);
            }
        } else {
            result = "Can't find account by id.";
            response.status(404);

        }
        response.body(result);
        return response;
    }


}