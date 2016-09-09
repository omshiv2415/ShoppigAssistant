package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class ShoppingBroadcast {

    public String uid;
    public String shoppingAssistant;
    public String shoppingBroadcastTitle;
    public String shoppingBroadcastDescription;
    public String createdAt;
    public String shoppingAssistantPhotoUrl;
    public String shoppingStatus = "Processing";
    public String paymentType = "Cash";
    public String shoppingAssistantName;
    public String paymentCompletedAt;
    public String saFirstLineAddress;
    public String saCity;
    public String saPostCode;
    public String srFirstLineAddress;
    public String srCity;
    public String srPostCode;
    public Double setShoppingPoint;


    public Map<String, Boolean> status = new HashMap<>();
    private String photoUrl;

    public ShoppingBroadcast() {
        // Default constructor required for calls to DataSnapshot.getValue(ShoppingBroadcast.class)
    }

    public ShoppingBroadcast(String uid, String shoppingAssistant, String shoppingBroadcastTitle, String shoppingBroadcastDescription, String createdAt, String shoppingAssistantPhotoUrl, String paymentType,
                             String paymentCompletedAt, String shoppingAssistantName, String saFirstLineAddress, String saCity, String saPostCode, String srFirstLineAddress,
                             String srCity, String srPostCode, Double setShoppingPoint) {
        this.uid = uid;
        this.shoppingAssistant = shoppingAssistant;
        this.shoppingBroadcastTitle = shoppingBroadcastTitle;
        this.shoppingBroadcastDescription = shoppingBroadcastDescription;
        this.createdAt = createdAt;
        this.shoppingAssistantPhotoUrl = shoppingAssistantPhotoUrl;
        this.paymentType = paymentType;
        this.paymentCompletedAt = paymentCompletedAt;
        this.shoppingAssistantName = shoppingAssistantName;
        this.saFirstLineAddress = saFirstLineAddress;
        this.saCity = saCity;
        this.saPostCode = saPostCode;
        this.srFirstLineAddress = srFirstLineAddress;
        this.srCity = srCity;
        this.srPostCode = srPostCode;
        this.setShoppingPoint = setShoppingPoint;

    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("shoppingAssistant", shoppingAssistant);
        result.put("shoppingBroadcastTitle", shoppingBroadcastTitle);
        result.put("shoppingBroadcastDescription", shoppingBroadcastDescription);
        result.put("createdAt", createdAt);
        result.put("shoppingAssistantPhotoUrl", shoppingAssistantPhotoUrl);
        result.put("paymentType", paymentType);
        result.put("transactionCompletedAt", paymentCompletedAt);
        result.put("shoppingStatus", shoppingStatus);
        result.put("status", status);
        result.put("shoppingAssistantName", shoppingAssistantName);
        result.put("saFirstLineAddress", saFirstLineAddress);
        result.put("saCity", saCity);
        result.put("saPostCode", saPostCode);
        result.put("srFirstLineAddress", srFirstLineAddress);
        result.put("srCity", srCity);
        result.put("srPostcode", srPostCode);
        result.put("setShoppingPoint", setShoppingPoint);


        return result;
    }

    // [END post_to_map]
    public String getPhotoUrl() {

        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
// [END post_class]
