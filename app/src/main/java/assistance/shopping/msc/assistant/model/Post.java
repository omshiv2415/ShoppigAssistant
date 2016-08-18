package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String title;
    public String body;
    public Double LocationLat;
    public Double LocationLon;
    public String createdAt;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String body, String createdAt, Double LocationLat, Double LocationLon) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.LocationLat = LocationLat;
        this.LocationLon = LocationLon;

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
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
