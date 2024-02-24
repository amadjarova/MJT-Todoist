package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCollaborationsCommandTest {
    @Mock
    private CollaborationsStorage collaborationsStorage;
    @Mock
    private Collaboration collaboration;
    @InjectMocks
    private ListCollaborationsCommand listCollaborationsCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {};
        assertEquals("Please login first.", listCollaborationsCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"collaboration=<name>"};
        assertEquals("Invalid arguments count.", listCollaborationsCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 0.");
    }

    @Test
    void testExecuteNoCollaborations() {
        when(collaborationsStorage.listCollaborationsOfUser(USERNAME)).thenReturn(Collections.emptyList());
        assertEquals("There are no collaborations.",listCollaborationsCommand.execute(new String[] {}, USERNAME),
            "execute should return correct message when there are no collaborations");
    }

    @Test
    void testExecuteCollaborationsFound() {
        when(collaborationsStorage.listCollaborationsOfUser(USERNAME)).thenReturn(List.of(collaboration, collaboration));
        when(collaboration.getName()).thenReturn("name");

        StringBuilder message = new StringBuilder("name").append(System.lineSeparator()).append("name").append(System.lineSeparator());

        assertEquals(message.toString(), listCollaborationsCommand.execute(new String[] {}, USERNAME),
            "execute should return correct list when collaborations are found");
    }

}
