package pluss.rs.moneymanager.response;

/**
 * Standard response for client.
 * Must be converted to Json format before sends
 */
public class StandardResponse {
    /**
     * Text of responce
     */
    private String text;
    private ResponseStatus responseStatus;

    public StandardResponse(ResponseStatus responseStatus, String text) {
        this.responseStatus = responseStatus;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

}
