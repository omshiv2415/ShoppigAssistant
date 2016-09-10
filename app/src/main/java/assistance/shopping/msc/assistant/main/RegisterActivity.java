package assistance.shopping.msc.assistant.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.model.User;
import assistance.shopping.msc.assistant.support.Support;

public class RegisterActivity extends Activity {

    private static final String TAG = "EmailPassword";
    public Support support = new Support();
    protected Button mButtonRegisterRegister;
    private EditText mEmailFieldRegister;
    private EditText mPasswordFieldRegister;
    private FirebaseAuth mAuth;
    private TextToSpeech speech;
    private DatabaseReference mDatabase;
    public Button mButtonGoogleLoginFromRegister;
    public Button mButtonLoginFromRegister;
    public Button mButtonResetFromRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailFieldRegister = (EditText) findViewById(R.id.editTextEmail);
        mPasswordFieldRegister = (EditText) findViewById(R.id.editTextPassword);
        mButtonRegisterRegister = (Button) findViewById(R.id.buttonRegister);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mButtonGoogleLoginFromRegister = (Button) findViewById(R.id.login_with_Google_from_register_screen);
        mButtonLoginFromRegister = (Button) findViewById(R.id.login_from_register_screen);
        mButtonResetFromRegister = (Button)findViewById(R.id.reset_from_register_screen);



        mButtonRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailFieldRegister.getText().toString().trim();
                String password = mPasswordFieldRegister.getText().toString().trim();


                if (mEmailFieldRegister.equals("") || mPasswordFieldRegister.equals("")) {

                    speech = new TextToSpeech(RegisterActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                speech.setLanguage(Locale.UK);
                                String toSpeak = ("Please provide all the Details");
                                speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(RegisterActivity.this, toSpeak,
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                } else if (!support.isValidEmail(email)) {
                    speech = new TextToSpeech(RegisterActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                speech.setLanguage(Locale.UK);
                                String toSpeak = ("Please provide correct Email");
                                speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(RegisterActivity.this, toSpeak,
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                } else if (!support.isValidPassword(password)) {

                    speech = new TextToSpeech(RegisterActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                speech.setLanguage(Locale.UK);
                                String toSpeak = ("Password must be between 7 and 21");
                                speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(RegisterActivity.this, toSpeak,
                                        Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


                } else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    if (task.isSuccessful()) {

                                        onAuthSuccess(task.getResult().getUser());

                                    } else if (!task.isSuccessful()) {

                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }


                                }

                            });


                }


            }

        });


        mButtonGoogleLoginFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeUserToGoogleSignIn = new Intent(RegisterActivity.this, GoogleSignInActivity.class);
                startActivity(takeUserToGoogleSignIn);

            }
        });

        mButtonLoginFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeUserToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(takeUserToLogin);
            }
        });


        mButtonResetFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeUserToRegister = new Intent(RegisterActivity.this, PasswordResetActivity.class);
                startActivity(takeUserToRegister);
            }
        });
    }


    public void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        String userFirstname = "Update";
        String userLastname = "Update";
        String userGender = "";
        String userphoto = "https://lh3.googleusercontent.com/-et8-_Jd3MiY/AAAAAAAAAAI/AAAAAAAAAAs/9OWsA3w5ZGw/s96-c/photo.jpg";
        String userDateofBirth = "";
        String uid = mAuth.getCurrentUser().getUid();
        Double TotalshoppingPoints = 50.00;
        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail(), FirebaseInstanceId.getInstance().getToken(), userFirstname, userLastname, userGender,
                userDateofBirth, userphoto, uid, TotalshoppingPoints);

        // Go to MainActivity
        Toast.makeText(RegisterActivity.this, "Welcome to the Shopping Assistant", Toast.LENGTH_LONG).show();
        Intent takeUserHome = new Intent(RegisterActivity.this, NavigationActivity.class);
        startActivity(takeUserHome);
        finish();


    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }

    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email, String authentication, String FirstName, String LastName, String Gender, String DateOfBirth,
                              String UserPhoto, String uid, Double TotalshoppingPoints) {

        User user = new User(name, email, authentication, FirstName, LastName, Gender, DateOfBirth, UserPhoto, uid, TotalshoppingPoints);

        mDatabase.child("users").child(userId).setValue(user);
    }
    @Override
    public void onStop() {
        super.onStop();
        this.finish();
    }

}

