package gps.fhv.at.gps_hawk.helper;

import gps.fhv.at.gps_hawk.persistence.WaypointContract;

/**
 * Created by Tobias on 24.10.2015.
 */
public class DbSetupHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INT = " TEXT";
    private static final String TYPE_DATETIME = " DATETIME";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WaypointContract.WaypointEntry.TABLE_NAME + " (" +
                    WaypointContract.WaypointEntry._ID + " INTEGER PRIMARY KEY," +
                    WaypointContract.WaypointEntry.COLUMN_NAME_WAYPOINT_ID + TYPE_TEXT + COMMA_SEP +
                    WaypointContract.WaypointEntry.COLUMN_NAME_NR_OF_SATTELITES + TYPE_INT + COMMA_SEP +
                    WaypointContract.WaypointEntry.COLUMN_NAME_POSITION_ID + TYPE_INT + COMMA_SEP +
                    WaypointContract.WaypointEntry.COLUMN_NAME_DATETIME + TYPE_DATETIME +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WaypointContract.WaypointEntry.TABLE_NAME;
}
