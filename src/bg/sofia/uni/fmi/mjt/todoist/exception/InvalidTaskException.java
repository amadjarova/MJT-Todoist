package bg.sofia.uni.fmi.mjt.todoist.exception;

public class InvalidTaskException extends Exception {
    public InvalidTaskException(String message) {
        super(message);
    }

    public InvalidTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
