package assistance.shopping.msc.assistant.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
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
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import assistance.shopping.msc.assistant.R;
import assistance.shopping.msc.assistant.main.NavigationActivity;

import static android.content.Context.LOCATION_SERVICE;
import static assistance.shopping.msc.assistant.fragments.ShoppingListFragment.PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class StreetFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final View TODO = null;
    private static View view;
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public LocationManager locationManager;
    public Bundle bundle;
    public double currentlatitude;
    public double currentlongitude;
    public String lat;
    public String lon;
    private StreetViewPanoramaFragment streetViewPanoramaFragment;


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
        try {
            view = inflater.inflate(R.layout.fragment_street, container, false);
            SharedPreferences preferences = this.getActivity().getSharedPreferences(PREFS_NAME, 0);

            lat = preferences.getString("current_lat", "");
            lon = preferences.getString("current_lon", "");
            locationManager = (LocationManager) this.getActivity().getSystemService(LOCATION_SERVICE);

            streetViewPanoramaFragment = (StreetViewPanoramaFragment) getActivity().getFragmentManager().findFragmentById(R.id.streetView);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                /**
                 * Called when the location has changed.
                 * <p>
                 * <p> There are no restrictions on the use of the supplied Location object.
                 *
                 * @param location The new location, as a Location object.
                 */
                @Override
                public void onLocationChanged(Location location) {


                    streetViewPanoramaFragment.getStreetViewPanorama().setPosition(new LatLng(location.getLatitude(), location.getLongitude()));

                }

                /**
                 * Called when the provider status changes. This method is called when
                 * a provider is unable to fetch a location or if the provider has recently
                 * become available after a period of unavailability.
                 *
                 * @param provider the name of the location provider associated with this
                 *                 update.
                 * @param status   {@link LocationProvider#OUT_OF_SERVICE} if the
                 *                 provider is out of service, and this is not expected to change in the
                 *                 near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
                 *                 the provider is temporarily unavailable but is expected to be available
                 *                 shortly; and {@link LocationProvider#AVAILABLE} if the
                 *                 provider is currently available.
                 * @param extras   an optional Bundle which will contain provider specific
                 *                 status variables.
                 *                 <p>
                 *                 <p> A number of common key/value pairs for the extras Bundle are listed
                 *                 below. Providers that use any of the keys on this list must
                 *                 provide the corresponding value as described below.
                 *                 <p>
                 *                 <ul>
                 *                 <li> satellites - the number of satellites used to derive the fix
                 */
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                /**
                 * Called when the provider is enabled by the user.
                 *
                 * @param provider the name of the location provider associated with this
                 *                 update.
                 */
                @Override
                public void onProviderEnabled(String provider) {

                }

                /**
                 * Called when the provider is disabled by the user. If requestLocationUpdates
                 * is called on an already disabled provider, this method is called
                 * immediately.
                 *
                 * @param provider the name of the location provider associated with this
                 *                 update.
                 */
                @Override
                public void onProviderDisabled(String provider) {

                }
            });


        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        return view;
    }

    @Override
    public void onStreetViewPanoramaReady(final StreetViewPanorama streetViewPanorama) {

        currentlatitude = Double.parseDouble(lat);
        currentlongitude = Double.parseDouble(lon);
        streetViewPanorama.setPosition(new LatLng(currentlatitude, currentlongitude));


    }
}
