package assistance.shopping.msc.assistant;

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

import java.util.Locale;

public class RegisterActivity extends Activity {

    private static final String TAG = "EmailPassword";
    protected Button mButtonRegisterRegister;
    Support support = new Support();
    private EditText mEmailFieldRegister;
    private EditText mPasswordFieldRegister;
    private FirebaseAuth mAuth;
    private TextToSpeech speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailFieldRegister = (EditText) findViewById(R.id.editTextEmail);
        mPasswordFieldRegister = (EditText) findViewById(R.id.editTextPassword);
        mButtonRegisterRegister = (Button) findViewById(R.id.buttonRegister);


        mAuth = FirebaseAuth.getInstance();


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
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    Toast.makeText(RegisterActivity.this, "Welcome to the Shopping Assistant", Toast.LENGTH_LONG).show();
                                    Intent takeUserHome = new Intent(RegisterActivity.this, NavigationActivity.class);
                                    startActivity(takeUserHome);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                }

            }

        });


    }


}

