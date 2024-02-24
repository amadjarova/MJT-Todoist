package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.InboxTaskStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteTaskCommandTest {
    @Mock
    private UsersStorage usersStorage;

    @Mock
    private User user;

    @Mock
    private InboxTaskStorage inboxTaskStorage;

    @InjectMocks
    private DeleteTaskCommand deleteTaskCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"name=<name>"};
        assertEquals("Please login first.", deleteTaskCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"name=<name>", "task=<task>", "date=<date>"};
        assertEquals("Invalid arguments count.", deleteTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 1 or 2.");
    }

    @Test
    void testExecuteInvalidArgument() {
        String[] args = new String[] {"name1=<name>"};
        assertEquals("Invalid arguments.", deleteTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the argument is not correct.");
    }

    @Test
    void testExecuteTaskNotFound() throws TaskNotFoundException{
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        when(user.getInboxTaskStorage()).thenReturn(inboxTaskStorage);
        doThrow(new TaskNotFoundException("The task was not found")).when(inboxTaskStorage).deleteTask("task");
        String[] args = new String[] {"name=<task>"};
        assertEquals("The task was not found", deleteTaskCommand.execute(args,USERNAME),
            "execute should return correct message when the task is not found");
    }

}
