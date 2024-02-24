package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.CollaborationTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListUsersCommandTest {
    @Mock
    private CollaborationsStorage collaborationsStorage;
    @Mock
    private Collaboration collaboration;
    @InjectMocks
    private ListUsersCommand listUsersCommand;

    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {};
        assertEquals("Please login first.", listUsersCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"collaboration=<name>", "user=<name>"};
        assertEquals("Invalid arguments count.", listUsersCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 1.");
    }

    @Test
    void testExecuteInvalidCommandArgument() {
        String[] args = new String[] {"collaborations=<collaboration>"};
        assertEquals("Invalid command argument.", listUsersCommand.execute(args, USERNAME),
            "execute should return correct message when an argument is invalid.");
    }

    @Test
    void testExecuteCollaborationNotFound() throws CollaborationNotFoundException {
        when(collaborationsStorage.getCollaboration("collaboration")).thenThrow(
            new CollaborationNotFoundException("collaboration not found"));

        assertEquals("collaboration not found",
            listUsersCommand.execute(new String[] {"collaboration=<collaboration>"}, USERNAME),
            "execute should return correct message when the collaboration is not found");

    }

    @Test
    void testExecuteNoParticipants() throws CollaborationNotFoundException {
        when(collaborationsStorage.getCollaboration("collaboration")).thenReturn(collaboration);
        when(collaboration.getParticipants()).thenReturn(Collections.emptySet());
        assertEquals("There are no users in this collaboration.",
            listUsersCommand.execute(new String[] {"collaboration=<collaboration>"}, USERNAME),
            "execute should return correct message when there are no participants"
        );
    }

    @Test
    void testExecuteParticipantsFound() throws CollaborationNotFoundException {
        when(collaborationsStorage.getCollaboration("collaboration")).thenReturn(collaboration);
        when(collaboration.getParticipants()).thenReturn(Set.of("a"));

        StringBuilder message = new StringBuilder("a").append(System.lineSeparator());

        assertEquals(message.toString(),listUsersCommand.execute(new String[] {"collaboration=<collaboration>"}, USERNAME),
            "execute should return correct list of participants");
    }

}
