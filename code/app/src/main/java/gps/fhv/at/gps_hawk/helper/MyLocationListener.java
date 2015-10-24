package gps.fhv.at.gps_hawk.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import gps.fhv.at.gps_hawk.services.IGpsSvc;

/**
 * Created by Tobias on 23.10.2015.
 */
public class MyLocationListener implements LocationListener {
    private Context mContext;
    private IGpsSvc mGpsSvc;

    public MyLocationListener(Context context, IGpsSvc gpsSvc) {
        mContext = context;
        mGpsSvc = gpsSvc;
    }

    @Override
    public void onLocationChanged(Location loc) {

        if ( isSufficientLocation(loc)){
            mGpsSvc.addNewLocation(loc);
        }
        // else: throw away

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Location Listener", "Provider disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Location Listener", "Provider enabled");
        mGpsSvc.startGpsTracking();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Location Listener", "onStatusChanged");
    }

    /**
     * determines whether location can be used or not
     * @param location
     * @return
     */
    protected boolean isSufficientLocation(Location location) {
        return true;
    }

}

