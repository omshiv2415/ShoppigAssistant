package assistance.shopping.msc.assistant.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.NavigationActivity;

import static android.content.Context.LOCATION_SERVICE;
import static assistance.shopping.msc.assistant.fragments.ShoppingListFragment.PREFS_NAME;
import static assistance.shopping.msc.assistant.main.NavigationActivity.MY_PERMISSIONS_REQUEST_LOCATION;


/**
 * A simple {@link Fragment} subclass.
 */
public class StreetFragment extends Fragment implements OnStreetViewPanoramaReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private static final View TODO = null;
    private static View view;
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public  LocationManager locationManager;
    public  Bundle bundle;
    public double currentlatitude;
    public double currentlongitude;
    public Location mLastLocation;

    NavigationActivity navigationActivity = new NavigationActivity();

    public StreetFragment() {
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
        try{
        view = inflater.inflate(R.layout.fragment_street, container, false);

            locationManager = (LocationManager) this.getActivity().getSystemService(LOCATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    checkLocationPermission();

            } else {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    showGPSDisabledAlertToUser();
                }
            }
            buildGoogleApiClient();

            final StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getActivity().getFragmentManager().findFragmentById(R.id.streetView);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
            final LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                streetViewPanoramaFragment.getStreetViewPanorama().setPosition(new LatLng(location.getLatitude(), location.getLongitude()));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });


        }catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        return view;
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {

        streetViewPanorama.setPosition(new LatLng(-33.87365, 151.20689));
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                showGPSDisabledAlertToUser();
            }

            return true;
        }
    }

    public synchronized void buildGoogleApiClient() {
        Log.i("TAG", "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity().getApplicationContext())
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this.getActivity())
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this.getActivity())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                        {
                            showGPSDisabledAlertToUser();
                        }

                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }

                    }
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this.getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);


                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }




    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);

        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            currentlatitude = mLastLocation.getLatitude();
            currentlongitude = mLastLocation.getLongitude();

            double myLat = currentlatitude;
            double myLon = currentlongitude;


            Toast.makeText(this.getActivity(), "current Lat" + myLat, Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
