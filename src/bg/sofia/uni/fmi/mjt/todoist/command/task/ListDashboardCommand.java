package bg.sofia.uni.fmi.mjt.todoist.command.task;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandParser;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;
import bg.sofia.uni.fmi.mjt.todoist.user.User;

import java.util.Collection;

public class ListDashboardCommand implements CommandBase {
    private final UsersStorage usersStorage;
    private final CommandParser helper;
    private static final int ARGUMENTS_COUNT = 0;

    public ListDashboardCommand(UsersStorage usersStorage) {
        this.helper = new CommandParser();
        this.usersStorage = usersStorage;
    }

    private String tasksToString(Collection<Task> tasks) {
        if (tasks.isEmpty()) {
            return "There are no tasks.";
        }

        StringBuilder strTasks = new StringBuilder();

        for (Task task : tasks) {
            strTasks.append(task.toString()).append(System.lineSeparator());
        }

        return strTasks.toString();
    }

    @Override
    public String execute(String[] args, String username) {
        if (!helper.isUserLoggedIn(username)) {
            return "Please login first.";
        }
        if (args.length != ARGUMENTS_COUNT) {
            return "Invalid arguments count.";
        }

        User user = usersStorage.getUser(username);

        return tasksToString(user.getTimedTasksStorage().listDashboard());
    }
}
