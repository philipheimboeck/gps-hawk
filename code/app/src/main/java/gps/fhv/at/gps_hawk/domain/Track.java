package gps.fhv.at.gps_hawk.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.helper.TokenHelper;

/**
 * Created by Tobias on 30.10.2015.
 */
public class Track extends DomainBase implements Serializable, IJSONable, IExportable {
    private int _id;
    private String _externalId;
    private long _startDateTime;
    private long _endDateTime;
    private int _isValid;
    private int _isExported;

    public Track() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getExternalId() {
        return _externalId;
    }

    public void setExternalId(String _externalId) {
        this._externalId = _externalId;
    }

    public long getStartDateTime() {
        return _startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        _startDateTime = startDateTime;
    }

    public long getEndDateTime() {
        return _endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
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

            json.put("startDate", getStartDateTime());
            json.put("endDate", getEndDateTime());
            json.put("isValid", getIsValid());

        } catch (JSONException e) {
            Log.e(Constants.PREFERENCES, "Could not create json-object completely", e);
        }
        return json.toString();

    }

    public int getIsExported() {
        return _isExported;
    }

    public void setIsExported(int isExported) {
        _isExported = isExported;
    }
}
