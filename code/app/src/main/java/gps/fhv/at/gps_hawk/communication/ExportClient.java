package gps.fhv.at.gps_hawk.communication;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import gps.fhv.at.gps_hawk.Constants;
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
                Log.e(Constants.PREFERENCES, "Could not export data: " + expCtx.getCollectionName() + ", responseCode was: " + answer.responseCode);
                throw new RegistrationException("Could not export data!");
            }

            // Expects as return-string:
            // example {"waypoints":4}

            JSONObject response = new JSONObject(answer.content);
            if (!(response.has("amount") && response.getInt("amount") == expCtx.getExportList().size())) {
                String msg = "Amount of saved items (" + (response.has("amount") ? response.getInt("amount") : "null") + ")does not match amount of transferred items";
                Log.e(Constants.PREFERENCES, msg);
                throw new UnExpectedResultException(msg);
            }
            Log.d(Constants.PREFERENCES, "Exportet " + response.getInt("amount") + " " + expCtx.getCollectionName());

            return true;

        } catch (MalformedURLException e) {
            Log.e(Constants.PREFERENCES, "Export failed: ", e);
        } catch (RegistrationException e) {
            Log.e(Constants.PREFERENCES, "Export failed: ", e);
        } catch (NoConnectionException e) {
            Log.e(Constants.PREFERENCES, "Export failed: ", e);
        } catch (IOException e) {
            Log.e(Constants.PREFERENCES, "Export failed: ", e);
        } catch (JSONException e) {
            Log.e(Constants.PREFERENCES, "Export failed: ", e);
        }
        return false;
    }
}
