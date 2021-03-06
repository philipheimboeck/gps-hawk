package gps.fhv.at.gps_hawk.communication;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.exceptions.LoginException;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;
import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class LoginRestClient extends RestClient implements ILoginClient {

    public static final String REST_USER = "app/users/";

    private String getRestUserExists() {
        return getRestServer() + REST_USER + "exists/$USER";
    }

    private String getRestUserRegister() {
        return getRestServer() + REST_USER + "register";
    }

    private String getRestUserLogin() {
        return getRestServer() + REST_USER + "login/$DEVICE";
    }

    public LoginRestClient(Context context) {
        super(context);
    }

    @Override
    public boolean userExists(String username) {
        try {
            String urlString = getRestUserExists().replace("$USER", URLEncoder.encode(username, "UTF-8"));
            URL url = new URL(urlString);
            HTTPAnswer answer = get(url);
            if (answer.responseCode == 200) {
                return true;
            }
            if (answer.responseCode == 404) {
                return false;
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            Log.e(Constants.PREFERENCES, "Checking user failed", e);
        }

        // Todo Error
        return true;
    }

    @Override
    public String register(String username, String password, String deviceID) throws RegistrationException {
        try {
            String urlString = getRestUserRegister();
            URL url = new URL(urlString);

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            params.put("deviceId", deviceID);

            HTTPAnswer answer = post(url, params);
            if (answer.responseCode != 200) {
                throw new RegistrationException("Registration was not successful!");
            }

            return answer.content;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            Log.e(Constants.PREFERENCES, "Registration of user failed", e);

            throw new RegistrationException("Registration failed", e);
        }
    }

    @Override
    public String login(String username, String password, String deviceID) throws LoginException {
        try {
            String urlString = getRestUserLogin().replace("$DEVICE", URLEncoder.encode(deviceID, "UTF-8"));

            URL url = new URL(urlString);

            HashMap<String, String> headers = new HashMap<>();
            byte[] credentials = (username + ":" + password).getBytes();
            headers.put("Authorization", "basic " + Base64.encodeToString(credentials, Base64.NO_WRAP));

            HTTPAnswer answer = get(url, headers);
            if (answer.responseCode != 200) {
                throw new LoginException("Login was not successful!");
            }

            JSONObject object = new JSONObject(answer.content);
            if (!object.has("token")) {
                throw new LoginException("Login failed due to missing token!");
            }

            return object.getString("token");

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            Log.e(Constants.PREFERENCES, "Login failed", e);

            throw new LoginException("Login failed", e);
        } catch (JSONException e) {
            Log.e(Constants.PREFERENCES, "Login failed due to JSON-Problem", e);

            throw new LoginException("Login failed due to invalid answer", e);
        }
    }
}
