package gps.fhv.at.gps_hawk.exceptions;

/**
 * Author: Philip Heimböck
 * Date: 06.01.16
 * <p/>
 * Class for task exception handling
 */
public class TaskException extends Exception {
    public TaskException() {
    }

    public TaskException(String detailMessage) {
        super(detailMessage);
    }

    public TaskException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TaskException(Throwable throwable) {
        super(throwable);
    }
}
