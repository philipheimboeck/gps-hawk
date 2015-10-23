package gps.fhv.at.gps_hawk.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by Tobias on 23.10.2015.
 */
public class GpsSvc {

    private LocationManager mLocationManager;

    public GpsSvc(LocationManager locationManager) {
        mLocationManager = locationManager;
    }

    public void initialize() {

        LocationListener locationListener = new MyLocationListener();


        //noinspection ResourceType
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }
}
