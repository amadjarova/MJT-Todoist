package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TaskAlreadyExistException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.time.format.DateTimeParseException;

public class AddTaskCommand implements CommandBase {
    private UsersStorage usersStorage;
    private CommandParser helper;
    private static final int MIN_ARGS_COUNT = 1;
    private static final int MAX_ARGS_COUNT = 4;

    public AddTaskCommand(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
        this.helper = new CommandParser();
    }

    private String addTaskToUser(Task task, String username) {
        try {
            if (task.getDate() != null) {
                usersStorage.getUser(username).getTimedTasksStorage().addTask(task);
            } else {
                usersStorage.getUser(username).getInboxTaskStorage().addTask(task);
            }
        } catch (TaskAlreadyExistException e) {
            return e.getMessage();
        }
        return String.format("Added new task for user %s", username);
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
        return addTaskToUser(task, username);
    }
}
