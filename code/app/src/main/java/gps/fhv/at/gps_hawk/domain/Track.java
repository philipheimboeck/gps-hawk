package gps.fhv.at.gps_hawk.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

import gps.fhv.at.gps_hawk.helper.DateHelper;

/**
 * Created by Tobias on 30.10.2015.
 */
public class Track extends DomainBase implements Serializable, IJSONable {
    private int _id;
    private Calendar _startDateTime;
    private Calendar _endDateTime;

    public Track() {
        _startDateTime = Calendar.getInstance();
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public Calendar getStartDateTime() {
        return _startDateTime;
    }

    public void setStartDateTime(Calendar startDateTime) {
        _startDateTime = startDateTime;
    }

    public Calendar getEndDateTime() {
        return _endDateTime;
    }

    public void setEndDateTime(Calendar endDateTime) {
        _endDateTime = endDateTime;
    }

    @Override
    public String toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id",getId());
            json.put("startDateTime", DateHelper.toSql(getStartDateTime()));
            json.put("endDateTime", DateHelper.toSql(getEndDateTime()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();

    }
}
