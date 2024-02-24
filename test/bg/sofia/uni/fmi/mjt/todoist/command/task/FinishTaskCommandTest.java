package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinishTaskCommandTest {
    @Mock
    private UsersStorage usersStorage;

    @InjectMocks
    private FinishTaskCommand finishTaskCommand;

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"name=<name>"};
        assertEquals("Please log in first.", finishTaskCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"name=<name>", "task=<task>"};
        assertEquals("Invalid arguments count.", finishTaskCommand.execute(args, "az"),
            "execute should return correct message when the arguments count is not 1.");
    }

    @Test
    void testExecuteInvalidArgument() {
        String[] args = new String[] {"name1=<name>"};
        assertEquals("Invalid command argument.", finishTaskCommand.execute(args, "az"),
            "execute should return correct message when the argument is not correct.");
    }

    @Test
    void testExecuteTaskNotFound() {
        String username = "testUser";

        User user = new User(username, "pass");

        when(usersStorage.getUser(username)).thenReturn(user);

        String[] args = {"name=<task>"};

        assertEquals("There is no task with this name.", finishTaskCommand.execute(args, username),
            "execute should return correct message when the task is not found");
    }

    @Test
    void testExecuteFinishTaskSuccessfully() throws TaskAlreadyExistException {
        User user = new User("testUser", "pass");
        Task task = new Task(new Task.TaskBuilder("task"));
        user.getInboxTaskStorage().addTask(task);

        when(usersStorage.getUser("testUser")).thenReturn(user);

        String[] args = { "name=<task>"};
        String result = finishTaskCommand.execute(args, "testUser");

        assertEquals("You just finished the task : task.", result,
            "execute should return correct message when the task is finished successfully");
    }

}
