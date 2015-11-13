package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 30.10.2015.
 */
public class TrackDef extends BaseTableDef {

    public static final String TABLE_NAME = "track";

    public static final String COLUMN_NAME_DATETIME_START = "dateTimeStart";
    public static final String COLUMN_NAME_DATETIME_END = "dateTimeEnd";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getSqlCreateColumns() {
        return
                COLUMN_NAME_DATETIME_START + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_DATETIME_END + TYPE_INT
                ;
    }

    @Override
    public String getUpdateScript() {
        return null;
    }
}
