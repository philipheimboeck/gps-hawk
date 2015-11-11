package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 11.11.15
 */
public class RestException extends Exception {
    public RestException() {
    }

    public RestException(String detailMessage) {
        super(detailMessage);
    }

    public RestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RestException(Throwable throwable) {
        super(throwable);
    }
}
