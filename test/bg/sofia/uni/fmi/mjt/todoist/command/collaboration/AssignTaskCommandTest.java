package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssignTaskCommandTest {
    @Mock
    private CollaborationsStorage collaborationsStorage;
    @Mock
    private UsersStorage usersStorage;
    @Mock
    private Collaboration collaboration;
    @InjectMocks
    private AssignTaskCommand assignTaskCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"collaboration=<collaboration>","user=<user>","task=<task>"};
        assertEquals("Please login first.", assignTaskCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"collaboration=<name>", "user=<user>"};
        assertEquals("Invalid arguments count.", assignTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 2.");
    }

    @Test
    void testExecuteInvalidCommandArgument() {
        String[] args = new String[] {"collaboration=<collaboration>","user=<user>","task1=<task>"};
        assertEquals("Invalid command argument.", assignTaskCommand.execute(args, USERNAME),
            "execute should return correct message when an argument is invalid.");
    }

    @Test
    void testExecuteUserNotFound() {
        when(usersStorage.containsUser("testUser")).thenReturn(false);
        String[] args = new String[] {"collaboration=<collaboration>","user=<testUser>","task=<task>"};
        assertEquals("There is no user with this username.", assignTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the user is not found.");
    }

    @Test
    void testExecuteTaskNotFound() throws CollaborationNotFoundException, UserNotFoundException, TaskNotFoundException {
        when(usersStorage.containsUser("testUser")).thenReturn(true);
        when(collaborationsStorage.getCollaboration("collaboration")).thenReturn(collaboration);

        doThrow(new TaskNotFoundException("task not found")).when(collaboration).assignTask("testUser", "task");

        String[] args = new String[] {"collaboration=<collaboration>","user=<testUser>","task=<task>"};

        assertEquals("task not found", assignTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the task is not found.");

    }

    @Test
    void testExecuteAssignTaskSuccessfully() throws CollaborationNotFoundException, UserNotFoundException, TaskNotFoundException{
        when(usersStorage.containsUser("testUser")).thenReturn(true);
        when(collaborationsStorage.getCollaboration("collaboration")).thenReturn(collaboration);

        doNothing().when(collaboration).assignTask("testUser","task");

        String[] args = new String[] {"collaboration=<collaboration>","user=<testUser>","task=<task>"};

        assertEquals("The task was assigned successfully.", assignTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the task is assigned successfully.");
    }
}
