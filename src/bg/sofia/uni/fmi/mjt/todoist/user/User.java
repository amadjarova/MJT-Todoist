package bg.sofia.uni.fmi.mjt.todoist.user;

import bg.sofia.uni.fmi.mjt.todoist.storage.InboxTaskStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.TimedTasksStorage;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final InboxTaskStorage inboxStorage;
    private final TimedTasksStorage timedTasksStorage;

    public User(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("username or password can not be null or blank");
        }

        this.username = username;
        this.password = password;
        this.inboxStorage = new InboxTaskStorage();
        this.timedTasksStorage = new TimedTasksStorage();
    }

    public boolean isPasswordCorrect(String passwordToCheck) {
        if (passwordToCheck == null) {
            throw new IllegalArgumentException("passwordToCheck can not be null");
        }

        return  password.equals(passwordToCheck);
    }

    public String getUsername() {
        return username;
    }

    public InboxTaskStorage getInboxTaskStorage() {
        return inboxStorage;
    }

    public TimedTasksStorage getTimedTasksStorage() {
        return timedTasksStorage;
    }
}
