package bg.sofia.uni.fmi.mjt.todoist.exception;

public class CollaborationNameAlreadyTakenException extends Exception {
    public CollaborationNameAlreadyTakenException(String message) {
        super(message);
    }

    public CollaborationNameAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
