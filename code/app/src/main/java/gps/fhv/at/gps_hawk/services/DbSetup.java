package gps.fhv.at.gps_hawk.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import gps.fhv.at.gps_hawk.helper.DbSetupHelper;

/**
 * Created by Tobias on 24.10.2015.
 */
public class DbSetup extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GpwHawk.db";

    public DbSetup(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DbSetupHelper.SQL_CREATE_ENTRIES);
        } catch (Exception e) {
            Log.e("FATAL","Error onCreate()",e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(DbSetupHelper.SQL_DELETE_ENTRIES);
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
