package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.exceptions.TaskException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 05.01.16
 */
public class UploadTracksTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private IAsyncTaskCaller<Void, Void> mCaller;

    public UploadTracksTask(Context context) {
        mContext = context;
    }

    public UploadTracksTask(IAsyncTaskCaller<Void, Void> caller, Context context) {
        mContext = context;
        mCaller = caller;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DataClient client = new DataClient(mContext);
        DbFacade facade = DbFacade.getInstance();

        try {
            int junkSize = Constants.EXPORT_JUNK;

            // Mark unexported entities as "ExportNow" (= flag 2)
            int count = facade.markExportable(0, 2, Track.class);
            while (count > 0) {
                // Retrieve the tracks
                List<Track> tracks = DbFacade.getInstance().getAllEntities2Export(Track.class, junkSize, null);

                if (tracks.isEmpty()) {
                    // No result set found
                    throw new TaskException("Failed to retrieve exportable entities!");
                }

                // Send them to the server
                client.exportTracks(tracks);

                // Mark tracks as exported
                facade.markExportableList(tracks, 1, Track.class);

                // Calculate whats left
                count -= tracks.size();
            }


        } catch (CommunicationException | TaskException e) {
            Log.e(Constants.PREFERENCES, "Failed to export tracks!", e);

            // Reset not exported entities
            facade.markExportable(2, 0, Track.class);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void success) {
        // Return the tracks
        if (mCaller != null) {
            mCaller.onPostExecute(success);
        }
    }
}
