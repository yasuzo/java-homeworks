package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.*;

public class CharsetsCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "charsets";

        description = new ArrayList<>();
        description.add("Charsets command takes no arguments and lists all available charsets in Java platform.");
        description.add("A single charset will be outputted per line.");

        description = Collections.unmodifiableList(description);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException                           If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If a message could not be written to user.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(arguments);
        if (arguments.isBlank() == false) {
            env.writeln("charsets command does not take any arguments.");
            return ShellStatus.CONTINUE;
        }

        Set<String> charsets = Charset.availableCharsets().keySet();
        for (String charset : charsets) {
            env.writeln(charset);
        }
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
