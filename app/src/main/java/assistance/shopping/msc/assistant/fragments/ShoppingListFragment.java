package assistance.shopping.msc.assistant.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.ShoppingBroadcastViewHolder;
import assistance.shopping.msc.assistant.main.ShoppingDetailActivity;
import assistance.shopping.msc.assistant.model.ShoppingBroadcast;
import assistance.shopping.msc.assistant.support.FragmentSupport;

import static android.R.style.Theme_Dialog;


public abstract class ShoppingListFragment extends Fragment {

    private static final String TAG = "ShoppingListFragment";
    private static final int TAG_SIMPLE_NOTIFICATION = 1;
    public ProgressBar Shopping;
    FragmentSupport fragmentSupport = new FragmentSupport();
    final String userId = fragmentSupport.getUid();
    // [END define_database_reference]
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ShoppingBroadcast, ShoppingBroadcastViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;



    public ShoppingListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_shopping_request, container, false);


        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        // Query postsQuery = getQuery(mDatabase).orderByChild("starCount").equalTo("In Process");

        mAdapter = new FirebaseRecyclerAdapter<ShoppingBroadcast, ShoppingBroadcastViewHolder>(ShoppingBroadcast.class, R.layout.item_post,
                ShoppingBroadcastViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ShoppingBroadcastViewHolder viewHolder, final ShoppingBroadcast model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // showSimpleNotification(model.body);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.starCount.equals("Completed")) {

                            Toast.makeText(getActivity(), "Transaction is Completed", Toast.LENGTH_LONG).show();

                        } else {

                            Intent intent = new Intent(getActivity(), ShoppingDetailActivity.class);
                            intent.putExtra(ShoppingDetailActivity.EXTRA_POST_KEY, postKey);
                            startActivity(intent);


                        }

                    }
                });

                if (model.ShoppingAssistantPhoto == null) {
                    viewHolder.shoppingAssistantPhoto
                            .setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(),
                                            R.drawable.profile));
                } else {
                    Glide.with(getActivity())
                            .load(model.ShoppingAssistantPhoto)
                            .into(viewHolder.shoppingAssistantPhoto);
                }


                // Determine if the current user has liked this post and set UI accordingly

                if (model.starCount.equals("Completed")) {

                    viewHolder.starView.setImageResource(R.drawable.confirm_shopping_basket);
                    viewHolder.rel.setBackgroundColor(Color.parseColor("#CCC0C0C0"));


                } else {


                    viewHolder.starView.setImageResource(R.drawable.in_process_shopping);
                }


                // Bind ShoppingBroadcast to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {


                        if (model.starCount.equals("Completed")) {
                            Toast.makeText(getActivity(), "Transaction is Completed", Toast.LENGTH_LONG).show();

                        } else {

                            // Need to write to both places the post is stored
                            final DatabaseReference globalPostRef = mDatabase.child("shopping-broadcast").child(postRef.getKey());
                            final DatabaseReference userPostRef = mDatabase.child("user-shopping-broadcast").child(model.uid).child(postRef.getKey());


                            final CharSequence[] items = {" Cash ", " Card ", "Android Pay ", " PayPal "};

                            // Creating and Building the Dialog
                            final AlertDialog.Builder builder = new AlertDialog.Builder(
                                    new ContextThemeWrapper(getActivity(), Theme_Dialog));
                            builder.setTitle("Please confirmed payment type");


                            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {


                                    switch (item) {
                                        case 0:
                                            // Run two transactions
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                            builder.setTitle("Confirm");
                                            builder.setMessage("Are you sure?");

                                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Do nothing but close the dialog
                                                    onStarClicked(globalPostRef);
                                                    onStarClicked(userPostRef);
                                                    dialog.dismiss();
                                                }
                                            });

                                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    // Do nothing
                                                    dialog.dismiss();
                                                }
                                            });

                                            AlertDialog alert = builder.create();
                                            alert.show();


                                            break;
                                        case 1:
                                            // Run two transactions

                                            Toast.makeText(getActivity(), "This Payment available very soon", Toast.LENGTH_LONG).show();

                                            break;
                                        case 2:

                                            Toast.makeText(getActivity(), "This Payment available very soon", Toast.LENGTH_LONG).show();

                                            break;
                                        case 3:

                                            Toast.makeText(getActivity(), "This Payment available very soon", Toast.LENGTH_LONG).show();
                                            break;

                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();

                        }



                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);

    }


    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ShoppingBroadcast p = mutableData.getValue(ShoppingBroadcast.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.uid.equals(userId)) {

                    if (p.stars.containsKey(getUid())) {
                        // Unstar the post and remove self from stars
                        p.starCount = "In Process";
                        p.stars.remove(getUid());
                    } else {
                        // Star the post and add self to stars
                        p.starCount = "Completed";
                        p.stars.put(getUid(), true);

                    }
                }
                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    private void showSimpleNotification(String shoppingBrodcastText) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Use the NotificationCompat compatibility library in order to get gingerbread support.
        Notification notification = new NotificationCompat.Builder(getActivity())

                //Title of the notification
                .setContentTitle("Hello")
                //Content of the notification once opened
                .setContentText(shoppingBrodcastText)
                //This bit will show up in the notification area in devices that support that
                .setTicker(" I am Fine")
                //Icon that shows up in the notification area
                .setSmallIcon(R.drawable.shopping_assistant)
                //Icon that shows up in the drawer
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.shopping_assistant))
                //Set the intent
                .setContentIntent(pendingIntentForNotification())
                //Build the notification with all the stuff you've just set.
                .setSound(defaultSoundUri)
                .build();

        //Add the auto-cancel flag to make it dismiss when clicked on
        //This is a bitmask value so you have to pipe-equals it.
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        //Grab the NotificationManager and post the notification
        NotificationManager notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        //Set a tag so that the same notification doesn't get reposted over and over again and
        //you can grab it again later if you need to.
        notificationManager.notify(TAG_SIMPLE_NOTIFICATION, notification);
    }

    private PendingIntent pendingIntentForNotification() {
        //Create the intent you want to show when the notification is clicked
        Intent intent = new Intent(getActivity(), ShoppingListFragment.class);

        //This will hold the intent you've created until the notification is tapped.
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, intent, 0);
        return pendingIntent;
    }

}
