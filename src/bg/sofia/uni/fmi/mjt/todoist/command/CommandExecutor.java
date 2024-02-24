package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.AddCollaborationCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.AddUserCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.AssignTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.DeleteCollaborationCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.ListCollaborationTasksCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.ListCollaborationsCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.collaboration.ListUsersCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.help.HelpCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.AddTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.DeleteTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.FinishTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.GetTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.ListDashboardCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.ListTasksCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.task.UpdateTaskCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.user.LoginCommand;
import bg.sofia.uni.fmi.mjt.todoist.command.user.RegisterCommand;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private final Map<String, CommandBase> commands;
    private final UsersStorage usersStorage;
    private final CollaborationsStorage collaborationsStorage;
    private static final String HELP = "help";
    private static final String LOGIN = "login";
    private static final String REGISTER = "register";
    private static final String ADD_TASK = "add-task";
    private static final String GET_TASK = "get-task";

    private static final String UPDATE_TASK = "update-task";
    private static final String DELETE_TASK = "delete-task";
    private static final String LIST_TASKS = "list-tasks";
    private static final String LIST_DASHBOARD = "list-dashboard";
    private static final String FINISH_TASK = "finish-task";
    private static final String ADD_COLLABORATION = "add-collaboration";
    private static final String DELETE_COLLABORATION = "delete-collaboration";
    private static final String LIST_COLLABORATIONS = "list-collaborations";
    private static final String ADD_USER = "add-user";
    private static final String ASSIGN_TASKS = "assign-task";
    private static final String LIST_COLLABORATION_TASKS = "list-collaboration-tasks";
    private static final String DISCONNECT = "disconnect";
    private static final String LIST_USERS = "list-users";
    private static final String INVALID_COMMAND = "Invalid command";

    public CommandExecutor(UsersStorage usersStorage, CollaborationsStorage collaborationsStorage) {
        this.usersStorage = usersStorage;
        this.collaborationsStorage = collaborationsStorage;

        commands = new HashMap<>();
        commands.put(REGISTER, new RegisterCommand(usersStorage));
        commands.put(UPDATE_TASK, new UpdateTaskCommand(usersStorage));
        commands.put(LOGIN, new LoginCommand(usersStorage));
        commands.put(HELP, new HelpCommand());
        commands.put(ADD_TASK, new AddTaskCommand(usersStorage));
        commands.put(GET_TASK, new GetTaskCommand(usersStorage));
        commands.put(DELETE_TASK, new DeleteTaskCommand(usersStorage));
        commands.put(LIST_TASKS, new ListTasksCommand(usersStorage));
        commands.put(LIST_DASHBOARD, new ListDashboardCommand(usersStorage));
        commands.put(FINISH_TASK, new FinishTaskCommand(usersStorage));
        commands.put(ADD_COLLABORATION, new AddCollaborationCommand(collaborationsStorage));
        commands.put(DELETE_COLLABORATION, new DeleteCollaborationCommand(collaborationsStorage));
        commands.put(LIST_COLLABORATIONS, new ListCollaborationsCommand(collaborationsStorage));
        commands.put(ADD_USER, new AddUserCommand(collaborationsStorage, usersStorage));
        commands.put(ASSIGN_TASKS, new AssignTaskCommand(collaborationsStorage, usersStorage));
        commands.put(LIST_COLLABORATION_TASKS, new ListCollaborationTasksCommand(collaborationsStorage));
        commands.put(LIST_USERS, new ListUsersCommand(collaborationsStorage));
        commands.put(DISCONNECT, new DisconnectCommand(usersStorage, collaborationsStorage));

        commands.put(INVALID_COMMAND, (args, user) -> INVALID_COMMAND);
    }

    public String execute(Command cmd, String user) {
        CommandBase command = commands.getOrDefault(cmd.command(), commands.get(INVALID_COMMAND));
        return command.execute(cmd.arguments(), user);
    }
}
