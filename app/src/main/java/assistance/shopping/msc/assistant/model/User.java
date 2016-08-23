package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String UserName;
    public String email;
    public String authentication;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String AuthenticationToken) {
        this.UserName = username;
        this.email = email;
        this.authentication = AuthenticationToken;
    }

}
// [END blog_user_class]
