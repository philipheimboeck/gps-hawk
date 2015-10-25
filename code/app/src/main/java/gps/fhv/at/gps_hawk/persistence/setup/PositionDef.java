package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 25.10.2015.
 */
public class PositionDef extends BaseTableDef {

    public static final String TABLE_NAME = "position";
    public static final String COLUMN_NAME_LAT = "lat";
    public static final String COLUMN_NAME_LNG = "lng";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getSqlCreateColumns() {
        return COLUMN_NAME_LAT + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_LNG + TYPE_INT;
    }
}
