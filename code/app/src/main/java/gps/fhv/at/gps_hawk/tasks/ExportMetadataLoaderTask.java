package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.activities.fragments.ExportFragment;
import gps.fhv.at.gps_hawk.domain.Exception2Log;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;
import gps.fhv.at.gps_hawk.persistence.setup.MotionValuesDef;
import gps.fhv.at.gps_hawk.persistence.setup.TrackDef;
import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Created by Tobias on 20.12.2015.
 */
public class ExportMetadataLoaderTask extends AsyncTask<Void, Void, int[]> {

    private ExportFragment mView;
    private IAsyncTaskCaller<Void, int[]> mCaller;

    public ExportMetadataLoaderTask(ExportFragment mView, final IAsyncTaskCaller<Void, int[]> caller) {
        this.mView = mView;
        this.mCaller = caller;
    }

    protected void onPreExecute() {
        mCaller.onPreExecute();
    }

    protected void onPostExecute(final int[] result) {
        mCaller.onPostExecute(result);
    }


    @Override
    protected int[] doInBackground(Void... params) {

        // TODO: remove before deployment
        // And also remove entries in server-db between 08:42 and ... on 13.11.2015 from my deviceid!
        // And also remove entries in server-db between 08:00 and ... on 28.11.2015 from my deviceid!
        DbFacade db = DbFacade.getInstance(mView.getContext());
        db.markExportable(2, 0, Waypoint.class);
        db.markExportable(2, 0, Exception2Log.class);
        db.markExportable(2, 0, MotionValues.class);
        db.markExportable(2, 0, Track.class);
//        db.markExportable(0, 1, Waypoint.class);

        int[] amounts = new int[8];

        try {

            int i = -1;

            // Waypoints
            amounts[++i] = db.getCount(WaypointDef.TABLE_NAME, null);
            amounts[++i] = db.getCount(WaypointDef.TABLE_NAME, WaypointDef.COLUMN_NAME_IS_EXPORTED + " = 0");

            // Exceptions
            amounts[++i] = db.getCount(Exception2LogDef.TABLE_NAME, null);
            amounts[++i] = db.getCount(Exception2LogDef.TABLE_NAME, Exception2LogDef.COLUMN_NAME_IS_EXPORTED + " = 0");

            // Motions
            amounts[++i] = db.getCount(MotionValuesDef.TABLE_NAME, null);
            amounts[++i] = db.getCount(MotionValuesDef.TABLE_NAME, MotionValuesDef.COLUMN_NAME_IS_EXPORTED + " = 0");

            // Tracks
            amounts[++i] = db.getCount(TrackDef.TABLE_NAME, null);
            amounts[++i] = db.getCount(TrackDef.TABLE_NAME, TrackDef.COLUMN_NAME_IS_EXPORTED + " = 0");

        } catch (Exception e) {
            Log.e(Constants.PREFERENCES, "Cannot determine all amounts of data", e);
        }

        return amounts;
    }
}
