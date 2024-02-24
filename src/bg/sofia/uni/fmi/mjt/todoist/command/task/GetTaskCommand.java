package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.time.LocalDate;

public class GetTaskCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final String PARAM_NAME = "name";
    private static final String REGEX = "=";
    private static final String PARAM_DATE = "date";

    public GetTaskCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.helper = new CommandParser();
    }

    private String getTimedTask(String[] args, String user) {
        String[] tokens1 = args[0].split(REGEX);
        String[] tokens2 = args[1].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_NAME, tokens1) ||
            !helper.isArgumentFormatCorrect(PARAM_DATE, tokens2)) {
            return "Invalid arguments.";
        }

        String name = helper.removeBracketsFromString(tokens1[1]);
        LocalDate date = helper.parseDate(helper.removeBracketsFromString(tokens2[1]));
        Task task;
        try {
            task = usersStorage.getUser(user).getTimedTasksStorage().getTask(name, date);
        } catch (TaskNotFoundException e) {
            return e.getMessage();
        }
        return task.toString();
    }

    private String getInboxTask(String[] args, String user) {
        String[] tokens = args[0].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_NAME, tokens)) {
            return "Invalid arguments.";
        }

        String name = helper.removeBracketsFromString(tokens[1]);
        Task task;
        try {
            task = usersStorage.getUser(user).getInboxTaskStorage().getTask(name);
        } catch (TaskNotFoundException e) {
            return e.getMessage();
        }
        return task.toString();
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please log in first.";
        }
        if (args.length < 1 || args.length > 2) {
            return "Invalid arguments count.";
        }
        if (args.length == 1) {
            return getInboxTask(args, username);
        }

        return getTimedTask(args, username);
    }
}
