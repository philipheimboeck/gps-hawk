package gps.fhv.at.gps_hawk.communication;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;
import gps.fhv.at.gps_hawk.exceptions.RegistrationException;
import gps.fhv.at.gps_hawk.exceptions.UnExpectedResultException;

/**
 * Created by Tobias on 25.10.2015.
 */
public class ExportClient extends RestClient {


    public ExportClient(Context context) {
        super(context);
    }

    public boolean exportCollectedWaypoints(ExportContext expCtx) throws UnExpectedResultException {

        try {

            URL url = new URL(expCtx.getUrl());

            HashMap<String, String> params = new HashMap<>();
            params.put("deviceid", expCtx.getAndroidId());

            HTTPAnswer answer = post(url, expCtx.getExportList(), expCtx.getCollectionName(), params);

            if (answer.responseCode != 200) {
                throw new RegistrationException("Could not export data!");
            }

            // Expects as return-string:
            // {"waypoints":4}

            JSONObject response = new JSONObject(answer.content);
            if (!(response.has("amount") && response.getInt("amount") == expCtx.getExportList().size())) {
                throw new UnExpectedResultException("Amount of saved items does not match amount of transferred items");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
