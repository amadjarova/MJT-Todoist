package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimedTasksStorage implements Serializable {
    private final Map<LocalDate, Map<String, Task>> storage;

    public TimedTasksStorage() {
        storage = new HashMap<>();
    }

    public void addTask(Task task) throws TaskAlreadyExistException {
        if (task == null) {
            throw new IllegalArgumentException("task cannot be null");
        }
        if (storage.containsKey(task.getDate()) && storage.get(task.getDate()).containsKey(task.getName())) {
            throw new TaskAlreadyExistException("There is another task with the same name and date");
        }

        storage.putIfAbsent(task.getDate(), new HashMap<>());
        storage.get(task.getDate()).put(task.getName(), task);
    }

    private boolean isTaskNamePresent(String taskName) {
        for (Map<String, Task> tasks : storage.values()) {
            if (tasks.containsKey(taskName)) {
                return true;
            }
        }

        return false;
    }

    private void  removeTheOldTask(String taskName) {
        for (Map<String, Task> tasksDate : storage.values()) {
            if (tasksDate.containsKey(taskName)) {
                tasksDate.remove(taskName);
                if (tasksDate.isEmpty()) {
                    storage.remove(tasksDate);
                }
            }
        }

    }

    public void updateTask(Task task) throws TaskNotFoundException {
        if (task == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        if (!isTaskNamePresent(task.getName())) {
            throw new TaskNotFoundException("The task was not found");
        }
        removeTheOldTask(task.getName());
        storage.putIfAbsent(task.getDate(), new HashMap<>());
        storage.get(task.getDate()).put(task.getName(), task);
    }

    public Task getTask(String task, LocalDate date) throws TaskNotFoundException {
        if (task == null || task.isBlank()) {
            throw new IllegalArgumentException("task can not be null or blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date can not be null");
        }
        if (!storage.containsKey(date) || !storage.get(date).containsKey(task)) {
            throw new TaskNotFoundException("The task was not found.");
        }

        return storage.get(date).get(task);
    }

    public void deleteTask(LocalDate taskDate, String taskName) throws TaskNotFoundException {
        if (!storage.containsKey(taskDate) && storage.get(taskDate).containsKey(taskName)) {
            throw new TaskNotFoundException("The task was not found.");
        }

        storage.get(taskDate).remove(taskName);
        if (storage.get(taskDate).isEmpty()) {
            storage.remove(taskDate);
        }
    }

    public List<Task> listTasks(LocalDate date) {
        return
            storage.getOrDefault(date, Collections.emptyMap()).values().stream().toList();

    }

    public List<Task> listDashboard() {
        return listTasks(LocalDate.now());
    }

    public List<Task> getCompletedTasks() {
        return storage.values()
            .stream()
            .flatMap(s -> s.values().stream())
            .filter(Task::isCompleted)
            .toList();
    }
}
