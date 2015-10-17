package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class LoginException extends Exception {
    public LoginException() {
    }

    public LoginException(String detailMessage) {
        super(detailMessage);
    }

    public LoginException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public LoginException(Throwable throwable) {
        super(throwable);
    }
}
