package pluss.rs.moneymanager;

import pluss.rs.moneymanager.database.AccountView;
import pluss.rs.moneymanager.database.DataBaseConnector;
import pluss.rs.moneymanager.database.TransactionCommiter;
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


    static String createDatabase(Request request, Response response) {
        Statement createStatement = DataBaseConnector.createStatement();
        try {
            createStatement.executeUpdate("CREATE TABLE ACCOUNT(ID INT PRIMARY KEY, NAME VARCHAR(255), BALANCE DECIMAL);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(1, 'Rin', 12500);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(2, 'Sun', 9500);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(3, 'Propan', 6200);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(4, 'Pooran', 900);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(5, 'Richi', 99900);");
            createStatement.executeUpdate("INSERT INTO ACCOUNT VALUES(6, 'Sauan', 3700);");
        } catch (SQLException e) {
            System.err.print(e);
            return "failed ";
        }

        return "success";
    }

    static String transfer(Request request, Response response) {
        StringBuilder sb = new StringBuilder();
        String senderId = request.queryParams("sender");
        String receiverID = request.queryParams("receiver");
        BigDecimal amount = new BigDecimal(request.queryParams("amount"));

        Account sender = AccountView.getAccount(senderId);
        Account receiver = AccountView.getAccount(receiverID);
        if (sender.getBalance().compareTo(amount) >= 0) {
            TransactionCommiter.moneyTransferTransaction(sender, receiver, amount);
        } else {

        }

        return sb.toString();
    }


}