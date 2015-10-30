package gps.fhv.at.gps_hawk.domain;

import java.util.Calendar;

/**
 * Created by Tobias on 30.10.2015.
 */
public class Track extends DomainBase {
    private int _id;
    private Calendar _startDateTime;
    private Calendar _endDateTime;

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
}
