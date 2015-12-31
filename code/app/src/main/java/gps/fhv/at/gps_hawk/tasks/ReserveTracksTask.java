package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.DataClient;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.workers.DbFacade;

/**
 * Author: Philip Heimböck
 * Date: 31.12.15
 * <p/>
 * The purpose of this task is to retrieve tracks from the server, which then can be used to save waypoints on the server
 */
public class ReserveTracksTask extends AsyncTask<Void, Void, List<Track>> {

    private Context mContext;
    private IAsyncTaskCaller<Void, List<Track>> mCaller;

    public ReserveTracksTask(Context context) {
        this(null, context);
    }

    public ReserveTracksTask(IAsyncTaskCaller<Void, List<Track>> caller, Context context) {
        mCaller = caller;
        mContext = context;
    }

    @Override
    protected List<Track> doInBackground(Void... params) {
        DataClient client = new DataClient(mContext);
        try {
            return client.reserveTracks();

        } catch (CommunicationException e) {
            Log.e(Constants.PREFERENCES, "Failed to reserve tracks", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        if(tracks != null) {

            // Persist the entities
            DbFacade facade = DbFacade.getInstance();
            try {
                facade.saveEntities(tracks.toArray(new Track[tracks.size()]), tracks.size());

                // Return the tracks
                if(mCaller != null) {
                    mCaller.onPostExecute(tracks);
                }

            } catch (SQLException e) {
                Log.e(Constants.PREFERENCES, "Failed to persist the entities!", e);
            }
        }
    }
}
