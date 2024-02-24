package bg.sofia.uni.fmi.mjt.todoist.exception;

public class UserNotCreatorException extends Exception {
    public UserNotCreatorException(String message) {
        super(message);
    }

    public UserNotCreatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
