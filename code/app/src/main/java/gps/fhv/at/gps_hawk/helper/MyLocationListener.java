package gps.fhv.at.gps_hawk.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tobias on 23.10.2015.
 */
public class MyLocationListener implements LocationListener {
    private Context mContext;
    public MyLocationListener(Context context) {
        mContext = context;
    }

    @Override
    public void onLocationChanged(Location loc) {

/*
            editLocation.setText("");
            pb.setVisibility(View.INVISIBLE);
*/

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

