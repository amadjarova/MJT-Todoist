package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNameAlreadyTakenException;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserAlreadyInCollaborationException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotCreatorException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollaborationsStorage implements Serializable {
    private final Map<String, Collaboration> collaborations;

    public CollaborationsStorage() {
        collaborations = new HashMap<>();
    }

    public void addCollaboration(String name, String username) throws CollaborationNameAlreadyTakenException {
        if (name == null || name.isBlank() || username == null || username.isBlank()) {
            throw new IllegalArgumentException("name can not be null or blank");
        }
        if (collaborations.containsKey(name)) {
            throw new CollaborationNameAlreadyTakenException("There is another collaboration with the same name.");
        }

        collaborations.put(name, new Collaboration(name, username));
    }

    public void deleteCollaboration(String collaborationName, String username)
        throws CollaborationNotFoundException, UserNotCreatorException {
        if (collaborationName == null || collaborationName.isBlank() || username == null || username.isBlank()) {
            throw new IllegalArgumentException("name can not be null or blank");
        }
        if (!collaborations.containsKey(collaborationName)) {
            throw new CollaborationNotFoundException("There is not collaboration with this name");
        }
        if (!collaborations.get(collaborationName).getCreator().equals(username)) {
            throw new UserNotCreatorException("Only the creator can delete the collaboration.");
        }

        collaborations.remove(collaborationName);
    }

    public List<Collaboration> listCollaborationsOfUser(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name can not be null or blank");
        }

        return collaborations.values().stream()
            .filter(c -> c.isParticipant(name))
            .toList();
    }

    public void addUserToCollaboration(String username, String collaborationName) throws CollaborationNotFoundException,
        UserAlreadyInCollaborationException {
        if (collaborationName == null || username == null || collaborationName.isBlank() || username.isBlank()) {
            throw new IllegalArgumentException("username or collaborationName can not be null or blank");
        }
        if (!collaborations.containsKey(collaborationName)) {
            throw new CollaborationNotFoundException("The collaboration was not found.");
        }

        collaborations.get(collaborationName).addUser(username);
    }

    public Collaboration getCollaboration(String collaborationName) throws CollaborationNotFoundException {
        if (collaborationName == null || collaborationName.isBlank()) {
            throw new IllegalArgumentException("collaborationName cannot be null or blank.");
        }
        if (!collaborations.containsKey(collaborationName)) {
            throw new CollaborationNotFoundException("The collaboration was not found.");
        }

        return collaborations.get(collaborationName);
    }

    public void save(String fileName) {
        Path file = Path.of(fileName);

        try (var objectOutputStream = new ObjectOutputStream(Files.newOutputStream(file))) {
            objectOutputStream.writeInt(collaborations.size());
            for (Collaboration c : collaborations.values()) {
                objectOutputStream.writeObject(c);
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
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(filePath))) {

            int numCollaborations = objectInputStream.readInt();

            for (int i = 0; i < numCollaborations; i++) {
                Collaboration collaboration = (Collaboration) objectInputStream.readObject();
                collaborations.put(collaboration.getName(), collaboration);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

}
