package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class NoConnectionException extends Exception {
    public NoConnectionException() {
    }

    public NoConnectionException(String detailMessage) {
        super(detailMessage);
    }

    public NoConnectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoConnectionException(Throwable throwable) {
        super(throwable);
    }
}
