package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public String ShoppingAssistantPhoto;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text, String ShoppingAssistantPhoto) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.ShoppingAssistantPhoto = ShoppingAssistantPhoto;
    }

}
// [END comment_class]
