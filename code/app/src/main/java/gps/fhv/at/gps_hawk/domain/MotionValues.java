package gps.fhv.at.gps_hawk.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Created by Tobias on 08.11.2015.
 */
public class MotionValues extends DomainBase implements IExportable, IJSONable {

    public static final int MOTION_TYPE_ACCELEROMETER = 0;
    public static final int MOTION_TYPE_LINEAR_ACCELEROMETER = 1;

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
            Log.e(Constants.PREFERENCES, "Could not create json-object completely", e);
        }
        return json.toString();
    }
}
