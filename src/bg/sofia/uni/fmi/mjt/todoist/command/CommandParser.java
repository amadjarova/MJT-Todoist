package bg.sofia.uni.fmi.mjt.todoist.command;

import bg.sofia.uni.fmi.mjt.todoist.exception.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.todoist.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.todoist.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommandParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String PARAM_NAME = "name";
    private static final String REGEX = "=";
    private static final String PARAM_DATE = "date";
    private static final String PARAM_DUE_DATE = "due-date";
    private static final String PARAM_DESCRIPTION = "description";

    public boolean isArgumentFormatCorrect(String argument, String[] tokens) {
        return tokens.length == 2 && tokens[0].equals(argument) && tokens[1].length() > 2 &&
            tokens[1].charAt(0) == '<' && tokens[1].charAt(tokens[1].length() - 1) == '>';
    }

    public String removeBracketsFromString(String str) {
        return str.substring(1, str.length() - 1);
    }

    public LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public boolean isUserLoggedIn(String username) {
        return username != null;
    }

    public Task constructTask(String[] args) throws InvalidCommandException, TimeFrameMismatchException {
        String[] tokens = args[0].split(REGEX);
        if (!isArgumentFormatCorrect(PARAM_NAME, tokens)) {
            throw new InvalidCommandException("First param should be name=<name>");
        }
        Task.TaskBuilder task = new Task.TaskBuilder(removeBracketsFromString(tokens[1]));
        for (int i = 1; i < args.length; i++) {
            String[] tokens1 = args[i].split(REGEX);
            if (isArgumentFormatCorrect(PARAM_DATE, tokens1)) {
                if (task.hasDate()) {
                    throw new InvalidCommandException("The task should have only one date.");
                }
                task.setDate(parseDate(removeBracketsFromString(tokens1[1])));
            } else if (isArgumentFormatCorrect(PARAM_DUE_DATE, tokens1)) {
                if (task.hasDueDate()) {
                    throw new InvalidCommandException("The task should have only one due-date.");
                }
                task.setDueDate(parseDate(removeBracketsFromString(tokens1[1])));
            } else if (isArgumentFormatCorrect(PARAM_DESCRIPTION, tokens1)) {
                if (task.hasDescription()) {
                    throw new InvalidCommandException("The task should have only one description.");
                }
                task.setDescription(removeBracketsFromString(tokens1[1]));
            } else {
                throw new InvalidCommandException("Invalid command arguments.");
            }
        }
        return task.build();
    }

}
