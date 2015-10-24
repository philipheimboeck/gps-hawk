package gps.fhv.at.gps_hawk.persistence.setup;

import android.provider.BaseColumns;

/**
 * Created by Tobias on 24.10.2015.
 */
public abstract class BaseTableDef implements BaseColumns {

    protected static final String TYPE_TEXT = " TEXT";
    protected static final String TYPE_INT = " TEXT";
    protected static final String TYPE_DATETIME = " DATETIME";
    protected static final String COMMA_SEP = ",";

    /**
     * gets the table name
     * @return
     */
    protected abstract String getTableName();

    /**
     * Name of the PK-Column
     * @return
     */
    protected abstract String getColumnId();

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
                _ID + " INTEGER PRIMARY KEY," +
                getSqlCreateColumns() +
                " )";
    }

    public  String getSqlDeleteEntries() {
        return "DROP TABLE IF EXISTS " + getTableName();
    }

}
