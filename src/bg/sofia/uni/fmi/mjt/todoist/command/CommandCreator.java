package bg.sofia.uni.fmi.mjt.todoist.command;

import java.util.Arrays;

public class CommandCreator {
    private static final String REGEX = " --";
    private static String[] getCommandArguments(String input) {
        return input.split(REGEX);
    }

    public static Command newCommand(String clientInput) {
        String[] tokens = CommandCreator.getCommandArguments(clientInput);
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        return new Command(tokens[0], args);
    }
}