package gps.fhv.at.gps_hawk.services;

import android.location.Location;

/**
 * Created by Tobias on 24.10.2015.
 */
public interface IGpsSvc {
    void startGpsTracking();
    void addNewLocation(Location location);
}
