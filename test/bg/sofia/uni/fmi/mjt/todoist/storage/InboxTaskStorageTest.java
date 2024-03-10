package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InboxTaskStorageTest {
    private static InboxTaskStorage inboxTaskStorage = new InboxTaskStorage();
    private InboxTaskStorage empty = new InboxTaskStorage();

    @BeforeAll
    static void setUpTestCase() throws TaskAlreadyExistException, TaskNotFoundException {
        inboxTaskStorage.addTask(new Task.TaskBuilder("task1").build());
        inboxTaskStorage.addTask(new Task.TaskBuilder("task2").build());
        inboxTaskStorage.getTask("task1").finish();
    }

    @Test
    void testDeleteTaskFound() throws TaskAlreadyExistException, TaskNotFoundException {
        empty.addTask(new Task.TaskBuilder("task1").build());
        assertEquals(new Task.TaskBuilder("task1").build(), empty.getTask("task1"),"failed during setup" );
        empty.deleteTask("task1");
        assertThrows(TaskNotFoundException.class, ()->empty.getTask("task1"),
            "deleteTask should delete the task");

    }

    @Test
    void testDeleteTaskNotFound()  {
        assertThrows(TaskNotFoundException.class, ()->inboxTaskStorage.deleteTask("task10"),
            "deleteTask should throw exception when the task is not found");
    }

    @Test
    void testListCompletedTasks() {
        assertIterableEquals(List.of(new Task.TaskBuilder("task1").build()), inboxTaskStorage.listCompletedTasks(),
            "listCompletedTasks should return correct list");
    }
}
