package gps.fhv.at.gps_hawk.communication;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.helper.TokenHelper;

/**
 * Author: Philip Heimböck
 * Date: 11.11.15
 */
public class AppClient extends RestClient implements IAppClient {

    public static final String REST_APP = "app/";

    private String getRestVersion() {
        return getRestServer() + REST_APP + "version";
    }

    public AppClient(Context context) {
        super(context);
    }

    @Override
    public String getUpdateLink(String currentVersion) throws CommunicationException {
        try {
            String urlString = getRestVersion();
            URL url = new URL(urlString);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", TokenHelper.getToken(mContext));

            HTTPAnswer answer = get(url, headers);

            if (answer.responseCode != 200) {
                Log.w(Constants.PREFERENCES, "Rest exception: " + answer.content);
                throw new CommunicationException("Some error occurred");
            }

            JSONObject jsonObject = new JSONObject(answer.content);
            String newVersion = jsonObject.getString("current-version");
            Log.v(Constants.PREFERENCES,"Checked version: old: "+ currentVersion +", new: "+ newVersion);

            // If no update is available just return null
            if (currentVersion.equals(newVersion)) {
                return null;
            }

            JSONArray versions = jsonObject.getJSONArray("versions");

            // Find the URL and return it
            for (int i = 0; i < versions.length(); i++) {
                JSONObject v = versions.getJSONObject(i);
                if (v.getString("version").equals(newVersion)) {
                    return getRestServer() + v.getString("url");
                }
            }

            // We should never come here

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            Log.e(Constants.PREFERENCES,"Error receiving current-version from server", e);

        } catch (JSONException e) {
            throw new CommunicationException("Invalid answer!");
        }

        return null;
    }
}
