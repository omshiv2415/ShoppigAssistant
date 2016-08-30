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
        public String paymentCompletedAt;
        public Double totalShoppingPoints;
        public Double earnedShoppingPoints;

        public Map<String, Boolean> stars = new HashMap<>();

        public ShoppingPoints(String uid, String shoppingAssistantName, String createdAt, String paymentCompletedAt, Double totalShoppingPoints,
                              Double earnedShoppingPoints) {
            this.uid = uid;
            this.shoppingAssistantName = shoppingAssistantName;
            this.createdAt = createdAt;
            this.paymentCompletedAt = paymentCompletedAt;
            this.totalShoppingPoints = totalShoppingPoints;
            this.earnedShoppingPoints = earnedShoppingPoints;

        }

        // [START post_to_map]
        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", uid);
            result.put("shoppingAssistantName", shoppingAssistantName);
            result.put("createdAt", createdAt);
            result.put("transactionCompletedAt",paymentCompletedAt);
            result.put("totalShoppingPoints", totalShoppingPoints);
            result.put("earnedShoppingPoints", earnedShoppingPoints);


            return result;
        }

    }
// [END post_class]
