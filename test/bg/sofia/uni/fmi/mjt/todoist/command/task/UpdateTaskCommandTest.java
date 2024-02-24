package bg.sofia.uni.fmi.mjt.todoist.command.task;


import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UpdateTaskCommandTest {
    @Mock
    private UsersStorage usersStorage;

    @InjectMocks
    private UpdateTaskCommand updateTaskCommand;
    private static final String USERNAME = "username";
    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {"name=<name>"};
        assertEquals("Please login first.", updateTaskCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {};
        assertEquals("Invalid arguments count", updateTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not from 1 to 4.");
    }

    @Test
    void testExecuteInvalidArgument() {
        String[] args = new String[] {"name1=<name>"};
        assertEquals("First param should be name=<name>", updateTaskCommand.execute(args, USERNAME),
            "execute should return correct message when the argument is not correct.");
    }
}
