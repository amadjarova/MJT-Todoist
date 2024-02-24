package bg.sofia.uni.fmi.mjt.todoist.command.user;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidPasswordException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

public class LoginCommand implements CommandBase {
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String REGEX = "=";
    UsersStorage usersStorage;
    CommandParser helper;

    public LoginCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.helper = new CommandParser();
    }

    @Override
    public String execute(String[] args, String username) {
        if (helper.isUserLoggedIn(username)) {
            return "You are already logged in.";
        }
        if (args.length != 2) {
            return "Invalid arguments count.";
        }
        String[] tokens1 = args[0].split(REGEX);
        String[] tokens2 = args[1].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_USERNAME, tokens1) ||
            !helper.isArgumentFormatCorrect(PARAM_PASSWORD, tokens2)) {
            return "Invalid arguments.";
        }

        String username1 = helper.removeBracketsFromString(tokens1[1]);
        String password = helper.removeBracketsFromString(tokens2[1]);

        try {
            usersStorage.login(username1, password);
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return e.getMessage();
        }

        return String.format("Successful login, user: %s", username1);
    }
}
