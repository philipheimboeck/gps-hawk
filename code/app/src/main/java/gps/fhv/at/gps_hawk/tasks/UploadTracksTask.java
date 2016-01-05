package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 05.01.16
 */
public class UploadTracksTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    public UploadTracksTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DataClient client = new DataClient(mContext);

        // First retrieve the tracks
        List<Track> tracks = DbFacade.getInstance().getAllEntities2Export(Track.class, 0, null);

        // Send them to the server
        try {
            client.exportTracks(tracks);

            // Mark tracks as exported
            DbFacade.getInstance().markExportableList(tracks, 1, Track.class);

        } catch (CommunicationException e) {
            e.printStackTrace();
        }



        return null;
    }
}
