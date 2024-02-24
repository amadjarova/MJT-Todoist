package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

public class AssignTaskCommand implements CommandBase {
    private final CollaborationsStorage collaborationsStorage;
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final String PARAM_USER = "user";
    private static final int ARGUMENTS_COUNT = 3;
    private static final String PARAM_COLLABORATION = "collaboration";
    private static final String REGEX = "=";
    private static final String PARAM_TASK = "task";

    public AssignTaskCommand(CollaborationsStorage collaborationsStorage, UsersStorage usersStorage) {
        this.collaborationsStorage = collaborationsStorage;
        this.helper = new CommandParser();
        this.usersStorage = usersStorage;
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length != ARGUMENTS_COUNT) {
            return "Invalid arguments count.";
        }

        String[] tokens1 = args[0].split(REGEX);
        String[] tokens2 = args[1].split(REGEX);
        String[] tokens3 = args[2].split(REGEX);
        if (!helper.isArgumentFormatCorrect(PARAM_COLLABORATION, tokens1) ||
            !helper.isArgumentFormatCorrect(PARAM_USER, tokens2) ||
            !helper.isArgumentFormatCorrect(PARAM_TASK, tokens3)) {
            return "Invalid command argument.";
        }
        String user = helper.removeBracketsFromString(tokens2[1]);
        String collaboration = helper.removeBracketsFromString(tokens1[1]);
        String task = helper.removeBracketsFromString(tokens3[1]);
        if (!usersStorage.containsUser(user)) {
            return "There is no user with this username.";
        }
        try {
            collaborationsStorage.getCollaboration(collaboration).assignTask(user, task);
        } catch (CollaborationNotFoundException | UserNotFoundException | TaskNotFoundException e) {
            return e.getMessage();
        }

        return "The task was assigned successfully.";
    }
}
