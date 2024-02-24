package bg.sofia.uni.fmi.mjt.todoist.exception;

public class CollaborationNotFoundException extends Exception {
    public CollaborationNotFoundException(String message) {
        super(message);
    }

    public CollaborationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
