package bg.sofia.uni.fmi.mjt.todoist.exception;

public class TimeFrameMismatchException extends Exception {
    public TimeFrameMismatchException(String message) {
        super(message);
    }

    public TimeFrameMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}