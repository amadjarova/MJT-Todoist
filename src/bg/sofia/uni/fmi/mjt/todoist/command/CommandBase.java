package bg.sofia.uni.fmi.mjt.todoist.command;

public interface CommandBase {
    String execute(String[] args, String username);
}
