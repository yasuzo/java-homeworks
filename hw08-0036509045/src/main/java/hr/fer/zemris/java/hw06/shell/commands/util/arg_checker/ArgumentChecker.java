package hr.fer.zemris.java.hw06.shell.commands.util.arg_checker;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.util.parsing.CommandArgumentParser;

import java.util.List;
import java.util.Objects;

/**
 * Class containing static methods used for checking argument count and validity of arguments.
 *
 * @author Jan Capek
 */
public class ArgumentChecker {


    /**
     * This command test if environment variable or argument string is {@code null}
     * in which case {@link NullPointerException} will be thrown and nothing will be sent to user. <br>
     * Also this will test is argument string is valid (no closing string quotes are missing
     * and there is space between two arguments etc.) as well as if the argument string contains valid number of arguments.
     * <br>
     * If there arguments are invalid, appropriate message will be sent to user through environment
     * and {@link IllegalArgumentException} will be thrown. <br>
     * If everything is ok, a list of parsed arguments will be returned.
     *
     * @param env                 Environment given as a parameter in {@link ShellCommand#executeCommand(Environment, String)}.
     * @param arguments           Argument string given in {@link ShellCommand#executeCommand(Environment, String)}.
     * @param validArgumentCounts Valid number of parsed arguments from string.
     * @return List of arguments parsed from argument string.
     * @throws IllegalArgumentException                       If {@code arguments} is invalid or number of arguments is invalid.
     * @throws NullPointerException                           If given environment variable or arguments variable is {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user was not possible.
     */
    public static List<String> checkExecuteCommandArgs(Environment env, String arguments, int... validArgumentCounts) {
        Objects.requireNonNull(env, "Environment cannot be null.");
        Objects.requireNonNull(arguments, "Argument string cannot be null.");

//        parse arguments
        List<String> args;
        try {
            args = new CommandArgumentParser(arguments).getArguments();
        } catch (RuntimeException e) {
            env.writeln("Command called with illegal arguments " +
                    "e.g. closing quote is missing or there is no space between a string and next argument.");
            throw new IllegalArgumentException("Illegal arguments given.");
        }

//        check if args count is valid
        int argCount = args.size();
        for (int count : validArgumentCounts) {
            if (count == argCount) {
//                count is ok -> exit
                return args;
            }
        }
        env.writeln("Invalid argument count... Call 'help' for information about a command.");
        throw new IllegalArgumentException("Invalid argument count for shell command.");
    }
}
