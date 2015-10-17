package gps.fhv.at.gps_hawk.communication;

import gps.fhv.at.gps_hawk.exceptions.LoginException;
import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class LoginTestClient implements ILoginClient {

    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String TOKEN = "token";

    @Override
    public boolean userExists(String username) {
        return username.equals(USER);
    }

    @Override
    public String register(String username, String password, String deviceID) throws RegistrationException {
        if(username.equals(USER) ) {
            throw new RegistrationException("Already existing user!");
        }

        // Simulate network access.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return TOKEN;
    }

    @Override
    public String login(String username, String password, String deviceID) throws LoginException {
        if(!username.equals(USER) || !password.equals(PASS) ) {
            throw new LoginException("Wrong Credentials!");
        }

        // Simulate network access.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return TOKEN;
    }

    @Override
    public boolean tokenValid(String token) {
        return token.equals(TOKEN);
    }
}
