package pluss.rs.moneymanager;

import pluss.rs.moneymanager.json.request.MoneyTransferRequest;
import pluss.rs.moneymanager.service.AccountService;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Input point of the apps, impersonate Controller role
 */
public class MoneyManager {

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
        AccountService handleService = new AccountService();
        return handleService.executeMoneyTransferRequest(new MoneyTransferRequest(request.body()));
    }


    /**
     * Create table and initialize data on database. Implements just for tests and debug
     *
     * @param request
     * @param response
     * @return status
     */
    private static String initializeDatabase(Request request, Response response) {
        AccountService handleService = new AccountService();
        return handleService.initTestDataBase();
    }
}