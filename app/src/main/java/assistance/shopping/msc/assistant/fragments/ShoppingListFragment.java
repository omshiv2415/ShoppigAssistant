package assistance.shopping.msc.assistant.fragments;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.NavigationActivity;
import assistance.shopping.msc.assistant.main.ShoppingBroadcastViewHolder;
import assistance.shopping.msc.assistant.main.ShoppingDetailActivity;
import assistance.shopping.msc.assistant.model.ShoppingBroadcast;
import assistance.shopping.msc.assistant.model.ShoppingPoints;
import assistance.shopping.msc.assistant.model.User;
import assistance.shopping.msc.assistant.support.FragmentSupport;

import static android.R.style.Theme_Dialog;


public abstract class ShoppingListFragment extends Fragment {

    public static final String PREFS_NAME_START = "MyPreferencesFile1";
    public static final String PREFS_NAME = "MyPreferencesFile";
    private static final String TAG = "ShoppingListFragment";
    private static final int TAG_SIMPLE_NOTIFICATION = 1;
    public ProgressBar Shopping;
    public DatabaseReference mShoppingPoint;
    public FloatingActionButton fab;
    public Double Lat;
    public Double Lon;
    FragmentSupport fragmentSupport = new FragmentSupport();
    final String userId = fragmentSupport.getUid();
    // [END define_database_reference]
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<ShoppingBroadcast, ShoppingBroadcastViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private Location TODO = null;

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
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_new_post);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
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
        // Query postsQuery = getQuery(mDatabase).orderByChild("shoppingStatus").equalTo("In Process");

        mAdapter = new FirebaseRecyclerAdapter<ShoppingBroadcast, ShoppingBroadcastViewHolder>(ShoppingBroadcast.class, R.layout.item_post,
                ShoppingBroadcastViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ShoppingBroadcastViewHolder viewHolder, final ShoppingBroadcast model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // showSimpleNotification(model.shoppingBroadcastDescription);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (model.shoppingStatus.equals("Completed")) {

                            Toast.makeText(getActivity(), "Transaction is Completed", Toast.LENGTH_LONG).show();

                        } else {

                            Intent intent = new Intent(getActivity(), ShoppingDetailActivity.class);
                            intent.putExtra(ShoppingDetailActivity.EXTRA_POST_KEY, postKey);
                            startActivity(intent);

                        }

                    }
                });





                if (model.shoppingAssistantPhotoUrl == null) {
                    viewHolder.shoppingAssistantPhoto
                            .setImageDrawable(ContextCompat
                                    .getDrawable(getActivity(),
                                            R.drawable.profile));
                } else {
                    Glide.with(getActivity())
                            .load(model.shoppingAssistantPhotoUrl)
                            .into(viewHolder.shoppingAssistantPhoto);
                }


                // Determine if the current user has liked this post and set UI accordingly




                if (model.shoppingStatus.equals("Completed")) {

                    viewHolder.starView.setImageResource(R.drawable.confirm_shopping_basket);
                    //  viewHolder.rel.setBackgroundColor(Color.parseColor("#94CAD7"));
                    viewHolder.paymentTypeText.setText(model.paymentType);
                    viewHolder.hide.setVisibility(View.VISIBLE);
                    viewHolder.srcity.setTextColor(Color.parseColor("#000000"));
                    viewHolder.srfirstline.setTextColor(Color.parseColor("#000000"));
                    viewHolder.srpostcode.setTextColor(Color.parseColor("#000000"));
                    viewHolder.spdeliveredtime.setTextColor(Color.parseColor("#000000"));

                    viewHolder.paymentTypeText.setTextColor(Color.parseColor("#000000"));
                    viewHolder.SaName.setTextColor(Color.parseColor("#000000"));
                    viewHolder.transactioncompletedat.setTextColor(Color.parseColor("#000000"));
                    viewHolder.saAddressfirstline.setTextColor(Color.parseColor("#000000"));
                    viewHolder.sacity.setTextColor(Color.parseColor("#000000"));
                    viewHolder.saPostcode.setTextColor(Color.parseColor("#000000"));
                    viewHolder.timeView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.titleView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.bodyView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.authorView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.bodyView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.dotProgressBar.setVisibility(View.GONE);


                } else {

                    viewHolder.starView.setImageResource(R.drawable.in_process_shopping);
                    viewHolder.hide.setVisibility(View.GONE);
                    viewHolder.topline.setVisibility(View.GONE);
                    viewHolder.rel.setBackgroundColor(Color.parseColor("#ffffffff"));

                    viewHolder.saAddressfirstline.setTextColor(Color.parseColor("#000000"));
                    viewHolder.sacity.setTextColor(Color.parseColor("#000000"));
                    viewHolder.timeView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.titleView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.bodyView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.saPostcode.setTextColor(Color.parseColor("#000000"));
                    viewHolder.saPostcode.setTypeface(null, Typeface.ITALIC);
                    viewHolder.saPostcode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.loc_item, 0);
                    viewHolder.authorView.setTextColor(Color.parseColor("#000000"));
                    viewHolder.bodyView.setTextColor(Color.parseColor("#000000"));

                    viewHolder.dotProgressBar.startProgress();

                    viewHolder.bodyView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {


                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            String str = model.createdAt.substring(00,23);

                            DateFormat format = new SimpleDateFormat("dd-MM-yyyy EEE HH:mm:ss", Locale.ENGLISH);

                            long pastTime = 0;
                            try {
                                Date date = format.parse(str);
                                pastTime = date.getTime();
                            } catch (ParseException e) {
                                Log.e("log", e.getMessage(), e);
                            }

                            long curTime = System.currentTimeMillis();

                            pastTime = pastTime + 20000;

                            if (curTime < pastTime){

                                showSimpleNotification(model.shoppingAssistantName + " is doing shopping at " +  model.saFirstLineAddress,model.shoppingBroadcastTitle);
                            }


                        }

                    });


                    viewHolder.saPostcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Geocoder geocoder = new Geocoder(getContext());
                            final String zip = viewHolder.saPostcode.getText().toString();
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    // Use the address as needed
                                   // String message = String.format("Latitude: %f, Longitude: %f", address.getLatitude(), address.getLongitude());
                                    String message = "You are going to " + address.getAddressLine(0).toUpperCase() + " " + address.getLocality().toUpperCase();
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                                    Double sendLat = address.getLatitude();
                                    Double sendLon = address.getLongitude();

                                    // Create fragment and give it an argument for the selected article


                                    Bundle args = new Bundle();

                                    args.putDouble("LatMap", sendLat);
                                    args.putDouble("LonMap", sendLon);

                                    Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                    intent.putExtras(args);
                                    startActivity(intent);


                                } else {
                                    // Display appropriate message when Geocoder services are not available
                                    Toast.makeText(getActivity(), "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                // handle exception
                            }


                        }
                    });

                }


                // Bind ShoppingBroadcast to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View statusView) {
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            //All location services are disabled
                            Toast.makeText(getActivity(), "Please turn on Location and press back Button", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        } else {
                            if (model.shoppingStatus.equals("Completed")) {
                                Toast.makeText(getActivity(), "Shopping Assistant is not Available", Toast.LENGTH_LONG).show();
                            } else if ((!model.uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                                Toast.makeText(getActivity(), "This is not your shopping Broadcast", Toast.LENGTH_LONG).show();
                            } else {
                                // Need to write to both places the broadcast is stored
                                final DatabaseReference globalPostRef = mDatabase.child("shopping-broadcast").child(postRef.getKey());
                                final DatabaseReference userPostRef = mDatabase.child("user-shopping-broadcast").child(model.uid).child(postRef.getKey());
                                final CharSequence[] items = {"Cash", "Apple Pay", "Android Pay", "Debit or Credit Card", "PayPal"};
                                // Creating and Building the Dialog
                                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), Theme_Dialog));
                                builder.setTitle("Please confirm payment type");
                                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, final int item) {
                                        switch (item) {
                                            case 0:
                                                // Run two transactions
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Confirm");
                                                builder.setMessage("Are you sure?");
                                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog
                                                        String getPaymentType = String.valueOf(Arrays.asList(items).get(0));

                                                        onStatusClicked(globalPostRef, getPaymentType);
                                                        onStatusClicked(userPostRef, getPaymentType);
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
                                                Toast.makeText(getActivity(), "This Payment will be available very soon", Toast.LENGTH_LONG).show();
                                                break;
                                            case 2:
                                                Toast.makeText(getActivity(), "This Payment will be available very soon", Toast.LENGTH_LONG).show();
                                                break;
                                            case 3:
                                                Toast.makeText(getActivity(), "This Payment will be available very soon", Toast.LENGTH_LONG).show();
                                                break;
                                            case 4:
                                                Toast.makeText(getActivity(), "This Payment will be available very soon", Toast.LENGTH_LONG).show();
                                                break;

                                        }
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();

                            }


                        }
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);

    }


    //
    private void onStatusClicked(DatabaseReference postRef, final String PaymentType) {

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy EEE HH:mm:ss a");
        Date date = new Date();
        final String transactionCompletedAt = String.valueOf(dateFormat.format(date)).toUpperCase();

        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final ShoppingBroadcast p = mutableData.getValue(ShoppingBroadcast.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.uid.equals(userId)) {

                    if (p.status.containsKey(getUid())) {
                        // Unstar the post and remove self from status
                        p.shoppingStatus = "Processing";
                        p.status.remove(getUid());


                    } else {
                        // Star the post and add self to status

                        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);

                        String lat = preferences.getString("current_lat", "");
                        String lon = preferences.getString("current_lon", "");

                        Lat = Double.parseDouble(lat);
                        Lon = Double.parseDouble(lon);
                        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());

                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(Lat, Lon, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {


                            String City = addresses.get(0).getLocality();
                            String PostCode = addresses.get(0).getPostalCode();
                            String firstLineOfAddress = addresses.get(0).getAddressLine(0);
                            p.shoppingStatus = "Completed";
                            p.status.put(getUid(), true);
                            p.paymentType = PaymentType;
                            p.paymentCompletedAt = transactionCompletedAt;
                            p.srPostCode = PostCode;
                            p.srCity = City;
                            p.srFirstLineAddress = firstLineOfAddress;


                        } else {

                            Toast.makeText(getActivity(), "Please try agin no location found", Toast.LENGTH_LONG).show();
                        }

                    }
                    mShoppingPoint = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                    mShoppingPoint.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User point = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (point == null) {
                                // User is null, error out
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(getActivity(),
                                        "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                Double earnedShopPoints = 50.00;
                                Double totalShopPoint = point.TotalshoppingPoints + earnedShopPoints;
                                mShoppingPoint.child("TotalshoppingPoints").setValue(totalShopPoint);
                                writeNewShoppingPoint(p.uid, p.shoppingAssistantName, p.createdAt, p.paymentCompletedAt,
                                        totalShopPoint, earnedShopPoints, p.shoppingAssistantPhotoUrl, FirebaseAuth.getInstance().getCurrentUser().getEmail());


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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


    // [START write_fan_out]
    private void writeNewShoppingPoint(String userId, String shoppingAssistantName, String createdAt, String paymentCompletedAt, Double totalShoppingPoints,
                                       Double earnedShoppingPoints, String sAPhotoUrl, String sAEmail) {

        String key = mDatabase.child("shopping-broadcast").push().getKey();


        ShoppingPoints shoppingPoints = new ShoppingPoints(userId, shoppingAssistantName, createdAt, paymentCompletedAt, totalShoppingPoints,
                earnedShoppingPoints, sAPhotoUrl, sAEmail);

        Map<String, Object> postValues = shoppingPoints.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-shopping-points/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


    public abstract Query getQuery(DatabaseReference databaseReference);

    private void showSimpleNotification(String shoppingBrodcastText, String Name) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Use the NotificationCompat compatibility library in order to get gingerbread support.
        Notification notification = new NotificationCompat.Builder(getActivity())

                //Title of the notification
                .setContentTitle(Name)
                //Content of the notification once opened
                .setContentText(shoppingBrodcastText)
                //This bit will show up in the notification area in devices that support that
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
        Intent intent = new Intent(getActivity(), NavigationActivity.class);

        //This will hold the intent you've created until the notification is tapped.
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, intent, 0);
        return pendingIntent;
    }


}
