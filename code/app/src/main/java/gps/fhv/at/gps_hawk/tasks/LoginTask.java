package gps.fhv.at.gps_hawk.tasks;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */

import android.os.AsyncTask;

import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginTestClient;
import gps.fhv.at.gps_hawk.exceptions.LoginException;
import gps.fhv.at.gps_hawk.helper.TokenHelper;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private final String mUser;
    private final String mPassword;
    private final String mAndroidId;
    private final IAsyncTaskCaller<Void, Boolean> mCaller;
    private final TokenHelper mTokenHelper;

    public LoginTask(final String user, final String password, String androidId, final IAsyncTaskCaller<Void, Boolean> caller, TokenHelper tokenHelper) {
        mUser = user;
        mPassword = password;
        mAndroidId = androidId;
        mCaller = caller;
        mTokenHelper = tokenHelper;
    }

    @Override
    protected String doInBackground(Void... params) {

        ILoginClient loginClient = new LoginTestClient();

        try {
            return loginClient.login(mUser, mPassword, mAndroidId);

        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final String token) {

        if (token != null) {
            // Save token to settings
            mTokenHelper.setToken(token);
        }

        mCaller.onPostExecute(token != null);
    }

    @Override
    protected void onCancelled() {
        mCaller.onCancelled();
    }


}