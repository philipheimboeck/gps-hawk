package gps.fhv.at.gps_hawk.domain;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Created by Tobias on 24.10.2015.
 */
public class Waypoint extends DomainBase implements IJSONable, Serializable, IExportable {
    private int _id;
    private int _trackId;
    private int _isExported;
    private int _nrOfSatellites;
    private int _vehicleId;
    private long _unixtimestampCaptured;
    private float _accuracy;
    private float _speed;
    private String _provider;
    private double _lng;
    private double _lat;
    private double _altitude;

    // Referencec, that should be set only to export via JSON!
    private Vehicle _vehicle;
    private Track _track;

    /**
     * Bearing is the horizontal direction of travel of this device, and is not related to the device orientation.
     * It is guaranteed to be in the range (0.0, 360.0] if the device has a bearing.
     */
    private float _bearing;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getNrOfSatellites() {
        return _nrOfSatellites;
    }

    public void setNrOfSatellites(int nrOfSatellites) {
        _nrOfSatellites = nrOfSatellites;
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

    public int getTrackId() {
        return _trackId;
    }

    public void setTrackId(int trackId) {
        _trackId = trackId;
    }

    public int getIsExported() {
        return _isExported;
    }

    public void setIsExported(int isExported) {
        _isExported = isExported;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("accuracy", getAccuracy());
            json.put("nrOfSatellites", getNrOfSatellites());
            json.put("timestampCaptured", getUnixtimestampCaptured());
            json.put("speed", getSpeed());
            json.put("provider", getProvider());
            json.put("bearing", getBearing());
            json.put("lng", getLng());
            json.put("lat", getLat());
            json.put("altitude", getAltitude());

            if (getVehicle() != null) {
                json.put("vehicle", getVehicle().toJSON());
            } else {
                json.put("vehicleId", getVehicleId());
            }

            // Ensure the domain object is set! Otherwise we won't get the external ID of it
            json.put("track", getTrack().toJSON());

        } catch (JSONException e) {
            Log.e(Constants.PREFERENCES, "Could not create json-object completely", e);
        }
        return json;
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

    public LatLng createLatLng() {
        return new LatLng(getLat(), getLng());
    }

    public int getVehicleId() {
        return _vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        _vehicleId = vehicleId;
    }

    public Vehicle getVehicle() {
        return _vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        _vehicle = vehicle;
    }

    public Track getTrack() {
        return _track;
    }

    public void setTrack(Track track) {
        _track = track;
    }

    public long getUnixtimestampCaptured() {
        return _unixtimestampCaptured;
    }

    public void setUnixtimestampCaptured(long unixtimestampCaptured) {
        _unixtimestampCaptured = unixtimestampCaptured;
    }

}
