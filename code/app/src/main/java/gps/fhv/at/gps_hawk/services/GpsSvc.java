package gps.fhv.at.gps_hawk.services;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Created by Tobias on 23.10.2015.
 */
public class GpsSvc {

    private LocationManager mLocationManager;
    private Context mContext;
    private LocationListener mLocationListener;

    public GpsSvc(LocationManager locationManager, Context context) {
        mLocationManager = locationManager;
        mContext = context;
    }

    public boolean initialize() {

        mLocationListener = new MyLocationListener();

        if (!isGpsAvailable()) {
//            Toast.makeText(mContext, "Bitte GPS einschalten", Toast.LENGTH_LONG);
            return false;
        } else {
            startGpsTracking();
            return true;
        }


    }

    private void startGpsTracking() {

        //noinspection ResourceType
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.MIN_TIME, Constants.MIN_DIST_CHANGE, mLocationListener);
//noinspection ResourceType
        mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = mLocationManager.getBestProvider(criteria, false);

        @SuppressWarnings("ResourceType") Location location = mLocationManager.getLastKnownLocation(provider);

        Toast.makeText(mContext,"ein Test",Toast.LENGTH_LONG);
        if ( location != null ) {
            Log.i("Debug: ", location.toString());
        }
    }

    /*----------Listener class to get coordinates ------------- */
    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

/*
            editLocation.setText("");
            pb.setVisibility(View.INVISIBLE);
*/

            Toast.makeText(mContext, "Location changed : Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("Debug", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("Debug", latitude);

    /*----------to get City-Name from coordinates ------------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = longitude + "\n" + latitude + "\n\nMy Currrent City is: " + cityName;

//            editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("Location Listener", "Provider disabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("Location Listener", "Provider enabled");
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            Log.i("Location Listener", "onStatusChanged");
        }
    }


    /**
     * check if gps/network is available to get gps data
     *
     * @return
     */
    protected boolean isGpsAvailable() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return true;
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return true;
        return false;
    }


}
