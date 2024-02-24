package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidPasswordException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UsernameAlreadyTakenException;
import bg.sofia.uni.fmi.mjt.todoist.user.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UsersStorage implements Serializable {
    private final Map<String, User> users;

    public UsersStorage() {
        users = new HashMap<>();
    }

    public void addUser(String username, String password) throws UsernameAlreadyTakenException {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("username or password can not be null or blank");
        }
        if (users.containsKey(username)) {
            throw new UsernameAlreadyTakenException("There is another user with the same username.");
        }

        users.put(username, new User(username, password));
    }

    public void login(String username, String password) throws UserNotFoundException, InvalidPasswordException {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("username or password can not be null or blank");
        }
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("User with this username can not be found.");
        }
        if (!users.get(username).isPasswordCorrect(password)) {
            throw new InvalidPasswordException("The password is incorrect");
        }
    }

    public User getUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username can not be null or blank");
        }

        return users.get(username);
    }

    public boolean containsUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username can not be null or blank");
        }

        return users.containsKey(username);
    }

    public void save(String fileName) {
        Path file = Path.of(fileName);

        try (var objectOutputStream = new ObjectOutputStream(Files.newOutputStream(file))) {

            for (User u : users.values()) {
                objectOutputStream.writeObject(u);
                objectOutputStream.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    public void read(String fileName) {
        Path filePath = Paths.get(fileName);

        if (!Files.exists(filePath)) {
            return;
        }
        try (var objectInputStream = new ObjectInputStream(Files.newInputStream(filePath))) {

            Object userObject;
            while ((userObject = objectInputStream.readObject()) != null) {

                User s = (User) userObject;
                users.put(s.getUsername(), s);
            }

        } catch (EOFException e) {
            // EMPTY BODY
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }
}
