package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String UserName;
    public String email;
    public String authentication;
    public String FirstName;
    public String LastName;
    public String Gender;
    public String DateOfBirth;
    public String PhotoUri;
    public String uid;
    public Double TotalshoppingPoints;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String AuthenticationToken, String FirstName, String LastName, String Gender,
                String DateOfBirth, String PhotoUri, String uid, Double TotalshoppingPoints) {
        this.UserName = username;
        this.email = email;
        this.authentication = AuthenticationToken;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Gender = Gender;
        this.DateOfBirth = DateOfBirth;
        this.PhotoUri = PhotoUri;
        this.uid = uid;
        this.TotalshoppingPoints = TotalshoppingPoints;
    }

}
