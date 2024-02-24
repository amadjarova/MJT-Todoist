package bg.sofia.uni.fmi.mjt.todoist.user;

import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.InboxTaskStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.TimedTasksStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private User user = new User("abc", "bcd");

    @Test
    void testIsPasswordCorrectNullPassword() {
        assertThrows(IllegalArgumentException.class, () ->user.isPasswordCorrect(null),
            "isPasswordCorrect should throw IllegalArgumentException when password is null");
    }

    @Test
    void testIsPasswordCorrectTrue() {
        assertTrue(user.isPasswordCorrect("bcd"), "isPasswordCorrect should return true when the password is correct.");
    }

    @Test
    void testIsPasswordCorrectFalse() {
        assertFalse(user.isPasswordCorrect("fff"), "isPasswordCorrect should return false when the password is incorrect.");
    }




}
