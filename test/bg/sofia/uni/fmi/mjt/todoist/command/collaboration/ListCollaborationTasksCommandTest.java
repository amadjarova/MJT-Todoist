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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCollaborationTasksCommandTest {
    @Mock
    private CollaborationsStorage collaborationsStorage;
    @Mock
    private Collaboration collaboration;
    @Mock
    private CollaborationTask task;
    @InjectMocks
    private ListCollaborationTasksCommand listCollaborationTasksCommand;
    private static final String USERNAME = "username";

    @Test
    void testExecuteUserNotLoggedIn() {
        String[] args = new String[] {};
        assertEquals("Please login first.", listCollaborationTasksCommand.execute(args, null),
            "execute should return correct message when the user is not logged in.");
    }

    @Test
    void testExecuteInvalidArgumentsCount() {
        String[] args = new String[] {"collaboration=<name>", "user=<name>"};
        assertEquals("Invalid arguments count.", listCollaborationTasksCommand.execute(args, USERNAME),
            "execute should return correct message when the arguments count is not 1.");
    }

    @Test
    void testExecuteInvalidCommandArgument() {
        String[] args = new String[] {"collaborations=<collaboration>"};
        assertEquals("Invalid command argument.", listCollaborationTasksCommand.execute(args, USERNAME),
            "execute should return correct message when an argument is invalid.");
    }

    @Test
    void testExecuteCollaborationNotFound() throws CollaborationNotFoundException {
        when(collaborationsStorage.getCollaboration("collaboration")).thenThrow(
            new CollaborationNotFoundException("collaboration not found"));

        assertEquals("collaboration not found",
            listCollaborationTasksCommand.execute(new String[] {"collaboration=<collaboration>"}, USERNAME),
            "execute should return correct message when the collaboration is not found");

    }

    @Test
    void testExecuteNoTasks() throws CollaborationNotFoundException {
        when(collaborationsStorage.getCollaboration("collaboration")).thenReturn(collaboration);
        when(collaboration.listTasks()).thenReturn(Collections.emptyList());
        assertEquals("There are no tasks.",
            listCollaborationTasksCommand.execute(new String[] {"collaboration=<collaboration>"}, USERNAME),
            "execute should return correct message when there are no tasks");
    }

    @Test
    void testExecuteTasksFound() throws CollaborationNotFoundException {
        when(collaborationsStorage.getCollaboration("collaboration")).thenReturn(collaboration);
        when(collaboration.listTasks()).thenReturn(List.of(task, task));
        when(task.toString()).thenReturn("name-task");

        StringBuilder message = new StringBuilder("name-task").append(System.lineSeparator()).append("name-task")
            .append(System.lineSeparator());

        assertEquals(message.toString(),
            listCollaborationTasksCommand.execute(new String[] {"collaboration=<collaboration>"}, USERNAME),
            "execute should return correct tasks list");
    }
}
