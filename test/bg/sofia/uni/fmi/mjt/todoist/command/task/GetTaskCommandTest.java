package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.user.LoginCommand;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetTaskCommandTest {
    @Mock
    private UsersStorage usersStorage;

    @InjectMocks
    private GetTaskCommand getTaskCommand;
    private static final String USERNAME = "username";
    private static final String TASK_NAME = "task";
    private static final String PASSWORD = "123";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"name=<name>"};
        assertEquals("Please log in first.", getTaskCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"name=<name>", "task=<task>", "date=<date>"};
        assertEquals("Invalid arguments count.", getTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 1 or 2.");
    }

    @Test
    void testExecuteInvalidArgument() {
        String[] args = new String[] {"name1=<name>"};
        assertEquals("Invalid arguments.", getTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the argument is not correct.");
    }

    @Test
    void testExecuteGetInboxTaskSuccessfully() throws TaskAlreadyExistException {
        Task inboxTask = new Task(new Task.TaskBuilder(TASK_NAME));
        User user = new User(USERNAME, PASSWORD);
        user.getInboxTaskStorage().addTask(inboxTask);

        when(usersStorage.getUser(USERNAME)).thenReturn(user);

        String[] args = { "name=<task>"};
        String result = getTaskCommand.execute(args, USERNAME);

        assertEquals(inboxTask.toString(), result,
            "execute should return correct message when the task is found.");

    }

    @Test
    void testExecuteGetTimedTaskSuccessfully() throws TaskAlreadyExistException, TimeFrameMismatchException {
        Task timedTask = new Task(new Task.TaskBuilder(TASK_NAME).setDate(LocalDate.of(2024,2,16)));
        User user = new User(USERNAME,PASSWORD);
        user.getTimedTasksStorage().addTask(timedTask);

        when(usersStorage.getUser(USERNAME)).thenReturn(user);

        String[] args = { "name=<task>", "date=<16/02/2024>"};
        String result = getTaskCommand.execute(args, USERNAME);

        assertEquals(timedTask.toString(), result,
            "execute should return correct message when the task is found.");
    }

    @Test
    void testExecuteTimedTaskNotFound() {
        User user = new User(USERNAME,PASSWORD);

        when(usersStorage.getUser(USERNAME)).thenReturn(user);

        String[] args = { "name=<task>" , "date=<16/02/2024>"};

        assertEquals("The task was not found.", getTaskCommand.execute(args,USERNAME),
            "execute should return correct message when the task is not found.");
    }


}
