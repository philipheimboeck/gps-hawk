package gps.fhv.at.gps_hawk.workers;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.domain.events.NewLocationEventData;
import gps.fhv.at.gps_hawk.helper.MyLocationListener;

/**
 * Created by Tobias on 23.10.2015.
 */
public class GpsWorker implements IGpsWorker, MyLocationListener.MyLocationListenerCaller {

    private LocationManager mLocationManager;
    private Context mContext;
    private MyLocationListener mLocationListener;
    private Track mCurrentTrack;

    public GpsWorker(LocationManager locationManager, Context context) {
        mLocationManager = locationManager;
        mContext = context;
        mLocationListener = new MyLocationListener(this);
    }

    @Override
    public void startGpsTracking(Track t) {

        try {

            mCurrentTrack = t;

            //noinspection ResourceType
            int gpsTime = (int) SettingsWorker.getInstance().getSetting(Constants.SETTING_GPS_MIN_TIME);
            float gpsDistance = (float) SettingsWorker.getInstance().getSetting(Constants.SETTING_GPS_MIN_DIST_CHANGE);

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsTime, gpsDistance, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, gpsTime, gpsDistance, mLocationListener);
//            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, gpsTime, gpsDistance, mLocationListener);
            mLocationManager.addGpsStatusListener(mLocationListener);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = mLocationManager.getBestProvider(criteria, false);

            Log.v(Constants.PREFERENCES, "GpsWorker started GPS Tracking");

            @SuppressWarnings("ResourceType")
            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {
                Log.i(Constants.PREFERENCES, location.toString());
            }
        } catch (SecurityException ex) {
            Log.e(Constants.PREFERENCES, "Permission not granted!");
        } catch (Exception ex) {
            Log.e(Constants.PREFERENCES, "Error at starting GpsTracking", ex);
        }
    }

    @Override
    public void stopGpsTracking(int trackIsValid) {
        try {
            // Stop the tracking
            mLocationManager.removeUpdates(mLocationListener);

            Calendar end = Calendar.getInstance();

            // In case through UI the track changed, reload it
            DbFacade db = DbFacade.getInstance(mContext);
            Track t = db.select(mCurrentTrack.getId(), Track.class);
            t.setEndDateTime((int) (end.getTimeInMillis() / 1000));
            t.setIsValid(trackIsValid);

            db.saveEntity(t);
        } catch (SecurityException ex) {
            Log.e(Constants.PREFERENCES, "Permission not granted!");
        }
    }

    /**
     * Gets possible adresses from a location
     *
     * @param loc The location
     * @return the address
     */
    protected List<Address> getAddress(Location loc) {
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            Log.e(Constants.PREFERENCES, "Failed to resolve adress from location", e);
        }
        return addresses;
    }


    /**
     * check if gps/network is available to get gps data
     *
     * @return
     */
    @Override
    public boolean isGpsAvailable() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return true;
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return true;
        return false;
    }


    @Override
    public void onLocationChange(Location location, NewLocationEventData data) {

        data.setTrack(mCurrentTrack);

        Waypoint waypoint = WaypointFactory.getInstance().createWaypoint(location, data);

        // Send a new message
        Intent intent = new Intent(Constants.BROADCAST_NEW_WAYPOINT);
        intent.putExtra(Constants.EXTRA_WAYPOINT, waypoint);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Todo
        Log.d(Constants.PREFERENCES, "ProviderEnabled - startGpsTracking?");
//        startGpsTracking();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Todo: get some user-insert if Track was valid?
        stopGpsTracking(0);
    }

    @Override
    public Iterable<GpsSatellite> getSatellites() {
        return mLocationManager.getGpsStatus(null).getSatellites();
    }
}
