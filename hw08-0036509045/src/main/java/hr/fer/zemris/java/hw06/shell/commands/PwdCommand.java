package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.util.*;

/**
 * Shell command that takes no arguments and prints current working directory.
 *
 * @author Jan Capek
 */
public class PwdCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "pwd";

        description = new ArrayList<>();
        description.add("Pwd command takes no arguments and prints current working directory of the shell.");
        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        try{
            ArgumentChecker.checkExecuteCommandArgs(env, arguments, 0);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        env.writeln(env.getCurrentDirectory().toString());
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public List<String> getCommandDescription() {
        return description;
    }
}
