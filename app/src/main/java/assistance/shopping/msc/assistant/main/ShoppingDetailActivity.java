package assistance.shopping.msc.assistant.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.model.Comment;
import assistance.shopping.msc.assistant.model.ShoppingBroadcast;
import assistance.shopping.msc.assistant.model.User;
import assistance.shopping.msc.assistant.support.BaseActivity;

public class ShoppingDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_POST_KEY = "post_key";
    public static final String LATITUDES = "Lat";
    public static final String LOGNITUDES = "Lon";
    private static final String TAG = "ShoppingDetailActivity";
    public DatabaseReference mUserPostReference;
    ShoppingBroadcast model = new ShoppingBroadcast();
    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private FirebaseAuth mAuth;
    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mTimeView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;
    private TextToSpeech speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_detail);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }


        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("shopping-broadcast").child(mPostKey);
        mUserPostReference = FirebaseDatabase.getInstance().getReference()
                .child("user-shopping-broadcast").child(getUid()).child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("shopping-broadcast-comments").child(mPostKey);

        // Initialize Views
        mAuthorView = (TextView) findViewById(R.id.post_author);
        mTitleView = (TextView) findViewById(R.id.post_title);
        mBodyView = (TextView) findViewById(R.id.post_body);
        mTimeView = (TextView) findViewById(R.id.timeStamp);
        mCommentField = (EditText) findViewById(R.id.field_comment_text);
        mCommentButton = (Button) findViewById(R.id.button_post_comment);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get ShoppingBroadcast object and use the values to update the UI

                final ShoppingBroadcast shoppingBroadcast = dataSnapshot.getValue(ShoppingBroadcast.class);
                // [START_EXCLUDE]
                mAuthorView.setText(shoppingBroadcast.shoppingAssistant);
                mTitleView.setText(shoppingBroadcast.shoppingBroadcastTitle);
                mBodyView.setText(shoppingBroadcast.shoppingBroadcastDescription);

                new DownloadImageTask((ImageView) findViewById(R.id.post_author_photo))
                        .execute(String.valueOf(shoppingBroadcast.shoppingAssistantPhotoUrl));

                // mTimeView.setText(shoppingBroadcast.createdAt);
                // [END_EXCLUDE]
                if (shoppingBroadcast.uid.equals(mAuth.getCurrentUser().getUid())) {
                    mTitleView.setText(shoppingBroadcast.shoppingBroadcastTitle + ".......");
                    mBodyView.setText(shoppingBroadcast.shoppingBroadcastDescription + ".......");
                    mCommentField.setHint("Respond to shopping request");
                    mTitleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_icon, 0);
                    mBodyView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_icon, 0);

                    mTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Add value event listener to the post
                            // [START post_value_event_listener]
                            AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingDetailActivity.this);

                            final EditText edittext = new EditText(ShoppingDetailActivity.this);

                            edittext.setText(shoppingBroadcast.shoppingBroadcastTitle);
                            alert.setTitle("Update");
                            alert.setMessage("Shopping Broadcast Title");
                            alert.setView(edittext);

                            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {


                                    final String YouEditTextValue = edittext.getText().toString();

                                    mPostReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            mPostReference.child("shoppingBroadcastTitle").setValue(YouEditTextValue);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mUserPostReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            mUserPostReference.child("shoppingBroadcastTitle").setValue(YouEditTextValue);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // what ever you want to do with No option.


                                }
                            });

                            alert.show();


                        }
                    });
                    mBodyView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Add value event listener to the post
                            // [START post_value_event_listener]
                            AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingDetailActivity.this);

                            final EditText edittext = new EditText(ShoppingDetailActivity.this);

                            edittext.setText(shoppingBroadcast.shoppingBroadcastDescription);

                            alert.setTitle("Update");
                            alert.setMessage("Shopping Broadcast Description");

                            alert.setView(edittext);

                            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //What ever you want to do with the value

                                    //OR
                                    final String YouEditTextValue = edittext.getText().toString();

                                    mPostReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            mPostReference.child("shoppingBroadcastDescription").setValue(YouEditTextValue);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    mUserPostReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            mUserPostReference.child("shoppingBroadcastDescription").setValue(YouEditTextValue);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // what ever you want to do with No option.
                                }
                            });

                            alert.show();


                        }
                    });


                } else {

                    mCommentField.setHint("Request Shopping......");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting ShoppingBroadcast failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ShoppingDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_post_comment:
                postComment();
                break;
        }
    }

    private void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String shoppingAssistantName = user.UserName;
                        String shoppingAssistantPhoto = user.UserPhoto;


                        // Create new comment object
                        String shoppingBroadcast = mCommentField.getText().toString();

                        if (shoppingBroadcast.equals("")) {

                            speech = new TextToSpeech(ShoppingDetailActivity.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        speech.setLanguage(Locale.UK);
                                        String toSpeak = ("Please add some request or response");
                                        speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(ShoppingDetailActivity.this, toSpeak,
                                                Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

                        } else {

                            Comment comment = new Comment(uid, shoppingAssistantName, shoppingBroadcast, shoppingAssistantPhoto);

                            // Push the comment, it will appear in the list
                            mCommentsReference.push().setValue(comment);

                            // Clear the field
                            mCommentField.setText(null);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;
        public ImageView shoppingRequestPhoto;


        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.comment_author);
            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
            shoppingRequestPhoto = (ImageView) itemView.findViewById(R.id.comment_photo);


        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();


        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);

                    notifyItemInserted(mComments.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Replace with the new data
                        mComments.set(commentIndex, newComment);

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);

            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
            final String checkReg = "^(([gG][iI][rR] {0,}0[aA]{2})|((([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y]?[0-9][0-9]?)|(([a-pr-uwyzA-PR-UWYZ][0-9][a-hjkstuwA-HJKSTUW])|" +
                    "([a-pr-uwyzA-PR-UWYZ][a-hk-yA-HK-Y][0-9][abehmnprv-yABEHMNPRV-Y]))) {0,}[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2}))$";

            final String PostCode = holder.bodyView.getText().toString();

            if (PostCode.matches(checkReg)){

                holder.bodyView.setTextColor(Color.parseColor("#000000"));
                holder.bodyView.setTypeface(null, Typeface.ITALIC);
                holder.bodyView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.loc_item, 0);

            }

            new DownloadImageTask((holder.shoppingRequestPhoto)).execute(comment.ShoppingAssistantPhoto);
            // Toast.makeText(ShoppingDetailActivity.this, "Welcome to the Shopping Assistant", Toast.LENGTH_LONG).show();

            holder.bodyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (PostCode.matches(checkReg)) {

                        final Geocoder geocoder = new Geocoder(ShoppingDetailActivity.this, Locale.getDefault());
                        final String zip = PostCode;
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                // Use the address as needed
                                String message = "You are going to " + address.getAddressLine(0).toUpperCase() + " " + address.getLocality().toUpperCase() ;
                                Toast.makeText(ShoppingDetailActivity.this, message,Toast.LENGTH_LONG).show();

                                Double sendLat = address.getLatitude();
                                Double sendLon = address.getLongitude();

                                // Create fragment and give it an argument for the selected article

                                Bundle args = new Bundle();

                                args.putDouble("LatMap", sendLat);
                                args.putDouble("LonMap", sendLon);

                                Intent intent = new Intent(ShoppingDetailActivity.this, NavigationActivity.class);
                                intent.putExtras(args);
                                startActivity(intent);

                            } else {
                                // Display appropriate message when Geocoder services are not available

                                Toast.makeText(ShoppingDetailActivity.this, "Sorry unable to find location", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            // handle exception
                        }


                    } else {
                        // Display appropriate message when Geocoder services are not available
                        Toast.makeText(ShoppingDetailActivity.this, "Sorry unable to find location", Toast.LENGTH_LONG).show();
                    }


                }
            });

        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
}
