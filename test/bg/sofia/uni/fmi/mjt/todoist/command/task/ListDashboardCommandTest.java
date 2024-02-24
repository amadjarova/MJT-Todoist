package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.storage.TimedTasksStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.user.User;
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
public class ListDashboardCommandTest {
    @Mock
    private UsersStorage usersStorage;

    @Mock
    private TimedTasksStorage timedTasksStorage;

    @Mock
    private User user;

    @Mock
    private Task task;

    @InjectMocks
    private ListDashboardCommand listDashboardCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {};
        assertEquals("Please login first.", listDashboardCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"name=<name>", "task=<task>", "date=<date>"};
        assertEquals("Invalid arguments count.", listDashboardCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 0.");
    }

    @Test
    void testExecuteEmptyDashboard() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        when(user.getTimedTasksStorage()).thenReturn(timedTasksStorage);
        when(timedTasksStorage.listDashboard()).thenReturn(Collections.emptyList());
        String[] args = new String[] {};
        assertEquals("There are no tasks.", listDashboardCommand.execute(args, USERNAME),
            "execute should return correct message when there are no tasks");
    }

    @Test
    void testExecuteTasksFound() {
        when(usersStorage.getUser(USERNAME)).thenReturn(user);
        when(user.getTimedTasksStorage()).thenReturn(timedTasksStorage);
        when(timedTasksStorage.listDashboard()).thenReturn(List.of(task, task));
        when(task.toString()).thenReturn("name-name date-17.02.2044");

        String[] args = new String[] {};
        StringBuilder message = new StringBuilder("name-name date-17.02.2044").append(System.lineSeparator())
            .append("name-name date-17.02.2044").append(System.lineSeparator());

        assertEquals(message.toString(), listDashboardCommand.execute(args, USERNAME),
            "execute should return correct list of tasks");
    }


}
