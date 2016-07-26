package assistance.shopping.msc.assistant.main;

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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.fragments.PaymentFragment;
import assistance.shopping.msc.assistant.support.Support;

public class PasswordResetActivity extends LoginActivity {

    private static final String TAG = "EmailPassword";
    Support support = new Support();
    private Button mButtonPasswordReset;
    private EditText mEmailPasswordReset;
    private TextToSpeech speech;


    public PasswordResetActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);


        mButtonPasswordReset = (Button) findViewById(R.id.buttonPasswordReset);
        mEmailPasswordReset = (EditText) findViewById(R.id.editTextPasswordReset);


        mButtonPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailreset = mEmailPasswordReset.getText().toString().trim();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (!support.isValidEmail(emailreset)) {


                    speech = new TextToSpeech(PasswordResetActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                speech.setLanguage(Locale.UK);
                                String toSpeak = ("Please provide correct email");
                                speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(PasswordResetActivity.this, toSpeak,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    auth.sendPasswordResetEmail(emailreset)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");

                                        Intent takeUserHome = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                        startActivity(takeUserHome);
                                        Toast.makeText(PasswordResetActivity.this, "Please check your email and select new password",
                                                Toast.LENGTH_SHORT).show();
                                    } else if (task.isSuccessful()) {

                                        Log.w(TAG, "signInWithEmail", task.getException());
                                        Toast.makeText(PasswordResetActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent takeUserHome = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                        startActivity(takeUserHome);


                                    }
                                }
                            });

                }
            }
        });

    }
}
