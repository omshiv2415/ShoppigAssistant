package assistance.shopping.msc.assistant.fragments;


import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import assistance.shopping.msc.assistant.R;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StreetFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {


    private static final View TODO = null;

    public StreetFragment() {
        // Required empty public constructor
    }


    private static View view;

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
        final StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getActivity().getFragmentManager()
                        .findFragmentById(R.id.streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        // Getting LocationManager object from System Service LOCATION_SERVICE
        final LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        final String provider = locationManager.getBestProvider(criteria, true);



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
        final Location location = locationManager.getLastKnownLocation(provider);


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, new android.location.LocationListener() {
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


}
