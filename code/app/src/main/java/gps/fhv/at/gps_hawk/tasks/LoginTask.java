package gps.fhv.at.gps_hawk.tasks;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */

import android.os.AsyncTask;

import javax.security.auth.login.LoginException;

import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginTestClient;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mUser;
    private final String mPassword;
    private final String mAndroidId;
    private final IAsyncTaskCaller<Void> mCaller;

    public LoginTask(final String user, final String password, String androidId, final IAsyncTaskCaller<Void> caller) {
        mUser = user;
        mPassword = password;
        mAndroidId = androidId;
        mCaller = caller;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        ILoginClient loginClient = new LoginTestClient();

        try {
            loginClient.login(mUser, mPassword, mAndroidId);

        } catch (LoginException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mCaller.onPostExecute(success);
    }

    @Override
    protected void onCancelled() {
        mCaller.onCancelled();
    }


}