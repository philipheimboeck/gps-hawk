package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;
import android.provider.Settings;

import gps.fhv.at.gps_hawk.communication.ExportClient;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportTask extends AsyncTask<Void, Void, String> {

    private ExportContext mExpContext;

    public ExportTask(ExportContext expContext, final IAsyncTaskCaller<Void, Boolean> caller) {
        mExpContext = expContext;
    }

    @Override
    protected String doInBackground(Void... params) {

        DbFacade dbFacade = DbFacade.getInstance(mExpContext.getContext());

        // Mark unexported Waypoints as "ExportNow"
        int count = dbFacade.markWaypoints(0,2);

        if ( count > 0 ) {

            // Get all Waypoints from DB to export
            mExpContext.setWaypointList(dbFacade.getAllWaypoints2Update());

            // Send via Web
            ExportClient client = new ExportClient(mExpContext.getContext());
            client.exportCollectedWaypoints(mExpContext);

            // Mark "ExportNow" Waypoints as "Exported"
            dbFacade.markWaypoints(2, 1);
        }

        return "";
    }
}
