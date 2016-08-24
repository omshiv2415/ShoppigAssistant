package assistance.shopping.msc.assistant.support;

import android.app.ProgressDialog;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.model.User;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    public Support support = new Support();
    protected Button mButtonRegisterRegister;
    private ProgressDialog mProgressDialog;
    private EditText mEmailFieldRegister;
    private EditText mPasswordFieldRegister;
    private FirebaseAuth mAuth;
    private TextToSpeech speech;
    private DatabaseReference mDatabase;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }

    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email, String authentication, String FirstName, String LastName, String Gender, String DateOfBirth, String PhotoUri, String uid) {

        User user = new User(name, email, authentication, FirstName, LastName, Gender, DateOfBirth, PhotoUri, uid);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
