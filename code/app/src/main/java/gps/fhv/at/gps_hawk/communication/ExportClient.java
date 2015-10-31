package gps.fhv.at.gps_hawk.communication;

import android.content.Context;

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

        try {

            URL url = new URL(expCtx.getUrl());

            HashMap<String, String> params = new HashMap<>();
            params.put("deviceid", expCtx.getAndroidId());

            HTTPAnswer answer = post(url, expCtx.getWaypointList(), "waypoints", params);

            if (answer.responseCode != 200) {
                throw new RegistrationException("Could not export data!");
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
