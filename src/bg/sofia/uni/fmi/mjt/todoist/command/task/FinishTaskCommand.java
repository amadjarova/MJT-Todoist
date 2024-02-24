package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.user.User;

public class FinishTaskCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final String PARAM_NAME = "name";
    private static final String REGEX = "=";

    public FinishTaskCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.helper = new CommandParser();
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please log in first.";
        }
        if (args.length != 1) {
            return "Invalid arguments count.";
        }

        String[] tokens = args[0].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_NAME, tokens)) {
            return "Invalid command argument.";
        }

        String taskName = helper.removeBracketsFromString(tokens[1]);
        User user = usersStorage.getUser(username);
        try {
            user.getInboxTaskStorage().finishTask(taskName);
        } catch (TaskNotFoundException e) {
            return e.getMessage();
        }

        return String.format("You just finished the task : %s.", taskName);
    }
}
