package gps.fhv.at.gps_hawk.domain;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Tobias on 30.10.2015.
 */
public class Track extends DomainBase implements Serializable {
    private int _id;
    private Calendar _startDateTime;
    private Calendar _endDateTime;

    public Track() {
        _startDateTime = Calendar.getInstance();
    }

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
