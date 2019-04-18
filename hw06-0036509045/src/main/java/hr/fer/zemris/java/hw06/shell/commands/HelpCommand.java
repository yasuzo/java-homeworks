package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shell command used to print a description of other shell commands.
 *
 * @author Jan Capek
 */
public class HelpCommand implements ShellCommand {

    private static String name;
    private static List<String> description;

    static {
        name = "help";

        description = new ArrayList<>();
        description.add("This command takes in zero or one argument.");
        description.add("If zero arguments are given this will print all available commands to the user.");
        description.add("If one argument is given it will print commands description if there is any.");

        description = Collections.unmodifiableList(description);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException                           If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user has failed.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(arguments);

        ArgumentChecker.Result checkResult = ArgumentChecker.checkArguments(arguments, 0, 1);
        if (checkResult.isValid() == false) {
            checkResult.getMessages().forEach(env::writeln);
            return ShellStatus.CONTINUE;
        }

        List<String> args = checkResult.getArguments();

//        no arguments => list all available commands
        if (args.size() == 0) {
            env.commands().forEach((key, value) -> env.writeln(key));
            return ShellStatus.CONTINUE;
        }

        String commandName = args.get(0);
        ShellCommand command = env.commands().get(commandName);

//        unrecognized command
        if (command == null) {
            env.writeln(String.format("No commands match '%s'.", commandName));
            return ShellStatus.CONTINUE;
        }

        List<String> description = command.getCommandDescription();

//        missing description
        if (description == null) {
            env.writeln(String.format("Command '%s' is missing description.", commandName));
            return ShellStatus.CONTINUE;
        }

        description.forEach(env::writeln);

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
