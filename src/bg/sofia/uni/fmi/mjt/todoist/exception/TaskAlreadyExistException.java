package bg.sofia.uni.fmi.mjt.todoist.exception;

public class TaskAlreadyExistException extends Exception {

    public TaskAlreadyExistException(String message) {
        super(message);
    }

    public TaskAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
