package assistance.shopping.msc.assistant.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.model.User;
import assistance.shopping.msc.assistant.support.BaseActivity;

import static assistance.shopping.msc.assistant.R.attr.title;
import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    private DatabaseReference mDatabase;
    private EditText mTitleField;
    private EditText mBodyField;
    private static View view;
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            view = inflater.inflate(R.layout.fragment_chat, container, false);




        mTitleField = (EditText) view.findViewById(R.id.messageText);

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMessage();
            }
        });



        // Inflate the layout for this fragment

     } catch (InflateException e) {
        /* map is already there, just return view as it is */
    }

    return view;

    }

    private void submitMessage() {

       final String message = mTitleField.getText().toString();
        if (TextUtils.isEmpty(message)) {

            mTitleField.setError("Required");
            return;
        }
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase.child("users");
            mDatabase.child(userId);
            mDatabase.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {
                                // User is null, error out
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(getActivity(),
                                        "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Write new post
                                mDatabase.setValue(userId, message);

                            }

                            // Finish this Activity, back to the stream
                            // finish();
                            // [END_EXCLUDE]
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
            // [END single_value_read]
        }

    }



