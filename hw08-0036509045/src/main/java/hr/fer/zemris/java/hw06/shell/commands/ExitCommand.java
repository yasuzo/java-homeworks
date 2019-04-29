package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shell command used to terminate shell.
 *
 * @author Jan Capek
 */
public class ExitCommand implements ShellCommand {

    private static String name;
    private static List<String> description;

    static {
        name = "exit";

        description = new ArrayList<>();
        description.add("Command terminates the running shell.");
        description.add("This command takes in no arguments (arguments will be ignored if there is any).");
        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
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
