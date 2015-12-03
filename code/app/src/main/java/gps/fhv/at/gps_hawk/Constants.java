package gps.fhv.at.gps_hawk;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class Constants {
    public static final String PREFERENCES = "gps.fhv.at.gps_hawk";
    public static final String PREF_DEVICE_TOKEN = "dev_token";
    public static final int REQUEST_CODE_PERMISSION_GPS = 111;
    public static final int REQUEST_CODE_PERMISSION_STORAGE = 112;
    public static final int NOTIFICATION_TRACKING_ID = 1;
    public static final String BROADCAST_NEW_WAYPOINT = "new-waypoint";
    public static final String BROADCAST_INVALID_TOKEN = "invalid-token";
    public static final String EXTRA_WAYPOINT = "extra-waypoint";
    public static final String EXTRA_TRACK = "extra-track";
    public static final String EXTRA_FRAGMENT = "extra-fragment";
    public static final String FRAGMENT_EXPORT = "fragment-export";
    public static final String FRAGMENT_SETTINGS = "fragment-settings";

    /**
     * GPS-Constants
     */
    public static float GPS_MIN_DIST_CHANGE = 0.1F; // Minimum change in meters that needs to happen to get new coordinate
    public static int GPS_MIN_TIME = 3000; // Minimum time to report new coordinate
    public static int EXPORT_JUNK = 2500; // Amount of entities that are exported at one time

    /**
     * Acceleration
     */
    public static int ACCELERATION_MIN_TIME_GAP = 100; // in millis

    /**
     * Pferences-Keys
     */
    public static String PREF_EXPORT_URL = "export_url";

    /**
     * Motion capturing
     */
    public static final int MOTION_TO_DB_THRESHOLD = 100;

    /**
     * Setting-Names (easiest to use sequential numbers)
     */
    public static final int SETTING_GPS_MIN_DIST_CHANGE = 0;
    public static final int SETTING_GPS_MIN_TIME = 1;
    // Minimum time (in millis) to be elapsed between to MotionValues to save
    public static final int SETTING_ACCELERATION_MIN_TIME_GAP = 2;

    public static final int EXPORT_AUTOMATICALLY_GAP = 300; // seconds to be passed to start next export
}
