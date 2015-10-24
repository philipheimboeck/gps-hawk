package gps.fhv.at.gps_hawk.persistence;

import android.provider.BaseColumns;

/**
 * Created by Tobias on 24.10.2015.
 */
public final class WaypointContract {

    public WaypointContract() {
    }

    public static  abstract  class WaypointEntry implements BaseColumns {
        public static final String TABLE_NAME = "waypoint";
        public static final String COLUMN_NAME_WAYPOINT_ID = "waypoint_id";
        public static final String COLUMN_NAME_NR_OF_SATTELITES = "nrOfSattelites";
        public static final String COLUMN_NAME_POSITION_ID = "position_id";
        public static final String COLUMN_NAME_DATETIME = "date_gps";

    }
}
