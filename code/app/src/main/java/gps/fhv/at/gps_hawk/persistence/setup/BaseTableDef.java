package gps.fhv.at.gps_hawk.persistence.setup;

import android.provider.BaseColumns;

/**
 * Created by Tobias on 24.10.2015.
 */
public abstract class BaseTableDef implements BaseColumns {

    protected static final String TYPE_TEXT = " TEXT";
    protected static final String TYPE_INT = " INTEGER";
    protected static final String TYPE_TINYINT = " TINYINT";
    protected static final String TYPE_DATETIME = " DATETIME";
    protected static final String TYPE_FLOAT = " FLOAT";
    protected static final String TYPE_DOUBLE = " DOUBLE";
    protected static final String TYPE_LONG = " BIGINT";
    protected static final String COMMA_SEP = ",";

    public static final String COLUMN_NAME_IS_EXPORTED = "isExported";

    /**
     * gets the table name
     * @return
     */
    protected abstract String getTableName();

    /**
     * Gets the sql-statements for creating the columns
     *
     * Example:
     * 	`vorname` TEXT ,
     *  `familienname` TEXT ,
     *  `email` TEXT
     *
     * @return
     */
    protected abstract String getSqlCreateColumns();

    public String getSqlCreateTable() {
        return "CREATE TABLE " + getTableName() + " (" +
                _ID + " INTEGER PRIMARY KEY NOT NULL," +
                getSqlCreateColumns() +
                " )";
    }

    public  String getSqlDeleteEntries() {
        return "DROP TABLE IF EXISTS " + getTableName();
    }

    /**
     * Returns update-Script for the corresponding table
     * @return Updatescript or null, if not provided
     */
    public abstract String getUpdateScript(int oldVersion);

}
