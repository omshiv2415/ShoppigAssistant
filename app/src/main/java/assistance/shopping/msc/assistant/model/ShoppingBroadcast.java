package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class ShoppingBroadcast {

    public String uid;
    public String author;
    public String title;
    public String body;
    public Double LocationLat;
    public Double LocationLon;
    public String createdAt;
    public String ShoppingAssistantPhoto;
    public String starCount = "Processing";
    public Map<String, Boolean> stars = new HashMap<>();
    private String photoUrl;

    public ShoppingBroadcast() {
        // Default constructor required for calls to DataSnapshot.getValue(ShoppingBroadcast.class)
    }

    public ShoppingBroadcast(String uid, String author, String title, String body, String createdAt, Double LocationLat, Double LocationLon, String ShoppingAssistantPhoto) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.LocationLat = LocationLat;
        this.LocationLon = LocationLon;
        this.ShoppingAssistantPhoto = ShoppingAssistantPhoto;

    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("Location Lat", LocationLat);
        result.put("Location Lon", LocationLon);
        result.put("createdAt", createdAt);
        result.put("ShoppingAssistantPhoto", ShoppingAssistantPhoto);
        result.put("starCount", starCount);
        result.put("stars", stars);

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
