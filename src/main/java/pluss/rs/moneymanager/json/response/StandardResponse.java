package pluss.rs.moneymanager.json.response;

/**
 * Standard response for client.
 * Must be converted to Json format before sends
 */
public class StandardResponse {
    /**
     * Text of response
     */
    private String text;
    /**
     * status of response {@link ResponseStatus}
     */
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

    /**
     * @return status of response {@link ResponseStatus}
     */
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    /**
     * set status of response {@link ResponseStatus}
     *
     * @param responseStatus
     *         status
     */
    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

}
