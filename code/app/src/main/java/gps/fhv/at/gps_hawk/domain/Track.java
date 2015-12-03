package gps.fhv.at.gps_hawk.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Created by Tobias on 30.10.2015.
 */
public class Track extends DomainBase implements Serializable, IJSONable {
    private int _id;
    private int _startDateTime;
    private int _endDateTime;
    private int _isValid;

    public Track() {
        _startDateTime = (int) Calendar.getInstance().getTimeInMillis() / 1000;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getStartDateTime() {
        return _startDateTime;
    }

    public void setStartDateTime(int startDateTime) {
        _startDateTime = startDateTime;
    }

    public int getEndDateTime() {
        return _endDateTime;
    }

    public void setEndDateTime(int endDateTime) {
        _endDateTime = endDateTime;
    }

    public int getIsValid() {
        return _isValid;
    }

    public void setIsValid(int isValid) {
        _isValid = isValid;
    }

    @Override
    public String toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());

            json.put("startDateTime", getStartDateTime());
            json.put("endDateTime", getEndDateTime());
            json.put("isValid", getIsValid());

        } catch (JSONException e) {
            Log.e(Constants.PREFERENCES, "Could not create json-object completely", e);
        }
        return json.toString();

    }
}
