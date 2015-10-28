package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 24.10.2015.
 */
public class WaypointDef extends BaseTableDef {

    public WaypointDef() {
    }

    public static final String TABLE_NAME = "waypoint";
    public static final String COLUMN_NAME_NR_OF_SATTELITES = "nrOfSattelites";
    public static final String COLUMN_NAME_POSITION_ID = "position_id";
    public static final String COLUMN_NAME_DATETIME = "date_gps";
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_BEARING = "bearing";
    public static final String COLUMN_PROVIDER = "provider";
    public static final String COLUMN_SPEED = "speed";

    @Override
    public String getSqlCreateColumns() {
        return COLUMN_NAME_NR_OF_SATTELITES + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_POSITION_ID + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_DATETIME + TYPE_DATETIME + COMMA_SEP +
                COLUMN_ACCURACY + TYPE_FLOAT + COMMA_SEP +
                COLUMN_BEARING + TYPE_FLOAT + COMMA_SEP +
                COLUMN_PROVIDER + TYPE_TEXT + COMMA_SEP +
                COLUMN_SPEED + TYPE_FLOAT
                ;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

}
