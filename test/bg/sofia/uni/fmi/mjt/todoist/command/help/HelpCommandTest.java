package bg.sofia.uni.fmi.mjt.todoist.command.help;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {
    private CommandBase command = new HelpCommand();
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

    @Test
    void testExecuteNoArguments() {
        String[] args = new String[]{};
        assertEquals(HELP_MESSAGE, command.execute(args, "idk"));
    }

    @Test
    void testExecuteWithArguments() {
        String[] args = new String[]{"1","2"};
        assertEquals(INVALID_COMMAND_MESSAGE, command.execute(args, "idk"));
    }
}
