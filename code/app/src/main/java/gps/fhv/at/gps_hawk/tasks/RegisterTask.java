package gps.fhv.at.gps_hawk.tasks;

import android.content.Context;
import android.os.AsyncTask;

import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginRestClient;
import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class RegisterTask extends AsyncTask<Void, Void, String> {

    private final String mUser;
    private final String mPassword;
    private final String mAndroidId;
    private final IAsyncTaskCaller<Void, String> mCaller;
    private final Context mContext;

    public RegisterTask(final String user, final String password, final String androidId, IAsyncTaskCaller<Void, String> caller, Context context) {
        this.mUser = user;
        this.mPassword = password;
        this.mAndroidId = androidId;
        this.mCaller = caller;
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        ILoginClient loginClient = new LoginRestClient(mContext);

        try {
            return loginClient.register(mUser, mPassword, mAndroidId);

        } catch (RegistrationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final String token) {
        mCaller.onPostExecute(token);
    }

    @Override
    protected void onCancelled() {
        mCaller.onCancelled();
    }
}
