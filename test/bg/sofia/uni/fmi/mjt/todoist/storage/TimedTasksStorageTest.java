package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimedTasksStorageTest {
    private static TimedTasksStorage timedTasksStorage = new TimedTasksStorage();
    private TimedTasksStorage empty = new TimedTasksStorage();

    @BeforeAll
    static void setUpTestCase() throws TaskAlreadyExistException, TimeFrameMismatchException, TaskNotFoundException {
        timedTasksStorage.addTask(new Task.TaskBuilder("task1").setDate(LocalDate.of(2024, 2,18)).build());
        timedTasksStorage.addTask(new Task.TaskBuilder("task2").setDate(LocalDate.of(2024, 2,17)).build());
        timedTasksStorage.addTask(new Task.TaskBuilder("task3").setDate(LocalDate.of(2024, 2,16)).build());
        timedTasksStorage.getTask("task1", LocalDate.of(2024, 2,18)).finish();
    }

    @Test
    void testUpdateTask() throws TaskAlreadyExistException, TimeFrameMismatchException, TaskNotFoundException {
        TimedTasksStorage timedTasksStorage1 = new TimedTasksStorage();
        timedTasksStorage1.addTask(new Task.TaskBuilder("task1").setDate(LocalDate.of(2024, 2,18)).build());
        timedTasksStorage1.addTask(new Task.TaskBuilder("task2").setDate(LocalDate.of(2024, 2,17)).build());
        timedTasksStorage1.addTask(new Task.TaskBuilder("task3").setDate(LocalDate.of(2024, 2,16)).build());

        timedTasksStorage1.updateTask(new Task.TaskBuilder("task1").setDate(LocalDate.of(2024, 2,19)).build());

        assertEquals(new Task.TaskBuilder("task1").setDate(LocalDate.of(2024, 2,19)).build(), timedTasksStorage1.getTask("task1", LocalDate.of(2024, 2,19)),
            "updateTask should update successfully");

        assertThrows(TaskNotFoundException.class, ()->timedTasksStorage1.getTask("task1", LocalDate.of(2024, 2,18)),
            "updateTask should delete the old task");
    }

    @Test
    void testDeleteTask() throws TimeFrameMismatchException, TaskAlreadyExistException, TaskNotFoundException {
        TimedTasksStorage timedTasksStorage1 = new TimedTasksStorage();
        timedTasksStorage1.addTask(new Task.TaskBuilder("task1").setDate(LocalDate.of(2024, 2,18)).build());

        timedTasksStorage1.deleteTask(LocalDate.of(2024,2,18), "task1");
        assertThrows(TaskNotFoundException.class, ()->timedTasksStorage1.getTask("task1",LocalDate.of(2024, 2,18) ),
            "deleteTask should delete the task");
    }

    @Test
    void testGetCompletedTasks() throws TimeFrameMismatchException, TaskNotFoundException {
        assertIterableEquals(List.of(new Task.TaskBuilder("task1").setDate(LocalDate.of(2024, 2,18)).build()), timedTasksStorage.getCompletedTasks(),
            "getCompletedTasks should return correct list");
    }

}
