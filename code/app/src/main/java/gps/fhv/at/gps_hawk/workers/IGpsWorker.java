package gps.fhv.at.gps_hawk.workers;


/**
 * Created by Tobias on 24.10.2015.
 */
public interface IGpsWorker {
    boolean isGpsAvailable();
    void startGpsTracking();
    void stopGpsTracking();
}

