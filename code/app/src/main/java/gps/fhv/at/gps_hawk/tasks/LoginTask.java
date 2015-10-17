package gps.fhv.at.gps_hawk.tasks;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.communication.ILoginClient;
import gps.fhv.at.gps_hawk.communication.LoginTestClient;
import gps.fhv.at.gps_hawk.exceptions.LoginException;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginTask extends AsyncTask<Void, Void, String> {

    private final String mUser;
    private final String mPassword;
    private final String mAndroidId;
    private final IAsyncTaskCaller<Void, Boolean> mCaller;
    private final Context mContext;

    public LoginTask(final String user, final String password, String androidId, final IAsyncTaskCaller<Void, Boolean> caller, Context context) {
        mUser = user;
        mPassword = password;
        mAndroidId = androidId;
        mCaller = caller;
        mContext = context;
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
            SharedPreferences preferences = mContext.getSharedPreferences(Constants.PREF_DEVICE_TOKEN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.PREF_DEVICE_TOKEN, token);
            editor.apply();
        }

        mCaller.onPostExecute(token != null);
    }

    @Override
    protected void onCancelled() {
        mCaller.onCancelled();
    }


}