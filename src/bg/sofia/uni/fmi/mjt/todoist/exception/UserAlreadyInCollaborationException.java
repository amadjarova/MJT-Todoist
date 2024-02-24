package bg.sofia.uni.fmi.mjt.todoist.exception;

public class UserAlreadyInCollaborationException extends Exception {
    public UserAlreadyInCollaborationException(String message) {
        super(message);
    }

    public UserAlreadyInCollaborationException(String message, Throwable cause) {
        super(message, cause);
    }
}
