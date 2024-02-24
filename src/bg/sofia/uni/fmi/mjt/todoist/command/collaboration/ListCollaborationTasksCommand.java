package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.CollaborationTask;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.util.Collection;
import java.util.List;

public class ListCollaborationTasksCommand implements CommandBase {
    private final CollaborationsStorage collaborationsStorage;
    private final CommandParser helper;
    private static final int ARGUMENTS_COUNT = 1;
    private static final String REGEX = "=";
    private static final String PARAM_COLLABORATION = "collaboration";

    public ListCollaborationTasksCommand(CollaborationsStorage collaborationsStorage) {
        this.collaborationsStorage = collaborationsStorage;
        this.helper = new CommandParser();
    }

    private String tasksToString(Collection<? extends Task> tasks) {
        if (tasks.isEmpty()) {
            return "There are no tasks.";
        }

        StringBuilder strTasks = new StringBuilder();

        for (Task task : tasks) {
            strTasks.append(task.toString()).append(System.lineSeparator());
        }

        return strTasks.toString();
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length != ARGUMENTS_COUNT) {
            return "Invalid arguments count.";
        }

        String[] tokens = args[0].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_COLLABORATION, tokens)) {
            return "Invalid command argument.";
        }

        String collaboration = helper.removeBracketsFromString(tokens[1]);
        List<CollaborationTask> tasks;
        try {
            tasks = collaborationsStorage.getCollaboration(collaboration).listTasks();
        } catch (CollaborationNotFoundException e) {
            return e.getMessage();
        }

        return tasksToString(tasks);
    }
}
