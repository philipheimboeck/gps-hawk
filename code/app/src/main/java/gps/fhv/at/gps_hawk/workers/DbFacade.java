package gps.fhv.at.gps_hawk.workers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.domain.IExportable;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.persistence.broker.BrokerBase;
import gps.fhv.at.gps_hawk.persistence.broker.Exception2LogBroker;
import gps.fhv.at.gps_hawk.persistence.broker.MotionValuesBroker;
import gps.fhv.at.gps_hawk.persistence.broker.TrackBroker;
import gps.fhv.at.gps_hawk.persistence.broker.WaypointBroker;
import gps.fhv.at.gps_hawk.persistence.setup.BaseTableDef;
import gps.fhv.at.gps_hawk.persistence.setup.TrackDef;

/**
 * Created by Tobias on 25.10.2015.
 */
public class DbFacade {

    private static HashMap<Type, BrokerBase> mBrokerMap = new HashMap<>();
    private static DbFacade mInstance;
    private DbSetup mDbHelper;

    static {
        mBrokerMap.put(Waypoint.class, new WaypointBroker());
        mBrokerMap.put(Track.class, new TrackBroker());
        mBrokerMap.put(Exception2Log.class, new Exception2LogBroker());
        mBrokerMap.put(MotionValues.class, new MotionValuesBroker());
    }

