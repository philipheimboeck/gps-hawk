package gps.fhv.at.gps_hawk.helper;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
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

//        Log.v(Constants.PREFERENCES,loc.getProvider());
//        Log.v(Constants.PREFERENCES,Float.toString(loc.getAccuracy()));

        if (isSufficientLocation(loc)) {

            mLastLocation = loc;
            mLastLocationMillis = SystemClock.elapsedRealtime();

            NewLocationEventData data = new NewLocationEventData();
            data.setNrOfSattelites(mNrOfSattelites);
            caller.onLocationChange(loc, data);
        }
        // else: throw away

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(Constants.PREFERENCES, "Provider disabled");
        caller.onProviderDisabled(provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(Constants.PREFERENCES, "Provider enabled");
        caller.onProviderEnabled(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.i(Constants.PREFERENCES, "onStatusChanged");
    }

    private static final int SIGNIFICANT_TIME_CHANGE_GAP = Constants.GPS_MIN_TIME * 20;

    /**
     * determines whether location can be used or not
     * soure: http://developer.android.com/guide/topics/location/strategies.html
     *
     * @param location
     * @return
     */
    protected boolean isSufficientLocation(Location location) {

        if (mLastLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - mLastLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > SIGNIFICANT_TIME_CHANGE_GAP;
        boolean isSignificantlyOlder = timeDelta < -SIGNIFICANT_TIME_CHANGE_GAP;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
//            Log.v(Constants.PREFERENCES," -> isSignificantlyNewer");
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - mLastLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 50;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                mLastLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
//            Log.v(Constants.PREFERENCES," -> isMoreAccurate");
            return true;
        } else if (isNewer && !isLessAccurate) {
//            Log.v(Constants.PREFERENCES," -> isNewer and !isLessAccurate");
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            Log.v(Constants.PREFERENCES," -> isNewer and !isSignificantlyLessAccurate && isFromSameProvider");
            return true;
        }
        return false;

    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
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

