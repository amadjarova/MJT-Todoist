package bg.sofia.uni.fmi.mjt.todoist.command.user;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidPasswordException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
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

@ExtendWith(MockitoExtension.class)
public class LoginCommandTest {
    @Mock
    UsersStorage usersStorage;

    @InjectMocks
    LoginCommand loginCommand;

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"username=<username>", "password=<password>", "user=<user>"};
        assertEquals("Invalid arguments count.", loginCommand.execute(args,null),
            "execute should return Invalid arguments count message when the arguments are not 2.");
    }

    @Test
    void testExecuteUserAlreadyLoggedIn() {
        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("You are already logged in.", loginCommand.execute(args, "username1"),
            "execute should return You are already logged in. message when the user is logged in.");
    }

    @Test
    void testExecuteInvalidArguments() {
        String[] args = new String[] {"username=<username>", "pass=<password>"};
        assertEquals("Invalid arguments.", loginCommand.execute(args,null),
            "execute should return Invalid arguments message when there is invalid argument");
    }

    @Test
    void testExecuteUserNotFound() throws UserNotFoundException, InvalidPasswordException {
        doThrow(new UserNotFoundException("User with this username can not be found."))
            .when(usersStorage)
            .login("username", "password");

        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("User with this username can not be found.", loginCommand.execute(args,null),
            "execute should return correct message when the user is not found.");
    }

    @Test
    void testExecuteInvalidPassword() throws UserNotFoundException, InvalidPasswordException {
        doThrow(new UserNotFoundException("Invalid password."))
            .when(usersStorage)
            .login("username", "password");

        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("Invalid password.", loginCommand.execute(args,null),
            "execute should return correct message when the password is invalid.");
    }

    @Test
    void testExecuteValidData() throws UserNotFoundException, InvalidPasswordException{
        doNothing().when(usersStorage).login("username", "password");

        String[] args = new String[] {"username=<username>", "password=<password>"};
        assertEquals("Successful login, user: username" , loginCommand.execute(args,null),
            "execute should return correct message when the logging in is successful.");
    }
}
