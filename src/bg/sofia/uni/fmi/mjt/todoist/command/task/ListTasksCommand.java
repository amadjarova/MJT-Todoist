package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;

public class ListTasksCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final String PARAM_COMPLETED = "completed=true";
    private static final String REGEX = "=";
    private static final String PARAM_DATE = "date";
    private static final int MIN_ARGS_COUNT = 0;
    private static final int MAX_ARGS_COUNT = 1;

    public ListTasksCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.helper = new CommandParser();
    }

    private String tasksToString(Collection<Task> tasks) {
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
        if (args.length > MAX_ARGS_COUNT) {
            return "Invalid arguments count.";
        }
        User user = usersStorage.getUser(username);

        if (args.length == MIN_ARGS_COUNT) {
            return tasksToString(user.getInboxTaskStorage().listTasks());
        }

        String[] tokens = args[0].split(REGEX);
        if (helper.isArgumentFormatCorrect(PARAM_DATE, tokens)) {
            LocalDate date;
            try {
                date = helper.parseDate(helper.removeBracketsFromString(tokens[1]));
            } catch (DateTimeParseException e) {
                return "Invalid command. Invalid date format. Valid date format: dd/MM/yyyy";
            }
            return tasksToString(user.getTimedTasksStorage().listTasks(date));

        } else if (args[0].equals(PARAM_COMPLETED)) {
            return tasksToString(user.getInboxTaskStorage().listCompletedTasks());
        }

        return "Invalid command arguments.";
    }
}
