package gps.fhv.at.gps_hawk.workers;


import gps.fhv.at.gps_hawk.domain.Track;

/**
 * Created by Tobias on 24.10.2015.
 */
public interface IGpsWorker {
    boolean isGpsAvailable();

    /**
     * Starting Gps-Tracking is always associated with a Track
     * @param t
     */
    void startGpsTracking(Track t);


    void stopGpsTracking();
}

