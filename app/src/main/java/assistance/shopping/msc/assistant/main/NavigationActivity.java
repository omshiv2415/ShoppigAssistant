package assistance.shopping.msc.assistant.main;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.fragments.HistoryFragment;
import assistance.shopping.msc.assistant.fragments.MapFragment;
import assistance.shopping.msc.assistant.fragments.MyCompletedShoppingBroadcastFragment;
import assistance.shopping.msc.assistant.fragments.MyProfileFragment;
import assistance.shopping.msc.assistant.fragments.MyShoppingBroadcastFragment;
import assistance.shopping.msc.assistant.fragments.NewShoppingFragment;
import assistance.shopping.msc.assistant.fragments.PaymentFragment;
import assistance.shopping.msc.assistant.fragments.RecentShoppingBroadcastFragment;
import assistance.shopping.msc.assistant.fragments.ShoppingPointFragment;
import assistance.shopping.msc.assistant.fragments.StreetFragment;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Navigation";
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public DatabaseReference mDatabase;
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    public FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {

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

            // need to implement chat feature

        } else if (id == R.id.nav_direction) {

            MapFragment fragment = new MapFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_streetView) {

            StreetFragment fragment = new StreetFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_history) {

            HistoryFragment fragment = new HistoryFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();



        } else if (id == R.id.nav_shoppingPoints) {

            ShoppingPointFragment fragment = new ShoppingPointFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_payment) {

            PaymentFragment fragment = new PaymentFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_my_details) {

            MyProfileFragment fragment = new MyProfileFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_share) {


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
                    new MyCompletedShoppingBroadcastFragment()
            };
            private final String[] mFragmentNames = new String[] {
                    "Shopping Help Available",
                    "My Shopping Broadcast",
                    "Completed Shopping"
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
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    //All location services are disabled
                    Toast.makeText(NavigationActivity.this, "Please turn on Location and press bacck button", Toast.LENGTH_SHORT).show();
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

}
