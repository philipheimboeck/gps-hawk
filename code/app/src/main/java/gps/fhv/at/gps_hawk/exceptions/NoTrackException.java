package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 31.12.15
 */
public class NoTrackException extends Exception {
    public NoTrackException() {
    }

    public NoTrackException(String detailMessage) {
        super(detailMessage);
    }

    public NoTrackException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoTrackException(Throwable throwable) {
        super(throwable);
    }
}
