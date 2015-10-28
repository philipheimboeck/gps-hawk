package gps.fhv.at.gps_hawk.communication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;
import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportClient extends RestClient {


    public ExportClient(Context context) {
        super(context);
    }

    public boolean exportCollectedWaypoints(ExportContext expCtx) {

        JSONObject object = new JSONObject();
        try {

//            JSONArray jsonArray = new JSONArray(expCtx.getWaypointList());
            object.put("waypoints", getJsonArray(expCtx.getWaypointList()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            URL url = new URL(expCtx.getUrl());

            HashMap<String, String> params = new HashMap<>();
            params.put("waypoints", object.toString());

            HTTPAnswer answer = post(url, params);
            if (answer.responseCode != 200) {
                throw new RegistrationException("Registration was not successful!");
            }
            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RegistrationException e) {
            e.printStackTrace();
        } catch (NoConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
