package gps.fhv.at.gps_hawk.helper;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import gps.fhv.at.gps_hawk.services.IGpsSvc;

/**
 * Created by Tobias on 23.10.2015.
 */
public class MyLocationListener implements LocationListener, GpsStatus.Listener {
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

    /**
     * Method is called to determine number os sattelites
     * @param event
     */
    @Override
    public void onGpsStatusChanged(int event) {
        int satellites = 0;
        int satellitesInFix = 0;
        int timetofix = mGpsSvc.getLocationManager().getGpsStatus(null).getTimeToFirstFix();
        Log.i("Debug", "Time to first fix = " + timetofix);
        for (GpsSatellite sat : mGpsSvc.getLocationManager().getGpsStatus(null).getSatellites()) {
            if(sat.usedInFix()) {
                satellitesInFix++;
            }
            satellites++;
        }
        Log.i("Debug", satellites + " Used In Last Fix ("+satellitesInFix+")");
    }
}

