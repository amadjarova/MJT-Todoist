package bg.sofia.uni.fmi.mjt.todoist.command.user;

import bg.sofia.uni.fmi.mjt.todoist.exception.UsernameAlreadyTakenException;
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
public class RegisterCommandTest {
    @Mock
    UsersStorage usersStorage;

    @InjectMocks
    RegisterCommand registerCommand;

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"username=<username>", "password=<password>", "user=<user>"};
        assertEquals("Invalid arguments count", registerCommand.execute(args,null),
            "execute should return Invalid arguments count message when the arguments are not 2.");
    }

    @Test
    void testExecuteUserAlreadyLoggedIn() {
        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("You are already logged in.", registerCommand.execute(args, "username1"),
            "execute should return You are already logged in. first message when the user is logged int.");
    }

    @Test
    void testExecuteInvalidArguments() {
        String[] args = new String[] {"username=<username>", "pass=<password>"};
        assertEquals("Invalid arguments", registerCommand.execute(args,null),
            "execute should return Invalid arguments message when there is invalid argument");
    }

    @Test
    void testExecuteUsernameAlreadyTaken() throws UsernameAlreadyTakenException {
        doThrow(new UsernameAlreadyTakenException("There is another user with the same username."))
            .when(usersStorage)
            .addUser("username", "password");

        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("There is another user with the same username.", registerCommand.execute(args,null),
            "execute should return correct message when there ia another user with the same username.");
    }

    @Test
    void testExecuteValidData() throws UsernameAlreadyTakenException {
        doNothing().when(usersStorage).addUser("username", "password");

        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("Successful registration, user: username" , registerCommand.execute(args,null),
            "execute should return correct message when the registration is successful.");
    }
}
