package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

import java.time.LocalDate;

public class DeleteTaskCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final int MIN_ARGUMENTS_COUNT = 1;
    private static final int MAX_ARGUMENTS_COUNT = 2;

    private static final String PARAM_NAME = "name";
    private static final String REGEX = "=";
    private static final String PARAM_DATE = "date";

    public DeleteTaskCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        helper = new CommandParser();
    }

    private String deleteTimedTask(String[] args, String username) {
        String[] tokens1 = args[0].split(REGEX);
        String[] tokens2 = args[1].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_NAME, tokens1) ||
            !helper.isArgumentFormatCorrect(PARAM_DATE, tokens2)) {
            return "Invalid arguments.";
        }

        String name = helper.removeBracketsFromString(tokens1[1]);
        LocalDate date = helper.parseDate(helper.removeBracketsFromString(tokens2[1]));
        try {
            usersStorage.getUser(username).getTimedTasksStorage().deleteTask(date, name);
        } catch (TaskNotFoundException e) {
            return e.getMessage();
        }

        return "Task deleted successfully";
    }

    private String deleteInboxTask(String[] args, String username) {
        String[] tokens = args[0].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_NAME, tokens)) {
            return "Invalid arguments.";
        }

        String name = helper.removeBracketsFromString(tokens[1]);

        try {
            usersStorage.getUser(username).getInboxTaskStorage().deleteTask(name);
        } catch (TaskNotFoundException e) {
            return e.getMessage();
        }

        return "Task deleted successfully";
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length < MIN_ARGUMENTS_COUNT || args.length > MAX_ARGUMENTS_COUNT) {
            return "Invalid arguments count.";
        }

        if (args.length == MIN_ARGUMENTS_COUNT) {
            return deleteInboxTask(args, username);
        }

        return deleteTimedTask(args, username);
    }
}
