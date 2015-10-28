package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 25.10.2015.
 */
public class PositionDef extends BaseTableDef {

    public static final String TABLE_NAME = "position";
    public static final String COLUMN_NAME_LAT = "lat";
    public static final String COLUMN_NAME_LNG = "lng";
    public static final String COLUMN_ALTITUDE = "alt";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getSqlCreateColumns() {
        return COLUMN_NAME_LAT + TYPE_DOUBLE + COMMA_SEP +
                COLUMN_NAME_LNG + TYPE_DOUBLE + COMMA_SEP +
                COLUMN_ALTITUDE + TYPE_DOUBLE;
    }
}
