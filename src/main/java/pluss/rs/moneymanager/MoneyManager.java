package pluss.rs.moneymanager;

import static spark.Spark.*;

public class MoneyManager{
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}