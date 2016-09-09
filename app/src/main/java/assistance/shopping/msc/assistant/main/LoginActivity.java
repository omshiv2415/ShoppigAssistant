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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import assistance.shopping.msc.assistant.R;

public class LoginActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    protected Button mButtonLogin;
    protected Button mInitialRegister;
    protected Button mInitialReset;
    protected Button mGoogleSignIn;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextToSpeech speech;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = (EditText) findViewById(R.id.editTextEmail);
        mPasswordField = (EditText) findViewById(R.id.editTextPassword);
        mButtonLogin = (Button) findViewById(R.id.buttonLogin);
        mInitialRegister = (Button) findViewById(R.id.buttonInitialRegister);
        mInitialReset = (Button) findViewById(R.id.buttonInitialReset);
        mGoogleSignIn = (Button) findViewById(R.id.sign_in_button_by_Google);
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeUserToGoogleSignIn = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                startActivity(takeUserToGoogleSignIn);

            }
        });
        mInitialRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takeUserToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(takeUserToRegister);

            }
        });
        mInitialReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takeUserPasswordReset = new Intent(LoginActivity.this, PasswordResetActivity.class);
                startActivity(takeUserPasswordReset);

            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();


                if (email.equals("") || password.equals("")) {

                    speech = new TextToSpeech(LoginActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                speech.setLanguage(Locale.UK);
                                String toSpeak = ("Please provide correct login Detail");
                                speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(LoginActivity.this, toSpeak,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                } else {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (task.isSuccessful()) {
                                        Intent takeUserHome = new Intent(LoginActivity.this, NavigationActivity.class);
                                        startActivity(takeUserHome);
                                    } else if (!task.isSuccessful()) {

                                        Log.w(TAG, "signInWithEmail", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent takeUserHome = new Intent(LoginActivity.this, LoginActivity.class);
                                        startActivity(takeUserHome);
                                    }

                                }
                            });


                }
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this, "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent takeUserResetPassword = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(takeUserResetPassword);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
            finish();

        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }


    }


    // [END onactivityresult]

    // [START auth_with_google]


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
