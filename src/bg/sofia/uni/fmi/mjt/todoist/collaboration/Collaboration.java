package bg.sofia.uni.fmi.mjt.todoist.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserAlreadyInCollaborationException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.task.CollaborationTask;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collaboration implements Serializable {
    String name;
    private final Set<String> participants;
    private String creatorsUsername;
    private final Map<String, CollaborationTask> tasks;

    public Collaboration(String collaborationsName, String creatorsUsername) {
        if (creatorsUsername == null || creatorsUsername.isBlank()) {
            throw new IllegalArgumentException("creatorsUsername or collaborationsName cannot be null or blank");
        }
        this.name = collaborationsName;
        this.creatorsUsername = creatorsUsername;
        this.participants = new HashSet<>();
        participants.add(creatorsUsername);
        tasks = new HashMap<>();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Collaboration otherCollaboration = (Collaboration) other;
        return name.equals(otherCollaboration.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public List<CollaborationTask> listTasks() {
        return List.copyOf(tasks.values());
    }

    public Set<String> getParticipants() {
        return Set.copyOf(participants);
    }

    public String getCreator() {
        return creatorsUsername;
    }

    public boolean isParticipant(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username can not be null or blank");
        }

        return participants.contains(username);
    }

    public void addUser(String username) throws UserAlreadyInCollaborationException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username can not be null or blank");
        }
        if (participants.contains(username)) {
            throw new UserAlreadyInCollaborationException("This user is already in the collaboration.");
        }

        participants.add(username);
    }

    public void assignTask(String username, String task) throws UserNotFoundException, TaskNotFoundException {
        if (username == null || username.isBlank() || task == null || task.isBlank()) {
            throw new IllegalArgumentException("username or task can not be null or blank");
        }

        if (!participants.contains(username)) {
            throw new UserNotFoundException("This user was not found.");
        }

        if (!tasks.containsKey(task)) {
            throw new TaskNotFoundException("This task was not found.");
        }

        tasks.get(task).setAssigneeUsername(username);
    }

    public String getName() {
        return name;
    }
}