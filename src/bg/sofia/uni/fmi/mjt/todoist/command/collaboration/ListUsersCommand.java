package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;

import java.util.Collection;
import java.util.Set;

public class ListUsersCommand implements CommandBase {
    private final CollaborationsStorage collaborationsStorage;
    private final CommandParser helper;
    private static final String REGEX = "=";
    private static final String PARAM_COLLABORATION = "collaboration";

    public ListUsersCommand(CollaborationsStorage collaborationsStorage) {
        this.collaborationsStorage = collaborationsStorage;
        this.helper = new CommandParser();
    }

    private String usernamesToString(Collection<String> users) {
        if (users.isEmpty()) {
            return "There are no users in this collaboration.";
        }

        StringBuilder usersStr = new StringBuilder();
        for (String user : users) {
            usersStr.append(user).append(System.lineSeparator());
        }

        return usersStr.toString();
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length != 1) {
            return "Invalid arguments count.";
        }

        String[] tokens = args[0].split(REGEX);

        if (!helper.isArgumentFormatCorrect(PARAM_COLLABORATION, tokens)) {
            return "Invalid command argument.";
        }

        String collaboration = helper.removeBracketsFromString(tokens[1]);
        Set<String> users;

        try {
            users = collaborationsStorage.getCollaboration(collaboration).getParticipants();
        } catch (CollaborationNotFoundException e) {
            return e.getMessage();
        }

        return usernamesToString(users);
    }
}
