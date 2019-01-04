package pluss.rs.moneymanager.json.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;

/**
 * Standard json request to transfer money
 */
public class MoneyTransferRequest {

    private String senderId;
    private String receiverId;
    private BigDecimal amount;

    class Properties {
        public static final String SENDER = "sender";
        public static final String RECEIVER = "receiver";
        public static final String AMOUNT = "amount";
    }

    /**
     * Constructor with String
     *
     * @param requestBody
     *         json String
     */
    public MoneyTransferRequest(String requestBody) {
        this(new JsonParser().parse(requestBody));
    }

    /**
     * Constructor with JsonElement
     *
     * @param jsonElement
     *         JsonElement
     */
    public MoneyTransferRequest(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        senderId = jsonObject.get(Properties.SENDER).getAsString();
        receiverId = jsonObject.get(Properties.RECEIVER).getAsString();
        amount = new BigDecimal(jsonObject.get(Properties.AMOUNT).getAsString());
    }


    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
