package hr.fer.zemris.java.hw06.shell.commands.util.arg_checker;

import hr.fer.zemris.java.hw06.shell.commands.util.CommandArgumentParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class containing static methods used for checking argument count and validity of arguments.
 *
 * @author Jan Capek
 */
public class ArgumentChecker {

    /**
     * Tries to parse given arguments and returns a {@link Result} object containing parsed arguments or
     * fills it with messages that should be outputted to user if arguments are invalid.
     *
     * @param arguments           Arguments that need to be parsed and checked.
     * @param validArgumentCounts Array of integers that represent valid number of arguments argument string should hold.
     * @return Result containing parsed arguments in case they are valid or messages in case they are invalid.
     * @throws NullPointerException If given string is {@code null}.
     */
    public static Result checkArguments(String arguments, int... validArgumentCounts) {
        Result result = checkArguments(arguments);
        if (result.isValid == false) {
            return result;
        }
        result.isValid = false;
        int argCount = result.args.size();
        for (int count : validArgumentCounts) {
            if (count == argCount) {
                result.isValid = true;
                return result;
            }
        }
        result.messages.add("Invalid argument count.");
        return result;
    }

    /**
     * Tries to parse given arguments and returns a {@link Result} object containing parsed arguments or
     * fills it with messages that should be outputted to user if arguments are invalid.
     *
     * @param arguments Arguments that need to be parsed and checked.
     * @return Result containing parsed arguments in case they are valid or messages in case they are invalid.
     * @throws NullPointerException If given string is {@code null}.
     */
    public static Result checkArguments(String arguments) {
        Objects.requireNonNull(arguments);
        Result result = new Result();
//        parse arguments
        try {
            result.args = new CommandArgumentParser(arguments).getArguments();
        } catch (RuntimeException e) {
            result.messages.add("Some arguments are invalid.");
            return result;
        }
        result.isValid = true;
        return result;
    }

    /**
     * Result struct which holds arguments if they are valid, or a list of messages
     * that should be outputted to user if arguments are invalid.
     */
    public static class Result {

        private List<String> messages;
        private List<String> args;
        private boolean isValid;

        private Result() {
            messages = new ArrayList<>();
            args = new ArrayList<>();
        }

        /**
         * @return Messages that should be outputted.
         */
        public List<String> getMessages() {
            return messages;
        }

        /**
         * @return Parsed arguments.
         */
        public List<String> getArguments() {
            return args;
        }

        /**
         * @return {@code true} if arguments are valid, {@code false} otherwise.
         */
        public boolean isValid() {
            return isValid;
        }
    }
}
