package gps.fhv.at.gps_hawk.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import gps.fhv.at.gps_hawk.helper.DateHelper;

/**
 * Created by Tobias on 24.10.2015.
 */
public class Waypoint extends DomainBase implements IJSONable {
    private long _id;
    private int _nrOfSattelites;
    private Calendar _timestampCaptured;
    private float _accuracy;
    private float _speed;
    private String _provider;
    private double _lng;
    private double _lat;
    private double _altitude;


    /**
     * Bearing is the horizontal direction of travel of this device, and is not related to the device orientation.
     * It is guaranteed to be in the range (0.0, 360.0] if the device has a bearing.
     */
    private float _bearing;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public int getNrOfSattelites() {
        return _nrOfSattelites;
    }

    public void setNrOfSattelites(int nrOfSattelites) {
        _nrOfSattelites = nrOfSattelites;
    }

    public Calendar getTimestampCaptured() {
        return _timestampCaptured;
    }

    public void setTimestampCaptured(Calendar timestampCaptured) {
        _timestampCaptured = timestampCaptured;
    }

    public float getAccuracy() {
        return _accuracy;
    }

    public void setAccuracy(float accuracy) {
        _accuracy = accuracy;
    }

    public float getSpeed() {
        return _speed;
    }

    public void setSpeed(float speed) {
        _speed = speed;
    }

    public String getProvider() {
        return _provider;
    }

    public void setProvider(String provider) {
        _provider = provider;
    }

    public float getBearing() {
        return _bearing;
    }

    public void setBearing(float bearing) {
        _bearing = bearing;
    }

    @Override
    public String toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("accuracy",getAccuracy());
            json.put("nrOfSattelites",getNrOfSattelites());
            json.put("timestampCaptured", DateHelper.toSql(getTimestampCaptured()));
            json.put("speed",getSpeed());
            json.put("provider",getProvider());
            json.put("bearing",getBearing());
            json.put("lng",getLng());
            json.put("lat",getLat());
            json.put("altitude",getAltitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public double getLng() {
        return _lng;
    }

    public void setLng(double lng) {
        _lng = lng;
    }

    public double getLat() {
        return _lat;
    }

    public void setLat(double lat) {
        _lat = lat;
    }

    public double getAltitude() {
        return _altitude;
    }

    public void setAltitude(double altitude) {
        _altitude = altitude;
    }
}
