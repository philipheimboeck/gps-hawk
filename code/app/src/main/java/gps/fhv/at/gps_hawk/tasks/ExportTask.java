package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;
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

        // Get all Waypoints from DB
        DbFacade dbFacade = DbFacade.getInstance(mExpContext.getContext());
        mExpContext.setWaypointList(dbFacade.getAllWaypoints());

        // Send via Web
        ExportClient client = new ExportClient(mExpContext.getContext());
        client.exportCollectedWaypoints(mExpContext);

        return "";
    }
}
