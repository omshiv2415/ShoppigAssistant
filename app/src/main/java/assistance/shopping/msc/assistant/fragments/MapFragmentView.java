package assistance.shopping.msc.assistant.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import assistance.shopping.msc.assistant.R;

import static android.content.Context.LOCATION_SERVICE;
import static assistance.shopping.msc.assistant.fragments.ShoppingListFragment.PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragmentView extends Fragment {

    private static View view;
    public Double mLat;
    public Double mLon;
    public LocationManager locationManager;
    public Boolean isClicked = true;
    public PolylineOptions mPolylineOptions;


    public MapFragmentView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);

        }
        try {

            view = inflater.inflate(R.layout.fragment_map, container, false);
            SharedPreferences preferences = this.getActivity().getSharedPreferences(PREFS_NAME, 0);

            final String lat = preferences.getString("current_lat", "");
            final String lon = preferences.getString("current_lon", "");

            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {

                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.BLUE).width(10);

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            if (isClicked) {
                                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                isClicked = false;
                            } else {
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                isClicked = true;
                            }


                        }
                    });

                    if (getArguments() != null) {

                        mLat = getArguments().getDouble("Lat");
                        mLon = getArguments().getDouble("Lon");
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(mLat, mLon)).title("You are Here"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLon), 17.0f));


                    } else {

                        mLat = Double.valueOf(lat);
                        mLon = Double.valueOf(lon);
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(mLat, mLon)).title("You are Here"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLon), 17.0f));

                    }
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new android.location.LocationListener() {
                        /**
                         * Called when the location has changed.
                         * <p>
                         * <p> There are no restrictions on the use of the supplied Location object.
                         *
                         * @param location The new location, as a Location object.
                         */
                        @Override
                        public void onLocationChanged(Location location) {


                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.getUiSettings().setMapToolbarEnabled(true);
                            googleMap.getUiSettings().setAllGesturesEnabled(true);
                            googleMap.setIndoorEnabled(true);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0f));
                            googleMap.addPolyline(mPolylineOptions);

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

                    }


                }); // NullPointerException at this line




        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        return view;
    }


}


