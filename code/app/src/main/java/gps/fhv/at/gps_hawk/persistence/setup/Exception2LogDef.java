package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 02.11.2015.
 */
public class Exception2LogDef extends BaseTableDef {

    public static final String TABLE_NAME = "exceptions";

    public static final String COLUMN_NAME_DATETIME = "dateTime";
    public static final String COLUMN_NAME_STACK_TRACE = "stackTrace";
    public static final String COLUMN_NAME_MSG = "msg";
    public static final String COLUMN_NAME_LEVEL = "level";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getSqlCreateColumns() {
        return
                COLUMN_NAME_DATETIME + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_STACK_TRACE + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_MSG + TYPE_TEXT + COMMA_SEP +
                        COLUMN_NAME_LEVEL + TYPE_INT + COMMA_SEP +
                        COLUMN_NAME_IS_EXPORTED + TYPE_TINYINT
                ;

    }

    @Override
    public String getUpdateScript(int oldVersion) {
        StringBuilder sb = new StringBuilder();

        if (oldVersion <= 17) {
            sb.append("DELETE FROM " + TABLE_NAME + ";");
        }

        return sb.toString();
    }
}
