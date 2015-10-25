package gps.fhv.at.gps_hawk;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class Constants {
    public static final String PREFERENCES = "gps.fhv.at.gps_hawk";
    public static final String PREF_DEVICE_TOKEN = "dev_token";
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 111;

    /**
     * GPS-Constants
     */
    public static float MIN_DIST_CHANGE = 0.1F; // Minimum change in meters that needs to happen to get new coordinate
    public static int MIN_TIME = 5000; // Minimum time to report new coordinate
}
