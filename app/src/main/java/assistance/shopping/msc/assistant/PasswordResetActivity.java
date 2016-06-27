package assistance.shopping.msc.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends Activity {

    private Button mButtonPasswordReset;
    private EditText mEmailPasswordReset;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);


        mButtonPasswordReset = (Button)findViewById(R.id.buttonPasswordReset);
        mEmailPasswordReset = (EditText)findViewById(R.id.editTextPasswordReset);

        mButtonPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailreset = mEmailPasswordReset.getText().toString().trim();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailreset)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");

                                    Intent takeUserHome = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                    startActivity(takeUserHome);
                                }
                            }
                        });


            }
        });

    }
}
