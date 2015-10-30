package gps.fhv.at.gps_hawk.workers;

import android.location.Location;
import android.location.LocationManager;

import gps.fhv.at.gps_hawk.domain.events.NewLocationEventData;

/**
 * Created by Tobias on 24.10.2015.
 */
public interface IGpsSvc {
    boolean isGpsAvailable();
    void startGpsTracking();
    void stopGpsTracking();
    void addNewLocation(Location location,NewLocationEventData data);
    LocationManager getLocationManager();
}

