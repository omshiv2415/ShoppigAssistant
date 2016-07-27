package assistance.shopping.msc.assistant.support;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by admin on 27/07/2016.
 */

public class Application extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
