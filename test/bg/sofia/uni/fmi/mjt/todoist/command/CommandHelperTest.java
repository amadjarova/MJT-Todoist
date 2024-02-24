package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandHelperTest {
    private CommandParser commandHelper = new CommandParser();
    @Test
    void testConstructTaskTwoNames() {
        String[] args = new String[]{"name=<name>", "name=<name1>"};
        assertThrows(InvalidCommandException.class, ()->commandHelper.constructTask(args),
            "constructTask should throw exception when the arguments are invalid");
    }
}