    public static DbFacade getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbFacade(context);
        }
        return mInstance;
    }

    public static DbFacade getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("DbFacade hasn't been initialized - First call needs context");
        }
        return mInstance;
    }

    private DbFacade(Context context) {
        mDbHelper = new DbSetup(context);
    }

    private SQLiteDatabase getDb() {
        return mDbHelper.getWritableDatabase();
    }

    public <T extends DomainBase> long saveEntity(T entity) {
        BrokerBase broker = mBrokerMap.get(entity.getClass());
        ContentValues insertValues = broker.map2db(entity);

        long newRowId;

        // If has id --> update
        if (entity.getId() > 0) {
            String[] selectionArgs = {String.valueOf(entity.getId())};
            newRowId = getDb().update(broker.getTblName(), insertValues, BaseTableDef._ID + " = ?", selectionArgs);
        } else {
            // else --> insert
            newRowId = getDb().insert(broker.getTblName(), null, insertValues);
        }
        return newRowId;
    }

    public void emptyTable(String tableName) {
        String sql = "DELETE FROM " + tableName + ";";
        try {
            getDb().execSQL(sql);
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error in emptyTable(tableName)", e);
        }

    }

    /**
     * Saves array of objects to database using bulk insert
     *
     * @param arr
     * @param length of array to save
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T extends DomainBase> int saveEntities(T[] arr, int length) throws SQLException {

        int numInserted = 0;

        if (arr.length <= 0) return numInserted;

        BrokerBase broker = mBrokerMap.get(arr[0].getClass());

        SQLiteDatabase db = getDb();
        db.beginTransaction();

        // Create SQL-Statement
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(broker.getTblName()).append("(");
        String separator = "";
        for (String colName : broker.getColumns()) {
            sql.append(separator).append(" ").append(colName);
            separator = ",";
        }
        sql.append(") VALUES ");

        try {
            // Provide values as String foreach column: `( 'val1' , ... , 'valn' )`
            String outerSeparator = "";
            for (int i = 0; i < length; i++) {
                ContentValues cv = broker.map2db(arr[i]);
                sql.append(outerSeparator).append("(");
                separator = "";
                for (String colName : broker.getColumns()) {
                    sql.append(separator).append("'").append(cv.getAsString(colName)).append("'");
                    separator = ",";
                }
                sql.append(")");
                outerSeparator = ", ";
            }

            db.execSQL(sql.toString());
            db.setTransactionSuccessful();
            numInserted = arr.length;
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, sql.toString());
            throw e;
        } finally {
            db.endTransaction();
        }

        return numInserted;
    }

    /**
     * ## From here - start type-sepcific SELECT-queries ##
     */
    public <T extends DomainBase & IExportable> List<T> getAllEntities2Export(Class<T> t, int limit, String where) {

        List<T> listRet = new ArrayList<>();
        Cursor c = null;
        try {

            BrokerBase broker = mBrokerMap.get(t);

            // How you want the results sorted in the resulting Cursor
            String sortOrder = BaseTableDef._ID + " ASC LIMIT " + limit;

            c = getDb().query(
                    broker.getTblName(),  // The table to query
                    null,                               // The columns to return - simply all
                    BaseTableDef.COLUMN_NAME_IS_EXPORTED + " = 2" + (where != null ? " AND " + where : ""),                                // The columns for the WHERE clause
                    null,                              // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            c.moveToFirst();

            Log.v(Constants.PREFERENCES, "Query found " + c.getCount() + " entities to export - Start mapping to domain");

            int i = 0;
            while (i < c.getCount()) {
                T entity = broker.map2domain(c);
                listRet.add(entity);
                c.moveToNext();
                ++i;

            }
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error getting exportable data", e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return listRet;
    }

    /**
     * Masks Waypoints, that hasn't yet been exported, with `isExportet=updValIsExport`
     *
     * @return
     */
    public int markExportable(int whereIsExport, int updValIsExport, Type t) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        BrokerBase broker = mBrokerMap.get(t);

        ContentValues values = new ContentValues();

        values.put(BaseTableDef.COLUMN_NAME_IS_EXPORTED, updValIsExport);

        // Which row to update, based on the ID
        String selection = BaseTableDef.COLUMN_NAME_IS_EXPORTED + " = ?";

        // If unexported are desired, include those that are NULL
        if (whereIsExport == 0)
            selection += " OR " + BaseTableDef.COLUMN_NAME_IS_EXPORTED + " IS NULL ";

        String[] selectionArgs = {String.valueOf(whereIsExport)};

        int count = db.update(
                broker.getTblName(),
                values,
                selection,
                selectionArgs);

        return count;

    }

    /**
     * Updated for all entities in list their `isExported` column to the desired value
     *
     * @param list         of IExportable entities
     * @param updValExport value to set in column `isExported`
     * @param t            specific type of IExportable
     * @return -1 in case of error, 0 in case of success
     */
    public <T extends DomainBase> int markExportableList(List<T> list, int updValExport, Type t) {

        if (list.size() <= 0) return -1;

        if (!IExportable.class.isAssignableFrom(list.get(0).getClass()))
            throw new InvalidParameterException("Entity must be of type IExportable");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        BrokerBase broker = mBrokerMap.get(t);
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE ").append(broker.getTblName()).append(" SET ").append(BaseTableDef.COLUMN_NAME_IS_EXPORTED)
                .append(" = ").append(updValExport).append(" WHERE ").append(BaseTableDef._ID).append(" IN (");
        char comma = ' ';
        for (DomainBase exportable : list) {
            sql.append(comma).append(exportable.getId());
            comma = ',';
        }
        sql.append(")");

        try {
            db.execSQL(sql.toString());
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error in DbSetup.onCreate()", e);
            return -1;
        }


        return 0;
    }

    public <T extends DomainBase> T select(int id, Class<T> cl) {

        T domain = null;

        Cursor c = null;
        try {
            BrokerBase broker = mBrokerMap.get(cl);

            String where = BaseColumns._ID + " = " + id;

            c = getDb().query(
                    broker.getTblName(),  // The table to query
                    null,
//                new String[]{" * "},                               // The columns to return
                    where,                                // The columns for the WHERE clause
                    null,                              // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            c.moveToFirst();

            domain = broker.map2domain(c);
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, e.getMessage(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }


        return domain;
    }

    public <T extends DomainBase> List<T> selectWhere(String condition, Class<T> cl) {
        BrokerBase broker = mBrokerMap.get(cl);

        ArrayList<T> list = new ArrayList<>();
        Cursor c = null;
        try {
            c = getDb().query(
                    broker.getTblName(), // Table to query
                    null,
                    condition,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            c.moveToFirst();


            for (int i = 0; i < c.getCount(); i++) {
                T domain = broker.map2domain(c);
                list.add(domain);

                c.moveToNext();
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    public <T extends DomainBase> T selectOneWhere(String condition, Class<T> cl) {
        BrokerBase broker = mBrokerMap.get(cl);

        T domain;
        Cursor c = null;
        try {
            c = getDb().query(
                    broker.getTblName(), // Table to query
                    null,
                    condition,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            c.moveToFirst();
            domain = broker.map2domain(c);

        } finally {
            if (c != null) {
                c.close();
            }
        }

        return domain;
    }

    public int getCount(String tbl, String where) {
        int ret = -1;
        Cursor c = null;
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    "COUNT(*)"
            };

            c = getDb().query(
                    tbl,  // The table to query
                    projection,                               // The columns to return
                    where,                                // The columns for the WHERE clause
                    null,                              // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            c.moveToFirst();

            ret = c.getInt(0);

        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error fetching COUNT(*) FROM table" + tbl, e);
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return ret;
    }

    /**
     * Updates single entity base on its id
     *
     * @param domain
     * @param <T>
     * @return amount of results updated (should be 1)
     */
    public <T extends DomainBase> int update(T domain) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        BrokerBase broker = mBrokerMap.get(domain.getClass());
        ContentValues values = broker.map2db(domain);

        // Which row to update, based on the ID
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(domain.getId())};

        int count = db.update(
                broker.getTblName(),
                values,
                selection,
                selectionArgs);

        return count;
    }

    public Track findReservedTrack() {
        return DbFacade.getInstance().selectOneWhere(TrackDef.COLUMN_NAME_EXTERNAL_ID + "IS NOT NULL AND " + TrackDef.COLUMN_NAME_DATETIME_START + " IS NULL", Track.class);
    }

    public int countReservedTracks() {
        return DbFacade.getInstance().getCount(TrackDef.TABLE_NAME, TrackDef.COLUMN_NAME_EXTERNAL_ID + "IS NOT NULL AND " + TrackDef.COLUMN_NAME_DATETIME_START + " IS NULL");
    }

}
