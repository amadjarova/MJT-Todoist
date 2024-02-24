package bg.sofia.uni.fmi.mjt.todoist.command.help;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;

public class HelpCommand implements CommandBase {
    private static final String HELP_MESSAGE = """
        Commands:
        register --username=<username> --password=<password>
        login --username=<username> --password=<password>
        add-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
        update-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
        delete-task --name=<task_name>
        delete-task --name=<task_name> --date=<date>
        get-task --name=<task_name>
        get-task --name=<task_name> --date=<date>
        list-tasks
        list-tasks --completed=true
        list-tasks --date=<date>
        list-dashboard
        finish-task --name=<name>
        add-collaboration --name=<name>
        delete-collaboration --name=<name>
        list-collaborations
        add-user --collaboration=<name> --user=<username>
        assign-task --collaboration=<name> --user=<username> --task=<name>
        list-collaboration-tasks --collaboration=<name>
        list-users --collaboration=<name>
        The date format should be dd/MM/yyyy. Strings are passed without quotes.
        """;
    private static final String INVALID_COMMAND_MESSAGE = "Invalid command. Please do not pass arguments";

    @Override
    public String execute(String[] args, String username) {
        if (args.length != 0) {
            return INVALID_COMMAND_MESSAGE;
        }

        return HELP_MESSAGE;
    }
}
