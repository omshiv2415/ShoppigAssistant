package assistance.shopping.msc.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends Activity {

    private EditText mEmailFieldRegister;
    private EditText mPasswordFieldRegister;
    protected Button mButtonRegisterRegister;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;


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

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.


                                Intent takeUserHome = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(takeUserHome);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // [START_EXCLUDE]
                                // hideProgressDialog();
                                // [END_EXCLUDE]
                            }
                        });

            }
        });


      }





    }

