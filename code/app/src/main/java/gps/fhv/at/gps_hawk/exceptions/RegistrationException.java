package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class RegistrationException extends Exception {
    public RegistrationException() {
    }

    public RegistrationException(String detailMessage) {
        super(detailMessage);
    }

    public RegistrationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RegistrationException(Throwable throwable) {
        super(throwable);
    }
}
