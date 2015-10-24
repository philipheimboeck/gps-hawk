package gps.fhv.at.gps_hawk.domain;

/**
 * Created by Tobias on 24.10.2015.
 */
public class Position {
    private double _lng;
    private double _lat;

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
}
