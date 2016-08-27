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
    public String title;
    public String body;
    public String createdAt;
    public String ShoppingAssistantPhoto;
    public String starCount = "Processing";
    public String paymentType = "Cash";
    public String shoppingAssistantName;
    public String paymentCompletedAt;
    public String saFirstLineAddress;
    public String saCity;
    public String saPostCode;
    public String srFirstLineAddress;
    public String srCity;
    public String srPostCode;


    public Map<String, Boolean> stars = new HashMap<>();
    private String photoUrl;

    public ShoppingBroadcast() {
        // Default constructor required for calls to DataSnapshot.getValue(ShoppingBroadcast.class)
    }

    public ShoppingBroadcast(String uid, String shoppingAssistant, String title, String body, String createdAt, String ShoppingAssistantPhoto, String paymentType,
                             String paymentCompletedAt, String shoppingAssistantName, String saFirstLineAddress, String saCity, String saPostCode, String srFirstLineAddress,
                             String srCity, String srPostCode) {
        this.uid = uid;
        this.shoppingAssistant = shoppingAssistant;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.ShoppingAssistantPhoto = ShoppingAssistantPhoto;
        this.paymentType = paymentType;
        this.paymentCompletedAt = paymentCompletedAt;
        this.shoppingAssistantName = shoppingAssistantName;
        this.saFirstLineAddress = saFirstLineAddress;
        this.saCity = saCity;
        this.saPostCode = saPostCode;
        this.srFirstLineAddress = srFirstLineAddress;
        this.srCity = srCity;
        this.srPostCode = srPostCode;


    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("shoppingAssistant", shoppingAssistant);
        result.put("title", title);
        result.put("body", body);
        result.put("createdAt", createdAt);
        result.put("ShoppingAssistantPhoto", ShoppingAssistantPhoto);
        result.put("paymentType", paymentType);
        result.put("transactionCompletedAt",paymentCompletedAt);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("shoppingAssistantName", shoppingAssistantName);
        result.put("saFirstLineAddress", saFirstLineAddress);
        result.put("saCity", saCity);
        result.put("saPostCode",saPostCode);
        result.put("srFirstLineAddress", srFirstLineAddress);
        result.put("srCity",srCity);
        result.put("srPostcode",srPostCode);


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
