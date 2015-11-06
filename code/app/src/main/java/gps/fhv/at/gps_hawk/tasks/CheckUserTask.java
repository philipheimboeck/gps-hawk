package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;

import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginRestClient;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class CheckUserTask extends AsyncTask<Void, Void, Boolean> {

    private String mUser;
    private IAsyncTaskCaller<Void, Boolean> mCaller;
    private Context mContext;

    public CheckUserTask(String user, IAsyncTaskCaller<Void, Boolean> caller, Context context) {
        this.mUser = user;
        this.mCaller = caller;
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ILoginClient loginClient = new LoginRestClient(mContext);
        return loginClient.userExists(mUser);
    }

    @Override
    protected void onPostExecute(Boolean existing) {
        mCaller.onPostExecute(existing);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
