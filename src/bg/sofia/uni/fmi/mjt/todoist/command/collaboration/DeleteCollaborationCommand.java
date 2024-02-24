package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotCreatorException;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;

public class DeleteCollaborationCommand implements CommandBase {
    private final CollaborationsStorage collaborationsStorage;
    private final CommandParser helper;
    private static final String PARAM_NAME = "name";
    private static final String REGEX = "=";

    public DeleteCollaborationCommand(CollaborationsStorage collaborationsStorage) {
        this.collaborationsStorage = collaborationsStorage;
        this.helper = new CommandParser();
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

        if (!helper.isArgumentFormatCorrect(PARAM_NAME, tokens)) {
            return "Invalid command argument.";
        }

        String collaborationName = helper.removeBracketsFromString(tokens[1]);

        try {
            collaborationsStorage.deleteCollaboration(collaborationName, username);
        } catch (CollaborationNotFoundException | UserNotCreatorException e) {
            return e.getMessage();
        }

        return "Collaboration deleted successfully.";
    }
}
