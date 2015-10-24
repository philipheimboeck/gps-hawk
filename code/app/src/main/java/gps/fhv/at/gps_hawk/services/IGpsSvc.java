package gps.fhv.at.gps_hawk.services;

import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Tobias on 24.10.2015.
 */
public interface IGpsSvc {
    void startGpsTracking();
    void addNewLocation(Location location);
    LocationManager getLocationManager();
}
