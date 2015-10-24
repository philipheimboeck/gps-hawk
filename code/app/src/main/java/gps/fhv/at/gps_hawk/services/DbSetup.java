package gps.fhv.at.gps_hawk.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gps.fhv.at.gps_hawk.persistence.setup.BaseTableDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

/**
 * Created by Tobias on 24.10.2015.
 * This class is for creating the database!
 */
public class DbSetup extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GpwHawk.db";

    public DbSetup(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private List<BaseTableDef> getTableDefs() {
        // Add Table-Def for each table
        List<BaseTableDef> tableDefs = new ArrayList<>();
        tableDefs.add(new WaypointDef());
        return  tableDefs;
    }

    public void onCreate(SQLiteDatabase db) {

        List<BaseTableDef> tableDefs = getTableDefs();

        // Concatenate als CREATE TABLE statements
        StringBuilder sb = new StringBuilder();
        for( BaseTableDef tdbDef : tableDefs ) {
            sb.append(tdbDef.getSqlCreateTable()).append(";");
        }

        try {
            db.execSQL(sb.toString());
        } catch (Exception e) {
            Log.e("FATAL","Error onCreate()",e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        List<BaseTableDef> tableDefs = getTableDefs();

        // Concatenate als CREATE TABLE statements
        StringBuilder sb = new StringBuilder();
        for( BaseTableDef tdbDef : tableDefs ) {
            sb.append(tdbDef.getSqlDeleteEntries()).append(";");
        }

        try {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(sb.toString());
            onCreate(db);
        } catch (Exception e) {
            Log.e("FATAL","Error onUpgrade()",e);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            onUpgrade(db, oldVersion, newVersion);
        } catch (Exception e) {
            Log.e("FATAL","Error onDowngrade()",e);
        }
    }

}
