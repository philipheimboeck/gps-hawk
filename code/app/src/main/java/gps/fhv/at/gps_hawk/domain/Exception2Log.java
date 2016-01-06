package gps.fhv.at.gps_hawk.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Created by Tobias on 02.11.2015.
 */
public class Exception2Log extends DomainBase implements IExportable, IJSONable {

    private int _id;
    private String _stackTrace;
    private String _message;
    private int _dateTime;
    private int _isExported;
    private int _level;

    public Exception2Log() {
        _isExported = 0; // set default
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }

    public String getStackTrace() {
        return _stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        _stackTrace = stackTrace;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public int getDateTime() {
        return _dateTime;
    }

    public void setDateTime(int dateTime) {
        _dateTime = dateTime;
    }

    public int getIsExported() {
        return _isExported;
    }

    public void setIsExported(int isExported) {
        _isExported = isExported;
    }

    public int getLevel() {
        return _level;
    }

    public void setLevel(int level) {
        _level = level;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("level", getLevel());
            json.put("stackTrace", getStackTrace());
            json.put("msg", getMessage());
            json.put("dateTime", getDateTime());

        } catch (JSONException e) {
            Log.e(Constants.PREFERENCES, "Could not create json-object completely", e);
        }
        return json;
    }
}
