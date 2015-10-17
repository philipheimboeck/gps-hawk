package gps.fhv.at.gps_hawk.communication;

import android.content.Context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.exceptions.LoginException;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;
import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class LoginRestClient extends RestClient implements ILoginClient {

    private static final String REST_USER_CHECK = "http://10.0.2.2:8080/check/$USER";
    private static final String REST_USER_REGISTER = "http://10.0.2.2:8080/register";
    private static final String REST_USER_LOGIN = "http://10.0.2.2:8080/login";
    private static final String REST_USER_TOKEN = "http://10.0.2.2:8080/token/$TOKEN";

    public LoginRestClient(Context context) {
        super(context);
    }

    @Override
    public boolean userExists(String username) {
        try {
            String urlString = REST_USER_CHECK.replace("$USER", URLEncoder.encode(username, "UTF-8"));
            URL url = new URL(urlString);
            HTTPAnswer answer = get(url);
            return answer.responseCode == 200;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            e.printStackTrace();
        }

        // Todo Error
        return false;
    }

    @Override
    public void register(String username, String password) throws RegistrationException {
        try {
            String urlString = REST_USER_REGISTER;
            URL url = new URL(urlString);

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            HTTPAnswer answer = post(url, params);
            if (answer.responseCode != 200) {
                throw new RegistrationException("Registration was not successful!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            e.printStackTrace();

            throw new RegistrationException("Registration failed", e);
        }
    }

    @Override
    public String login(String username, String password, String deviceID) throws LoginException {
        try {
            String urlString = REST_USER_LOGIN;
            URL url = new URL(urlString);

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            params.put("deviceID", password);

            HTTPAnswer answer = post(url, params);
            if (answer.responseCode != 200) {
                throw new LoginException("Login was not successful!");
            }

            return answer.content;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            e.printStackTrace();

            throw new LoginException("Login failed", e);
        }
    }

    @Override
    public boolean tokenValid(String token) {
        try {
            String urlString = REST_USER_TOKEN.replace("$TOKEN", URLEncoder.encode(token, "UTF-8"));
            URL url = new URL(urlString);
            HTTPAnswer answer = get(url);
            return answer.responseCode == 200;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            e.printStackTrace();
        }

        // Todo Error
        return false;
    }
}
