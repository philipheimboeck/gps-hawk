package gps.fhv.at.gps_hawk.services;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.domain.events.NewLocationEventData;
import gps.fhv.at.gps_hawk.helper.MyLocationListener;

/**
 * Created by Tobias on 23.10.2015.
 */
public class GpsSvc implements IGpsSvc {

    private LocationManager mLocationManager;
    private Context mContext;
    private MyLocationListener mLocationListener;
    private List<Waypoint> mListWaypoints;

    public GpsSvc(LocationManager locationManager, Context context) {
        mLocationManager = locationManager;
        mContext = context;
        mListWaypoints = new ArrayList<>();
    }

    public boolean initialize() {

        mLocationListener = new MyLocationListener(mContext, this);

        if (!isGpsAvailable()) {
            return false;
        } else {
            startGpsTracking();
            return true;
        }

    }

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
        } catch (Exception ex) {
            Log.e("Debug", "Error at starting GpsTracking", ex);
        }

    }

    public void addNewLocation(Location location,NewLocationEventData data) {

        DbFacade db = DbFacade.getInstance(mContext);

        Waypoint wp = new Waypoint();
        // Double
        wp.setLat(location.getLatitude());
        wp.setLng(location.getLongitude());
        wp.setAltitude(location.getAltitude());
        // Float
        wp.setAccuracy(location.getAccuracy());
        wp.setSpeed(location.getSpeed());
        wp.setBearing(location.getBearing());
        // Text
        wp.setProvider(location.getProvider());
        // Int
        wp.setNrOfSattelites(data.getNrOfSattelites());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(location.getTime());
        wp.setTimestampCaptured(cal);

        db.saveEntity(wp);

        mListWaypoints.add(wp);
    }

    @Override
    public LocationManager getLocationManager() {
        return mLocationManager;
    }

    /**
     * Gets possible adresses from a location
     *
     * @param loc
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
    protected boolean isGpsAvailable() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return true;
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return true;
        return false;
    }


}
