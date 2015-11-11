package gps.fhv.at.gps_hawk.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import gps.fhv.at.gps_hawk.helper.DateHelper;

/**
 * Created by Tobias on 08.11.2015.
 */
public class MotionValues extends DomainBase implements IExportable, IJSONable {

    public static final int MOTION_TYPE_ACCELEROMETER = 0;

    private int _id;

    public float _x;
    public float _y;
    public float _z;

    public int _motionType;
    public int _isExported;

    public long _dateTimeCaptured;

    public MotionValues() {
        _isExported = 0; // set default value
    }

    public String toString() {
        return new StringBuilder().append("{ x: ").append(_x).append(", y: ").append(_y).append(", z: ").append(_z).append(" }").toString();
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }


    @Override
    public int getIsExported() {
        return _isExported;
    }

    @Override
    public void setIsExported(int isExported) {
        _isExported = isExported;
    }


    @Override
    public String toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("x", _x);
            json.put("y", _y);
            json.put("z", _z);
            json.put("capturedAt", _dateTimeCaptured);
            json.put("type", _motionType);
            json.put("id", _id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}