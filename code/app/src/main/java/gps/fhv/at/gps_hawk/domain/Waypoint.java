package gps.fhv.at.gps_hawk.domain;

/**
 * Created by Tobias on 24.10.2015.
 */
public class Waypoint {
    private Position _position;
    private int _nrOfSattelites;

    public Position getPosition() {
        return _position;
    }

    public void setPosition(Position position) {
        _position = position;
    }

    public int getNrOfSattelites() {
        return _nrOfSattelites;
    }

    public void setNrOfSattelites(int nrOfSattelites) {
        _nrOfSattelites = nrOfSattelites;
    }

}
