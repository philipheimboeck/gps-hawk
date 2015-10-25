package gps.fhv.at.gps_hawk.domain;

import java.util.Calendar;

/**
 * Created by Tobias on 24.10.2015.
 */
public class Waypoint extends DomainBase {
    private long _id;
    private long _positionId;
    private int _nrOfSattelites;
    private Calendar _timestampCaptured;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public long getPositionId() {
        return _positionId;
    }

    public void setPositionId(long positionId) {
        _positionId = positionId;
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
}
