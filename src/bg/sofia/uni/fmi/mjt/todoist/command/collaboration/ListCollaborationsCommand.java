package bg.sofia.uni.fmi.mjt.todoist.command.collaboration;

import bg.sofia.uni.fmi.mjt.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;

import java.util.Collection;

public class ListCollaborationsCommand implements CommandBase {
    private final CollaborationsStorage collaborationsStorage;
    private final CommandParser helper;
    private static final int ARGUMENTS_COUNT = 0;

    public ListCollaborationsCommand(CollaborationsStorage collaborationsStorage) {
        this.collaborationsStorage = collaborationsStorage;
        this.helper = new CommandParser();
    }

    private String collaborationsToString(Collection<Collaboration> collaborations) {
        if (collaborations.isEmpty()) {
            return "There are no collaborations.";
        }
        StringBuilder str = new StringBuilder();

        for (Collaboration collaboration : collaborations) {
            str.append(collaboration.getName()).append(System.lineSeparator());
        }

        return str.toString();
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length != ARGUMENTS_COUNT) {
            return "Invalid arguments count.";
        }

        return collaborationsToString(collaborationsStorage.listCollaborationsOfUser(username));
    }
}
