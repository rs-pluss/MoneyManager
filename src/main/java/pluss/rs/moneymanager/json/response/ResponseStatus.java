package pluss.rs.moneymanager.json.response;

/**
 * Response status types
 */
public enum ResponseStatus {
    SUCCESS("Success"),
    ERROR("Error");

    final private String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
