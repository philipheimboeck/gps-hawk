package gps.fhv.at.gps_hawk.tasks;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginRestClient;
import gps.fhv.at.gps_hawk.exceptions.LoginException;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private final String mUser;
    private final String mPassword;
    private final String mAndroidId;
    private final IAsyncTaskCaller<Void, String> mCaller;
    private final Context mContext;

    public LoginTask(final String user, final String password, String androidId, final IAsyncTaskCaller<Void, String> caller, Context context) {
        mUser = user;
        mPassword = password;
        mAndroidId = androidId;
        mCaller = caller;
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {

        ILoginClient loginClient = new LoginRestClient(mContext);

        try {
            return loginClient.login(mUser, mPassword, mAndroidId);

        } catch (LoginException e) {
            Log.e(Constants.PREFERENCES, "Login failed", e);
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