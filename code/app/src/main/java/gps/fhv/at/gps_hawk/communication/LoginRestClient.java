package gps.fhv.at.gps_hawk.communication;

import android.content.Context;

import javax.security.auth.login.LoginException;

import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class LoginRestClient extends RestClient implements ILoginClient {

    public LoginRestClient(Context context) {
        super(context);
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public void register(String username, String password) throws RegistrationException {

    }

    @Override
    public void login(String username, String password, String deviceID) throws LoginException {

    }
}
