package gps.fhv.at.gps_hawk.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Philip Heimböck
 * Date: 30.10.15
 */
public class Vehicle extends DomainBase implements IJSONable {

    private int _id;
    private int _uiId;

    public Vehicle(int id) {
        _id = id;
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }

    public int getUiId() {
        return _uiId;
    }

    public void setUiId(int uiId) {
        _uiId = uiId;
    }

    @Override
    public String toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id",getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();

    }
}
