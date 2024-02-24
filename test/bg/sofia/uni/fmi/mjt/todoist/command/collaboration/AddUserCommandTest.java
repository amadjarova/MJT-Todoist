package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserAlreadyInCollaborationException;
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
public class AddUserCommandTest {
    @Mock
    private CollaborationsStorage collaborationsStorage;
    @Mock
    private UsersStorage usersStorage;
    @InjectMocks
    private AddUserCommand addUserCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"name=<name>"};
        assertEquals("Please login first.", addUserCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"collaboration=<name>", "user=<user>", "date=<date>"};
        assertEquals("Invalid arguments count.", addUserCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 2.");
    }

    @Test
    void testExecuteInvalidCommandArgument() {
        String[] args = new String[] {"collaboration=<date>", "name=<name>"};
        assertEquals("Invalid command argument.", addUserCommand.execute(args, USERNAME),
            "execute should return correct message when an argument is invalid.");
    }

    @Test
    void testExecuteUserNotFound() {
        when(usersStorage.containsUser("testUser")).thenReturn(false);
        String[] args = new String[] {"collaboration=<date>", "user=<testUser>"};
        assertEquals("There is no user with this username.", addUserCommand.execute(args, USERNAME),
            "execute should return correct message when the user is not found.");
    }

    @Test
    void testExecuteCollaborationNotFound() throws CollaborationNotFoundException, UserAlreadyInCollaborationException {
        when(usersStorage.containsUser("testUser")).thenReturn(true);
        doThrow(new CollaborationNotFoundException("collaboration not found")).when(collaborationsStorage)
            .addUserToCollaboration("testUser", "date");
        String[] args = new String[] {"collaboration=<date>", "user=<testUser>"};
        assertEquals("collaboration not found", addUserCommand.execute(args, USERNAME),
            "execute should return correct message when the collaboration is not found.");
    }

    @Test
    void testExecuteUserAddedSuccessfully() throws CollaborationNotFoundException,UserAlreadyInCollaborationException{
        when(usersStorage.containsUser("testUser")).thenReturn(true);
        doNothing().when(collaborationsStorage)
            .addUserToCollaboration("testUser", "date");

        String[] args = new String[] {"collaboration=<date>", "user=<testUser>"};

        assertEquals("User added successfully", addUserCommand.execute(args, USERNAME),
            "execute should return correct message when the user is added successfully.");
    }
}
