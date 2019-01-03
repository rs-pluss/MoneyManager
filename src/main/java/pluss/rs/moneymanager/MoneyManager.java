package pluss.rs.moneymanager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pluss.rs.moneymanager.database.AccountView;
import pluss.rs.moneymanager.database.DataBaseConnector;
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
    public static void main(String[] args) {
        get("/create", MoneyManager::initializeDatabase);
        post("/transfer", MoneyManager::transfer);
    }




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


    private static String transfer(Request request, Response response) {

        JsonElement jsonElement = new JsonParser().parse(request.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String senderId = jsonObject.get("sender").getAsString();
        String receiverID = jsonObject.get("receiver").getAsString();
        BigDecimal amount = new BigDecimal(jsonObject.get("amount").getAsString());

        response.type("application/json");


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
                return new Gson().toJson(new StandardResponse(ResponseStatus.ERROR, "Sender don't have enough money."));

            }
        } else {
            return new Gson().toJson(new StandardResponse(ResponseStatus.ERROR, "Can't find account by id."));
        }
        return new Gson().toJson(new StandardResponse(ResponseStatus.SUCCESS, "Money transfered"));

    }


}