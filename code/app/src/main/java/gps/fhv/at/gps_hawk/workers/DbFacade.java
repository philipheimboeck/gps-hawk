package gps.fhv.at.gps_hawk.workers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gps.fhv.at.gps_hawk.domain.DomainBase;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.persistence.broker.BrokerBase;
import gps.fhv.at.gps_hawk.persistence.broker.TrackBroker;
import gps.fhv.at.gps_hawk.persistence.broker.WaypointBroker;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

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
        long newRowId = getDb().insert(broker.getTblName(), null, insertValues);
        return newRowId;
    }


    /**
     * ## From here - start type-sepcific SELECT-queries ##
     */
    public List<Waypoint> getAllWaypoints2Update() {

        List<Waypoint> listRet = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                // Int
                WaypointDef._ID,
                WaypointDef.COLUMN_NAME_NR_OF_SATTELITES,
                // Datetime
                WaypointDef.COLUMN_NAME_DATETIME,
                // Double
                WaypointDef.COLUMN_NAME_LAT,
                WaypointDef.COLUMN_NAME_LNG,
                WaypointDef.COLUMN_NAME_ALTITUDE,
                // Float
                WaypointDef.COLUMN_SPEED,
                WaypointDef.COLUMN_ACCURACY,
                WaypointDef.COLUMN_BEARING,
                // Text
                WaypointDef.COLUMN_PROVIDER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = WaypointDef.COLUMN_NAME_DATETIME + " ASC";

        Cursor c = getDb().query(
                WaypointDef.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                WaypointDef.COLUMN_NAME_IS_EXPORTED + " = 2",                                // The columns for the WHERE clause
                null,                              // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        BrokerBase broker = mBrokerMap.get(Waypoint.class);

        int i = 0;
        while (i < c.getCount()) {

            Waypoint wp = broker.map2domain(c);
            listRet.add(wp);
            c.moveToNext();
            ++i;

        }
        return listRet;
    }

    /**
     * Masks Waypoints, that hasn't yet been exported, with `isExportet=2`
     * @return
     */
    public int markWaypoints(int whereIsExport, int updValIsExport) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        BrokerBase broker = mBrokerMap.get(Waypoint.class);

        ContentValues values = new ContentValues();

        values.put(WaypointDef.COLUMN_NAME_IS_EXPORTED, updValIsExport);

        // Which row to update, based on the ID
        String selection = WaypointDef.COLUMN_NAME_IS_EXPORTED + " = ?";
        String[] selectionArgs = { String.valueOf(whereIsExport) };


        int count = db.update(
                broker.getTblName(),
                values,
                selection,
                null);

        return count;

    }

    public int getCount(String tbl, String where) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "COUNT(*)"
        };

        Cursor c = getDb().query(
                tbl,  // The table to query
                projection,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                              // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();

        int ret = c.getInt(0);

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

}
