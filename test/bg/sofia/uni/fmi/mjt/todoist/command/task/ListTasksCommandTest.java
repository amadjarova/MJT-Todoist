package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.storage.InboxTaskStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.TimedTasksStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListTasksCommandTest {
    @Mock
    private UsersStorage usersStorage;

    @Mock
    private TimedTasksStorage timedTasksStorage;
    @Mock
    private InboxTaskStorage inboxTaskStorage;
    @Mock
    private User user;

    @Mock
    private Task task;

    @InjectMocks
    private ListTasksCommand listTasksCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {};
        assertEquals("Please login first.", listTasksCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"name=<name>", "task=<task>", "date=<date>"};
        assertEquals("Invalid arguments count.", listTasksCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is from 0 to 1.");
    }

    @Test
    void testListTasksEmptyInbox() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        when(user.getInboxTaskStorage()).thenReturn(inboxTaskStorage);
        when(inboxTaskStorage.listTasks()).thenReturn(Collections.emptyList());

        String[] args = new String[] {};
        assertEquals("There are no tasks.", listTasksCommand.execute(args, USERNAME),
            "execute should return correct message when there are no tasks");
    }

    @Test
    void testExecuteListTimedTasks() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        when(user.getTimedTasksStorage()).thenReturn(timedTasksStorage);
        when(timedTasksStorage.listTasks(LocalDate.of(2044, 2, 17))).thenReturn(List.of(task, task));
        when(task.toString()).thenReturn("name-name date-17.02.2044");

        String[] args = new String[] {"date=<17/02/2044>"};
        StringBuilder message = new StringBuilder("name-name date-17.02.2044").append(System.lineSeparator())
            .append("name-name date-17.02.2044").append(System.lineSeparator());

        assertEquals(message.toString(), listTasksCommand.execute(args, USERNAME),
            "execute should return correct list of tasks");
    }

    @Test
    void testExecuteListTimedTasksInvalidDate() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);

        String[] args = new String[] {"date=<nz/02/2044>"};

        assertEquals("Invalid command. Invalid date format. Valid date format: dd/MM/yyyy",
            listTasksCommand.execute(args, USERNAME),
            "execute should return correct message when date format is not correct");
    }

    @Test
    void testListCompletedTasks() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        when(user.getInboxTaskStorage()).thenReturn(inboxTaskStorage);
        when(task.toString()).thenReturn("name-name completed=true");
        when(inboxTaskStorage.listCompletedTasks()).thenReturn(List.of(task, task));
        String[] args = new String[] {"completed=true"};
        StringBuilder message = new StringBuilder("name-name completed=true").append(System.lineSeparator())
            .append("name-name completed=true").append(System.lineSeparator());

        assertEquals(message.toString(), listTasksCommand.execute(args, USERNAME),
            "execute should return correct list of tasks");
    }

    @Test
    void testExecuteInvalidArguments() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        String[] args = new String[] {"name=az"};
        assertEquals("Invalid command arguments.", listTasksCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments are incorrect");
    }

}
