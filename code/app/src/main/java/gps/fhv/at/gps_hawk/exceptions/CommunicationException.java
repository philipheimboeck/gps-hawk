package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 11.11.15
 */
public class CommunicationException extends Exception {
    public CommunicationException() {
    }

    public CommunicationException(String detailMessage) {
        super(detailMessage);
    }

    public CommunicationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CommunicationException(Throwable throwable) {
        super(throwable);
    }
}
