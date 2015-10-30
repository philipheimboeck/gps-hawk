package gps.fhv.at.gps_hawk.helper;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import gps.fhv.at.gps_hawk.domain.events.NewLocationEventData;

/**
 * Created by Tobias on 23.10.2015.
 */
public class MyLocationListener implements LocationListener, GpsStatus.Listener {

    private MyLocationListenerCaller caller;
    private Location mLastLocation;
    private long mLastLocationMillis;
    private int mNrOfSattelites = -1;

    public MyLocationListener(MyLocationListenerCaller gpsSvc) {
        caller = gpsSvc;
    }

    @Override
    public void onLocationChanged(Location loc) {

        if (loc == null) return;

        mLastLocation = loc;
        mLastLocationMillis = SystemClock.elapsedRealtime();

        if (isSufficientLocation(loc)) {
            NewLocationEventData data = new NewLocationEventData();
            data.setNrOfSattelites(mNrOfSattelites);
            caller.onLocationChange(loc, data);
        }
        // else: throw away

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Location Listener", "Provider disabled");
        caller.onProviderDisabled(provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Location Listener", "Provider enabled");
        caller.onProviderEnabled(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Location Listener", "onStatusChanged");
    }

    /**
     * determines whether location can be used or not
     *
     * @param location
     * @return
     */
    protected boolean isSufficientLocation(Location location) {
        return true;
    }

    /**
     * Method is called to determine number os sattelites
     *
     * @param event
     */
    @Override
    public void onGpsStatusChanged(int event) {
        boolean isGPSFix = false;

        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                if (mLastLocation != null)
                    isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;


                if (isGPSFix) { // A fix has been acquired.
                    // Do something.

                    // Old Code
                    int satellites = 0;
                    int satellitesInFix = 0;

                    for (GpsSatellite sat : caller.getSatellites()) {
                        if (sat.usedInFix()) {
                            satellitesInFix++;
                        }
                        satellites++;
                    }

                    mNrOfSattelites = satellitesInFix;

                } else { // The fix has been lost.
                    // Do something.
                }

                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                // Do something.
                isGPSFix = true;

                break;
        }
    }

    public interface MyLocationListenerCaller {
        void onLocationChange(Location location, NewLocationEventData data);

        void onProviderEnabled(String provider);

        void onProviderDisabled(String provider);

        Iterable<GpsSatellite> getSatellites();
    }
}

