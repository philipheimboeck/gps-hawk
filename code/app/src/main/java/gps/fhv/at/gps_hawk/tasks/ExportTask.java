package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Calendar;

import gps.fhv.at.gps_hawk.communication.ExportClient;
import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.Waypoint;

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

        // Todo: Test only
        ArrayList<Waypoint> wpList = new ArrayList<>();
        Waypoint w = new Waypoint();
        w.setId(1);
        w.setTimestampCaptured(Calendar.getInstance());
        wpList.add(w);
        mExpContext.setWaypointList(wpList);

        ExportClient client = new ExportClient(mExpContext.getContext());
        client.exportCollectedWaypoints(mExpContext);

        return "";
    }
}
