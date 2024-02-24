package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.time.format.DateTimeParseException;

public class UpdateTaskCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final int MIN_ARGS_COUNT = 1;
    private static final int MAX_ARGS_COUNT = 4;
    public UpdateTaskCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.helper = new CommandParser();
    }

    private String updateUsersTask(Task task, String username) {
        if (task.getDate() != null) {
            try {
                usersStorage.getUser(username).getTimedTasksStorage().updateTask(task);
            } catch (TaskNotFoundException e) {
                return e.getMessage();
            }
            return "Task updated successfully.";
        }
        try {
            usersStorage.getUser(username).getInboxTaskStorage().updateTask(task);
        } catch (TaskNotFoundException e) {
            return e.getMessage();
        }
        return "Task updated successfully.";
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length < MIN_ARGS_COUNT || args.length > MAX_ARGS_COUNT) {
            return "Invalid arguments count";
        }

        Task task;
        try {
            task = helper.constructTask(args);
        } catch (InvalidCommandException | TimeFrameMismatchException e) {
            return e.getMessage();
        } catch (DateTimeParseException e) {
            return "Invalid command. Invalid date format. Valid date format: dd/MM/yyyy";
        }
        return updateUsersTask(task, username);
    }
}
