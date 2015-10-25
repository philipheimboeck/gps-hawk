package gps.fhv.at.gps_hawk.domain;

/**
 * Created by Tobias on 24.10.2015.
 */
public class Position extends DomainBase {
    private double _lng;
    private double _lat;
    private int _id;

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

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
}
