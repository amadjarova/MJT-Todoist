package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserAlreadyInCollaborationException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

public class AddUserCommand implements CommandBase {
    private UsersStorage usersStorage;
    private CollaborationsStorage collaborationsStorage;
    private CommandParser helper;
    private static final String PARAM_USER = "user";
    private static final String PARAM_COLLABORATION = "collaboration";
    private static final String REGEX = "=";
    private static final int ARGUMENTS_COUNT = 2;

    public AddUserCommand(CollaborationsStorage collaborationsStorage, UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.collaborationsStorage = collaborationsStorage;
        this.helper = new CommandParser();
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
        if (!helper.isArgumentFormatCorrect(PARAM_COLLABORATION, tokens1) ||
            !helper.isArgumentFormatCorrect(PARAM_USER, tokens2)) {
            return "Invalid command argument.";
        }

        String user = helper.removeBracketsFromString(tokens2[1]);
        String collaboration = helper.removeBracketsFromString(tokens1[1]);

        if (!usersStorage.containsUser(user)) {
            return "There is no user with this username.";
        }

        try {
            collaborationsStorage.addUserToCollaboration(user, collaboration);
        } catch (CollaborationNotFoundException | UserAlreadyInCollaborationException e) {
            return e.getMessage();
        }

        return "User added successfully";
    }
}
