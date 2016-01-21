package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.workers.DbSetup;

/**
 * Author: Philip Heimböck
 * Date: 21.01.16
 */
public class ExportDatabaseTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {

        // Check if the export folder exists
        File storage = new File("/sdcard");
        File folder = new File(storage, "gpshawk");

        if(!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e(Constants.PREFERENCES, "Could not create gps-hawk folder");
                return null;
            }
        }
        File export = new File(folder, "database.db");

        // Delete the old file
        if(export.exists()) {
            export.delete();
        }

        try {
            DbSetup.exportDatabase(export.getCanonicalPath());
        } catch (IOException e) {
            Log.e(Constants.PREFERENCES, "Could not export the database", e);
        }
        return null;
    }
}
