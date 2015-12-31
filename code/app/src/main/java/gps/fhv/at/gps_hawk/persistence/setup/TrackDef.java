package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 30.10.2015.
 */
public class TrackDef extends BaseTableDef {

    public static final String TABLE_NAME = "track";

    public static final String COLUMN_NAME_DATETIME_START = "dateTimeStart";
    public static final String COLUMN_NAME_DATETIME_END = "dateTimeEnd";
    public static final String COLUMN_NAME_IS_VALID = "isValid";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getSqlCreateColumns() {
        return
                COLUMN_NAME_DATETIME_START + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_DATETIME_END + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_IS_VALID + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_IS_EXPORTED + TYPE_INT
                ;
    }

    @Override
    public String getUpdateScript(int oldVersion) {
        StringBuilder sb = new StringBuilder();

        if (oldVersion <= 13) {
            sb.append("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_IS_VALID + " INT AFTER dateTimeEnd;");
        }

        if (oldVersion <= 14) {
            sb.append("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME_IS_EXPORTED + " INT AFTER " + COLUMN_NAME_IS_VALID + ";");
        }

        return sb.toString();
    }
}
