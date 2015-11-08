package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 24.10.2015.
 */
public class WaypointDef extends BaseTableDef {

    public WaypointDef() {
    }

    public static final String TABLE_NAME = "waypoint";

    // Int types
    public static final String COLUMN_NAME_NR_OF_SATTELITES = "nrOfSattelites";
    public static final String COLUMN_NAME_TRACK_ID = "track_id";
    public static final String COLUMN_NAME_VEHICLE_ID = "vehicleId";

    // Datetime types
    public static final String COLUMN_NAME_DATETIME = "date_gps";

    // Text types
    public static final String COLUMN_PROVIDER = "provider";

    // Float types
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_BEARING = "bearing";
    public static final String COLUMN_SPEED = "speed";

    // Double types
    public static final String COLUMN_NAME_LAT = "lat";
    public static final String COLUMN_NAME_LNG = "lng";
    public static final String COLUMN_NAME_ALTITUDE = "alt";

    @Override
    public String getSqlCreateColumns() {
        return
                // Int types
                COLUMN_NAME_NR_OF_SATTELITES + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_TRACK_ID + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_IS_EXPORTED + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_VEHICLE_ID + TYPE_INT + COMMA_SEP +

                        // Datetime types
                        COLUMN_NAME_DATETIME + TYPE_DATETIME + COMMA_SEP +

                        // Float types
                        COLUMN_ACCURACY + TYPE_FLOAT + COMMA_SEP +
                        COLUMN_BEARING + TYPE_FLOAT + COMMA_SEP +
                        COLUMN_SPEED + TYPE_FLOAT + COMMA_SEP +

                        // Text types
                        COLUMN_PROVIDER + TYPE_TEXT + COMMA_SEP +

                        // Dobule types
                        COLUMN_NAME_LAT + TYPE_DOUBLE + COMMA_SEP +
                        COLUMN_NAME_LNG + TYPE_DOUBLE + COMMA_SEP +
                        COLUMN_NAME_ALTITUDE + TYPE_DOUBLE;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

}
