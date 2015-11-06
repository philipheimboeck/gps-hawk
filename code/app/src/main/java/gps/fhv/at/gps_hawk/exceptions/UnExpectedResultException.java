package gps.fhv.at.gps_hawk.exceptions;

import gps.fhv.at.gps_hawk.domain.Exception2Log;

/**
 * Created by Tobias on 06.11.2015.
 */
public class UnExpectedResultException extends Exception {

    public UnExpectedResultException() {
    }

    public UnExpectedResultException(String detailMessage) {
        super(detailMessage);
    }

    public UnExpectedResultException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnExpectedResultException(Throwable throwable) {
        super(throwable);
    }

}
