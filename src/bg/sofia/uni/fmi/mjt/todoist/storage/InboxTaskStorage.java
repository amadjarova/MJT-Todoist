package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboxTaskStorage implements Serializable {
    private final Map<String, Task> storage;

    public InboxTaskStorage() {
        this.storage = new HashMap<>();
    }

    public void addTask(Task task) throws TaskAlreadyExistException {
        if (task == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        if (storage.containsKey(task.getName())) {
            throw new TaskAlreadyExistException("There is another task with the same name.");
        }
        storage.put(task.getName(), task);
    }

    public void updateTask(Task task) throws TaskNotFoundException {
        if (!storage.containsKey(task.getName())) {
            throw new TaskNotFoundException("The task was not found.");
        }
        storage.put(task.getName(), task);
    }

    public Task getTask(String task) throws TaskNotFoundException {
        if (task == null || task.isBlank()) {
            throw new IllegalArgumentException("task can not be null or blank");
        }
        if (!storage.containsKey(task)) {
            throw new TaskNotFoundException("The task was not found.");
        }

        return storage.get(task);
    }

    public void deleteTask(String taskName) throws TaskNotFoundException {
        if (taskName == null) {
            throw new IllegalArgumentException("taskName can not be null");
        }
        if (!storage.containsKey(taskName)) {
            throw new TaskNotFoundException("There is no task with this name.");
        }

        storage.remove(taskName);
    }

    public void finishTask(String taskName) throws TaskNotFoundException {
        if (taskName == null) {
            throw new IllegalArgumentException("taskName can not be null");
        }
        if (!storage.containsKey(taskName)) {
            throw new TaskNotFoundException("There is no task with this name.");
        }

        storage.get(taskName).finish();

    }

    public List<Task> listTasks() {
        return List.copyOf(storage.values());
    }

    public List<Task> listCompletedTasks() {
        return storage.values().stream()
            .filter(Task::isCompleted)
            .toList();
    }

}
