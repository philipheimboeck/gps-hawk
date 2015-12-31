package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 31.12.15
 * <p/>
 * The purpose of this task is to retrieve tracks from the server, which then can be used to save waypoints on the server
 */
public class UploadWaypointsTask extends AsyncTask<Void, Void, List<Waypoint>> {

    private Context mContext;
    private IAsyncTaskCaller<Void, List<Waypoint>> mCaller;

    public UploadWaypointsTask(Context context) {
        this(null, context);
    }

    public UploadWaypointsTask(IAsyncTaskCaller<Void, List<Waypoint>> caller, Context context) {
        mCaller = caller;
        mContext = context;
    }

    @Override
    protected List<Waypoint> doInBackground(Void... params) {
        DataClient client = new DataClient(mContext);
        try {
            DbFacade facade = DbFacade.getInstance();
            List<Waypoint> waypoints = facade.getAllEntities2Export(Waypoint.class, 30, null);

            // Export all waypoints
            client.exportWaypoints(waypoints);

            // When successful mark them as exported
            facade.markExportableList(waypoints, 1, Waypoint.class);

            return waypoints;

        } catch (CommunicationException e) {
            Log.e(Constants.PREFERENCES, "Failed to upload waypoints", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Waypoint> tracks) {

        // Return the tracks
        if (mCaller != null) {
            mCaller.onPostExecute(tracks);
        }
    }
}
