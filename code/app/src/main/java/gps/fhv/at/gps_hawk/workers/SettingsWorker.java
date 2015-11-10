package gps.fhv.at.gps_hawk.workers;

import gps.fhv.at.gps_hawk.Constants;

/**
 * The aim of this class is to merge Settings from multiple sources (i.e. Constants, Webservice, LocalSettings)
 * Created by Tobias on 10.11.2015.
 */
public class SettingsWorker {

    private static SettingsWorker mInstance;

    public  static  SettingsWorker getmInstance() {
        if ( mInstance == null ) {
            mInstance = new SettingsWorker();
        }
        return mInstance;
    }

    private SettingsWorker() {}

    /**
     * Once (at startup), make an uptdate from server-side settings
     */
    public void initialize() {
        // TODO: implement code here
    }

    public Object getSetting(int key) {
        switch (key) {
            case Constants.SETTING_GPS_MIN_DIST_CHANGE:
                return Constants.GPS_MIN_DIST_CHANGE;
        }
        return null;
    }

}
