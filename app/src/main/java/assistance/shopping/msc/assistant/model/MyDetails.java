package assistance.shopping.msc.assistant.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class MyDetails {
    public String uid;
    public String uFirstName;
    public String uLastName;
    public String uDateOfBirth;
    public String uGender;
    public String uUserName;
    public String uEmail;
    public String uPhoto;



    public Map<String, Boolean> stars = new HashMap<>();

    public MyDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(MyDetails.class)
    }

    public MyDetails(String uid, String mFirstName, String mLastName, String mDateOfBirth, String mGender,
                     String mEmail, String mUserName, String mPhoto) {
        this.uFirstName = mFirstName;
        this.uLastName = mLastName;
        this.uDateOfBirth = mDateOfBirth;
        this.uGender = mGender;
        this.uid = uid;
        this.uEmail = mEmail;
        this.uUserName = mUserName;
        this.uPhoto = mPhoto;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("FirstName", uFirstName);
        result.put("LastName", uLastName);
        result.put("DateOfBirth", uDateOfBirth);
        result.put("Gender", uGender);
        result.put("Email", uEmail);
        result.put("UserName", uUserName);
        result.put("UserPhoto", uPhoto);


        return result;
    }
    // [END post_to_map]

}
// [END post_class]
