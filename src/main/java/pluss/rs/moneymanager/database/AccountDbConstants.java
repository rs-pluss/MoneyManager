package pluss.rs.moneymanager.database;


/**
 * Database fields and queries for account
 */
public interface AccountDbConstants {

    String ID = "id";
    String NAME = "name";
    String BALANCE = "balance";
    String TABLE = "account";

    /**
     * Query for select single account on database
     */
    String GET_ACCOUNT_QUERY = "SELECT "
            + ID + ", "
            + NAME + ", "
            + BALANCE + " "
            + " FROM " + TABLE
            + " WHERE " + ID
            + " = ?;";

    /**
     * Query for update balance of account
     */
    String CHANGE_BALANCE_QUERY = "UPDATE "
            + TABLE
            + " SET " + BALANCE + " = ? "
            + " WHERE " + ID + " = ? ";
}
