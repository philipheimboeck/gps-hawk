package gps.fhv.at.gps_hawk.domain;

import java.util.Calendar;

/**
 * Created by Tobias on 02.11.2015.
 */
public class Exception2Log extends DomainBase implements IExportable {

    private int _id;
    private String _stackTrace;
    private String _message;
    private Calendar _dateTime;
    private int _isExported;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }

    public String getStackTrace() {
        return _stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        _stackTrace = stackTrace;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public Calendar getDateTime() {
        return _dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        _dateTime = dateTime;
    }

    public int getIsExported() {
        return _isExported;
    }

    public void setIsExported(int isExported) {
        _isExported = isExported;
    }
}
