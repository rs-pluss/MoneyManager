package pluss.rs.moneymanager.response;

/**
 * Standard response for client.
 * Must be converted to Json format before sends
 */
public class StandardResponse {
    String text;
    ResponseStatus responseStatus;

    public StandardResponse(ResponseStatus responseStatus, String text) {
        this.responseStatus = responseStatus;
        this.text = text;
    }
}
