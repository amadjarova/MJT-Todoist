package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

public class DisconnectCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CollaborationsStorage collaborationsStorage;

    private static final String COLLABORATIONS_FILE_NAME = "collaborations.dat";
    private static final String USERS_FILE_NAME = "users.dat";

    public DisconnectCommand(UsersStorage usersStorage, CollaborationsStorage collaborationsStorage) {
        this.usersStorage = usersStorage;
        this.collaborationsStorage = collaborationsStorage;
    }

    @Override
    public String execute(String[] args, String username) {
        if (args.length != 0) {
            return "Invalid arguments count.";
        }

        collaborationsStorage.save(COLLABORATIONS_FILE_NAME);
        usersStorage.save(USERS_FILE_NAME);

        return "Disconnected";
    }
}
