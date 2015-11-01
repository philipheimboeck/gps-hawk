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
import java.util.List;
import java.util.Locale;

import gps.fhv.at.gps_hawk.Constants;
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

    public GpsWorker(LocationManager locationManager, Context context) {
        mLocationManager = locationManager;
        mContext = context;
        mLocationListener = new MyLocationListener(this);
    }

    @Override
    public void startGpsTracking() {

        try {

            //noinspection ResourceType
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.MIN_TIME, Constants.MIN_DIST_CHANGE, mLocationListener);
            mLocationManager.addGpsStatusListener(mLocationListener);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = mLocationManager.getBestProvider(criteria, false);

            @SuppressWarnings("ResourceType")
            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {
                Log.i("Debug: ", location.toString());
            }
        } catch (SecurityException ex) {
            Log.e("Security", "Permission not granted!");
        } catch (Exception ex) {
            Log.e("Debug", "Error at starting GpsTracking", ex);
        }
    }

    @Override
    public void stopGpsTracking() {
        try {
            // Stop the tracking
            mLocationManager.removeUpdates(mLocationListener);
        } catch (SecurityException ex) {
            Log.e("Security", "Permission not granted!");
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
            e.printStackTrace();
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
        Waypoint waypoint = WaypointFactory.getInstance().createWaypoint(location, data);

        // Send a new message
        Intent intent = new Intent(Constants.BROADCAST_NEW_WAYPOINT);
        intent.putExtra(Constants.EXTRA_WAYPOINT, waypoint);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void onProviderEnabled(String provider) {
        startGpsTracking();
    }

    @Override
    public void onProviderDisabled(String provider) {
        stopGpsTracking();
    }

    @Override
    public Iterable<GpsSatellite> getSatellites() {
        return mLocationManager.getGpsStatus(null).getSatellites();
    }
}