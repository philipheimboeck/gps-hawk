package gps.fhv.at.gps_hawk.persistence.setup;

/**
 * Created by Tobias on 08.11.2015.
 */
public class MotionValuesDef extends BaseTableDef {

    public static final String TABLE_NAME = "motionValues";

    public static final String COLUMN_NAME_X = "x";
    public static final String COLUMN_NAME_Y = "y";
    public static final String COLUMN_NAME_Z = "z";
    public static final String COLUMN_NAME_MOTION_TYPE = "motionType";

    public static final String COLUMN_NAME_DATETIME_CAPTURED = "dateCaptured";

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getSqlCreateColumns() {
        return COLUMN_NAME_X + TYPE_FLOAT + COMMA_SEP +
                COLUMN_NAME_Y + TYPE_FLOAT + COMMA_SEP +
                COLUMN_NAME_Z + TYPE_FLOAT + COMMA_SEP +
                COLUMN_NAME_MOTION_TYPE + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_IS_EXPORTED + TYPE_INT + COMMA_SEP +
                COLUMN_NAME_DATETIME_CAPTURED + TYPE_LONG
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
