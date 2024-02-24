package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNameAlreadyTakenException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class AddCollaborationCommandTest {
    @Mock
    private CollaborationsStorage collaborationsStorage;

    @InjectMocks
    private AddCollaborationCommand addCollaborationCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"name=<name>"};
        assertEquals("Please login first.", addCollaborationCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"name=<name>", "task=<task>", "date=<date>"};
        assertEquals("Invalid arguments count.", addCollaborationCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 1.");
    }

    @Test
    void testExecuteInvalidCommandArgument() {
        String[] args = new String[] {"date=<date>"};
        assertEquals("Invalid command argument.", addCollaborationCommand.execute(args, USERNAME),
            "execute should return correct message when the argument is invalid.");
    }

    @Test
    void testExecuteAddCollaborationSuccessfully() throws CollaborationNameAlreadyTakenException {
        doNothing().when(collaborationsStorage).addCollaboration("name", USERNAME);
        String[] args = new String[] {"name=<name>"};
        assertEquals("Collaboration added successfully.", addCollaborationCommand.execute(args,USERNAME),
            "execute should return correct message when collaboration is added successfully");
    }

    @Test
    void testExecuteAnotherCollaborationWithTheSameName() throws CollaborationNameAlreadyTakenException{
        doThrow(new CollaborationNameAlreadyTakenException("There is another collaboration with the same name."))
            .when(collaborationsStorage).addCollaboration("name",USERNAME);

        String[] args = new String[] {"name=<name>"};
        assertEquals("There is another collaboration with the same name.", addCollaborationCommand.execute(args,USERNAME));
    }
}
