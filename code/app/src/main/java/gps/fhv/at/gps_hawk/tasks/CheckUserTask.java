package gps.fhv.at.gps_hawk.tasks;

import android.os.AsyncTask;

import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginTestClient;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class CheckUserTask extends AsyncTask<Void, Void, Boolean> {

    private String mUser;
    private IAsyncTaskCaller<Void, Boolean> mCaller;

    public CheckUserTask(String user, IAsyncTaskCaller<Void, Boolean> caller) {
        this.mUser = user;
        this.mCaller = caller;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        ILoginClient loginClient = new LoginTestClient();
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
