package assistance.shopping.msc.assistant.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.fragments.ChatFragment;
import assistance.shopping.msc.assistant.fragments.HistoryFragment;
import assistance.shopping.msc.assistant.fragments.MapFragmentView;
import assistance.shopping.msc.assistant.fragments.MyCompletedShoppingBroadcastFragment;
import assistance.shopping.msc.assistant.fragments.MyProfileFragment;
import assistance.shopping.msc.assistant.fragments.MyShoppingBroadcastFragment;
import assistance.shopping.msc.assistant.fragments.NewShoppingFragment;
import assistance.shopping.msc.assistant.fragments.PaymentFragment;
import assistance.shopping.msc.assistant.fragments.RecentShoppingBroadcastFragment;
import assistance.shopping.msc.assistant.fragments.ShoppingPointFragment;
import assistance.shopping.msc.assistant.fragments.StreetFragment;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Navigation";
    private static final int REQUEST_INVITE = 16;

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseAuth.AuthStateListener mAuthListener;
    public FragmentPagerAdapter mPagerAdapter;
    public ViewPager mViewPager;
    public FloatingActionButton floatingActionButton;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if (bundle == null) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


        } else {

            Double latV = bundle.getDouble("Lat");
            Double lonV = bundle.getDouble("Lon");


            if (!latV.equals("") || !lonV.equals("")) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                // Create fragment and give it an argument for the selected article
                MapFragmentView sendLatLangToMap = new MapFragmentView();

                Bundle args = new Bundle();

                args.putDouble("Lat", latV);
                args.putDouble("Lon", lonV);

                sendLatLangToMap.setArguments(args);

                FragmentTransaction transaction = NavigationActivity.this.getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, sendLatLangToMap);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();


            }

        }


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {


        }

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final LocationManager locationManager = (LocationManager) NavigationActivity.this.getSystemService(Context.LOCATION_SERVICE);

                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


                            showToastMessage("Please turn off Location");
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                            Intent intent_finish = new Intent(Intent.ACTION_MAIN);
                            intent_finish.addCategory(Intent.CATEGORY_HOME);
                            intent_finish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            NavigationActivity.this.finish();


                        } else {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            NavigationActivity.this.finish();
                        }

                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.update_profile) {

            GoToMyProfile();

            return true;

        } else if (id == R.id.invites) {

            GoToShare();

            return true;
        } else if (id == R.id.logout) {


            FirebaseAuth.getInstance().signOut();
            mAuth.removeAuthStateListener(mAuthListener);
            Intent takeUserHome = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivity(takeUserHome);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

            startActivity(new Intent(NavigationActivity.this, NavigationActivity.class));

        } else if (id == R.id.nav_chat) {

            GoToChat();

        } else if (id == R.id.nav_direction) {

            GoToDirection();


        } else if (id == R.id.nav_streetView) {

            GoToStreetView();

        } else if (id == R.id.nav_history) {

            GoToHistory();


        } else if (id == R.id.nav_shoppingPoints) {

            GoToShoppingPoints();


        } else if (id == R.id.nav_payment) {

            GoToPayment();

        } else if (id == R.id.nav_my_details) {

            GoToMyProfile();

        } else if (id == R.id.nav_share) {

            GoToShare();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new RecentShoppingBroadcastFragment(),
                    new MyShoppingBroadcastFragment(),
                    new MyCompletedShoppingBroadcastFragment(),
                    new MapFragmentView(),
                    new StreetFragment(),
                    new ShoppingPointFragment(),
                    new PaymentFragment(),
                    new MyProfileFragment()

            };

            private final String[] mFragmentNames = new String[] {
                    "Shopping Assistant",
                    "Shopping Broadcast",
                    "Completed Shopping",
                    "Direction",
                    "Street View",
                    "Shopping Points",
                    "Payment",
                    "My Profile"
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

        };



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(mPagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(mViewPager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_new_post);

        // Button launches NewPostActivity


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    //All location services are disabled
                    // Toast toast = Toast.makeText(NavigationActivity.this, "Please turn on Location", Toast.LENGTH_LONG);

                    //  toast.show();

                    showToastMessage("Please Turn On Location");

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);


                }else{

                    NewShoppingFragment fragment = new NewShoppingFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }}
        });


    }

    public void showToastMessage(String Showtoast) {


        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(Showtoast);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(layout);
        toast.show();


    }

    public void GoToChat() {

        // need to implement chat feature
        ChatFragment fragment = new ChatFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public void GoToDirection() {

        MapFragmentView fragment = new MapFragmentView();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void GoToStreetView() {

        StreetFragment fragment = new StreetFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public void GoToHistory() {

        HistoryFragment fragment = new HistoryFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void GoToShoppingPoints() {

        ShoppingPointFragment fragment = new ShoppingPointFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public void GoToPayment() {

        PaymentFragment fragment = new PaymentFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public void GoToMyProfile() {

        MyProfileFragment fragment = new MyProfileFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public void GoToShare() {


        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }


}
