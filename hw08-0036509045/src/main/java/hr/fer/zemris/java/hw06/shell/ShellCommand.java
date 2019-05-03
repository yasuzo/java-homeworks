package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Interface that models one shell command that will need to be executed by the shell.
 *
 * @author Jan Capek
 */
public interface ShellCommand {

    /**
     * Executes a command and returns a new {@link ShellStatus}.
     *
     * @param env       Shell environment that will be used to communicate with a user.
     * @param arguments Command arguments.
     * @return New shell status.
     * @throws ShellIOException     In case a communication with user couldn't be established through given environment.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * @return Command's name.
     */
    String getCommandName();

    /**
     * @return Command's description in form of a read-only list where each item is one line of text.
     */
    List<String> getCommandDescription();
}
