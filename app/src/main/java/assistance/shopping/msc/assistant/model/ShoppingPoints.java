package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 30/08/2016.
 */
@IgnoreExtraProperties

public class ShoppingPoints {

        public String uid;
        public String shoppingAssistantName;
        public String createdAt;
    public String transactionCompletedAt;
        public Double totalShoppingPoints;
        public Double earnedShoppingPoints;
    public String sAPhotoUrl;
    public String sAEmail;

    public ShoppingPoints() {

        // Default Constructor
    }


    public ShoppingPoints(String uid, String shoppingAssistantName, String createdAt, String transactionCompletedAt, Double totalShoppingPoints,
                          Double earnedShoppingPoints, String sAPhotoUrl, String sAEmail) {
            this.uid = uid;
            this.shoppingAssistantName = shoppingAssistantName;
            this.createdAt = createdAt;
        this.transactionCompletedAt = transactionCompletedAt;
            this.totalShoppingPoints = totalShoppingPoints;
            this.earnedShoppingPoints = earnedShoppingPoints;
        this.sAPhotoUrl = sAPhotoUrl;
        this.sAEmail = sAEmail;

        }

        // [START post_to_map]
        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", uid);
            result.put("shoppingAssistantName", shoppingAssistantName);
            result.put("createdAt", createdAt);
            result.put("transactionCompletedAt", transactionCompletedAt);
            result.put("totalShoppingPoints", totalShoppingPoints);
            result.put("earnedShoppingPoints", earnedShoppingPoints);
            result.put("sAPhotoUrl", sAPhotoUrl);
            result.put("sAEmail", sAEmail);

            return result;
        }

    }
// [END post_class]
