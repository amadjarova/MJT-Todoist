package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidPasswordException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UsernameAlreadyTakenException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersStorageTest {
    private static UsersStorage usersStorage = new UsersStorage();

    @BeforeAll
    static void setUpTestCase() throws UsernameAlreadyTakenException {
        usersStorage.addUser("user1", "password1");
        usersStorage.addUser("user2", "password2");
    }

    @Test
    void testLoginInvalidPassword() {
        assertThrows(InvalidPasswordException.class,()-> usersStorage.login("user1", "inv"),
            "login should throw InvalidPasswordException when the password is invalid");
    }

    @Test
    void testSaveAndReadFromFile() throws UsernameAlreadyTakenException {
        String fileName = "testUsersStorage.dat";
        usersStorage.save(fileName);

        UsersStorage newUsersStorage = new UsersStorage();
        newUsersStorage.read(fileName);

        assertTrue(usersStorage.containsUser("user1"),
            "users storage should contain the same users after reading from file");
        assertTrue(usersStorage.containsUser("user2"),
            "users storage should contain the same users after reading from file");
        assertTrue(usersStorage.getUser("user1").isPasswordCorrect("password1"),
            "users storage should contain the same users after reading from file");
        assertTrue(usersStorage.getUser("user2").isPasswordCorrect("password2"),
            "users storage should contain the same users after reading from file");
    }



}
