package gps.fhv.at.gps_hawk.workers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.persistence.setup.BaseTableDef;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;
import gps.fhv.at.gps_hawk.persistence.setup.MotionValuesDef;
import gps.fhv.at.gps_hawk.persistence.setup.TrackDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

/**
 * Created by Tobias on 24.10.2015.
 * This class is for creating the database!
 */
public class DbSetup extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 18;
    public static final String DATABASE_NAME = "GpwHawk.db";

    public DbSetup(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private List<BaseTableDef> getTableDefs() {

        List<BaseTableDef> tableDefs = new ArrayList<>();

        // Add Table-Def for each table
        tableDefs.add(new WaypointDef());
        tableDefs.add(new TrackDef());
        tableDefs.add(new Exception2LogDef());
        tableDefs.add(new MotionValuesDef());

        return tableDefs;
    }

    public void onCreate(SQLiteDatabase db) {

        List<BaseTableDef> tableDefs = getTableDefs();

        for (BaseTableDef tdbDef : tableDefs) {
            try {
                db.execSQL(tdbDef.getSqlCreateTable());
            } catch (Exception e) {
                Log.e(Constants.PREFERENCES, "Error in DbSetup.onCreate()", e);
            }
        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        List<BaseTableDef> tableDefs = getTableDefs();

        // Upgrade all tables
        for (BaseTableDef tdbDef : tableDefs) {
            try {
                String updScript = tdbDef.getUpdateScript(oldVersion);
                if (updScript != null && !updScript.isEmpty())
                    db.execSQL(updScript);

            } catch (Exception e) {
                Log.e(Constants.PREFERENCES, "Error in DbSetup.onUpgrade()", e);
            }
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            onUpgrade(db, oldVersion, newVersion);
        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Error onDowngrade()", e);
        }
    }

}
