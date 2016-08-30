package assistance.shopping.msc.assistant.fragments;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.NavigationActivity;
import assistance.shopping.msc.assistant.model.ShoppingBroadcast;
import assistance.shopping.msc.assistant.model.User;
import assistance.shopping.msc.assistant.support.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewShoppingFragment extends Fragment {


    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    private static final int TAG_SIMPLE_NOTIFICATION = 1;
    private static final String FIREBASE_URL = "https://testing-2b3ba.firebaseio.com/";
    private static View view;
    BaseActivity baseActivity = new BaseActivity();
    // [END declare_database_ref]
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private EditText mTitleField;
    private EditText mBodyField;
    private FirebaseAuth mAuth;
    private Location TODO = null;

    public NewShoppingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            view = inflater.inflate(R.layout.fragment_new_shopping, container, false);


            // [START initialize_database_ref]
            mDatabase = FirebaseDatabase.getInstance().getReference();
            // [END initialize_database_ref]
            mAuth = FirebaseAuth.getInstance();
            mTitleField = (EditText) view.findViewById(R.id.field_title);
            mBodyField = (EditText) view.findViewById(R.id.field_body);

            view.findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitPost();
                }
            });


        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        return view;
    }

    private void submitPost() {

        final UtilLocation uti = new UtilLocation();



        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();
        final String paymentType = "Cash";
        final String tranCompletedAt = "";
        final String srfirstlineofAddress = "Not Available";
        final String srcity = "Not Available";
        final String srpostcode = "Not Available";

        String SAPhoto = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
        final String SAGPhoto = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());

        if (SAPhoto.isEmpty()) {

            SAPhoto = "https://lh3.googleusercontent.com/-et8-_Jd3MiY/AAAAAAAAAAI/AAAAAAAAAAs/9OWsA3w5ZGw/s96-c/photo.jpg";
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy EEE HH:mm:ss a");
            Date date = new Date();
            final String createdAt = String.valueOf(dateFormat.format(date)).toUpperCase();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // [START single_value_read]
        final String userId = baseActivity.getUid();
            final String finalSAPhoto = SAPhoto;
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(), "Please Update your Profile", Toast.LENGTH_SHORT).show();
                            MyProfileFragment fragment = new MyProfileFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.commit();
                        } else {
                            // Write new post
                            Double Lat = uti.getLastKnownLoaction(true, getContext()).getLatitude();
                            Double Lon =  uti.getLastKnownLoaction(true, getContext()).getLongitude();


                            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());

                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(Lat,Lon, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses.size() > 0) {
                                System.out.println(addresses.get(0).getLocality());

                                String City = addresses.get(0).getLocality();
                                String PostCode = addresses.get(0).getPostalCode();
                                String firstLineOfAddress = addresses.get(0).getAddressLine(0);

                                writeNewPost(userId, user.UserName, title, body, createdAt, finalSAPhoto, paymentType, tranCompletedAt,
                                        user.FirstName, firstLineOfAddress, City, PostCode, srfirstlineofAddress,srcity,srpostcode );
                                showSimpleNotification(body);
                                Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                startActivity(takeUserHome);


                            }else{

                                Toast.makeText(getActivity(), "Please Update your Profile", Toast.LENGTH_SHORT).show();

                                Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                startActivity(takeUserHome);


                            }


                        }

                        // Finish this Activity, back to the stream
                        baseActivity.finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        // [END single_value_read]
        } else {


            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy EEE HH:mm:ss a");
            Date date = new Date();
            final String createdAt = String.valueOf(dateFormat.format(date)).toUpperCase();

            // Title is required
            if (TextUtils.isEmpty(title)) {
                mTitleField.setError(REQUIRED);
                return;
            }

            // Body is required
            if (TextUtils.isEmpty(body)) {
                mBodyField.setError(REQUIRED);
                return;
            }

            // [START single_value_read]
            final String userId = baseActivity.getUid();


            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);

                            // [START_EXCLUDE]
                            if (user == null) {
                                // User is null, error out
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(getActivity(), "Please Update your Profile", Toast.LENGTH_SHORT).show();
                                MyProfileFragment fragment = new MyProfileFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.commit();
                            } else {
                                // Write new post

                                Double Lat = uti.getLastKnownLoaction(true, getContext()).getLatitude();
                                Double Lon =  uti.getLastKnownLoaction(true, getContext()).getLongitude();


                                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());

                                List<Address> addresses = null;
                                try {
                                    addresses = gcd.getFromLocation(Lat,Lon, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (addresses.size() > 0) {
                                    System.out.println(addresses.get(0).getLocality());

                                    String City = addresses.get(0).getLocality();
                                    String PostCode = addresses.get(0).getPostalCode();
                                    String firstLineOfAddress = addresses.get(0).getAddressLine(0);

                                    writeNewPost(userId, user.UserName, title, body, createdAt, SAGPhoto, paymentType, tranCompletedAt,
                                            user.FirstName, firstLineOfAddress, City, PostCode, srfirstlineofAddress,srcity,srpostcode );
                                    showSimpleNotification(body);
                                    Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                    startActivity(takeUserHome);


                                }else{

                                    Toast.makeText(getActivity(), "Please Update your Profile", Toast.LENGTH_SHORT).show();

                                    Intent takeUserHome = new Intent(getActivity(), NavigationActivity.class);
                                    startActivity(takeUserHome);


                                }

                            }

                            // Finish this Activity, back to the stream
                            baseActivity.finish();
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


    // [START write_fan_out]
    private void writeNewPost(String userId, String shoppingAssistant, String title, String body, String createdAt, String ShoppingAssistantPhoto, String paymentType,
                              String paymentCompletedAt, String shoppingAssistantName, String saFirstLineAddress, String saCity, String saPostCode, String srFirstLineAddress,
                              String srCity, String srPostCode) {
        // Create new shoppingBroadcast at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("shopping-broadcast").push().getKey();
        ShoppingBroadcast shoppingBroadcast = new ShoppingBroadcast(userId, shoppingAssistant, title, body, createdAt, ShoppingAssistantPhoto, paymentType,
                paymentCompletedAt,  shoppingAssistantName, saFirstLineAddress, saCity, saPostCode, srFirstLineAddress,srCity,srPostCode);
        Map<String, Object> postValues = shoppingBroadcast.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/shopping-broadcast/" + key, postValues);
        childUpdates.put("/user-shopping-broadcast/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

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

    public class UtilLocation {
        public Location getLastKnownLoaction(boolean enabledProvidersOnly, Context context) {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location utilLocation = null;
            List<String> providers = manager.getProviders(enabledProvidersOnly);
            for (String provider : providers) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return TODO;
                }
                utilLocation = manager.getLastKnownLocation(provider);
                if(utilLocation != null) return utilLocation;
            }
            return null;
        }
    }
}


