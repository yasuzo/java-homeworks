package com.yasuzo.shell;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Shell program used for executing commands on system.
 *
 * @author Jan Capek
 */
public class DefaultShell {

    /**
     * Shell environment used for communication with user and sharing data between commands.
     */
    private Environment env;

    /**
     * Constructs a new default shell that uses given environment.
     *
     * @param env Shell environment.
     * @throws NullPointerException If given environment is {@code null}.
     * @see Environment
     */
    public DefaultShell(Environment env) {
        this.env = Objects.requireNonNull(env);
    }

    /**
     * Extracts arguments from input. Arguments are all that is found after first word.
     *
     * @param input User input.
     * @return Command arguments or {@code null} if empty string was given.
     */
    private static String extractCommandArguments(String input) {
        Scanner sc = new Scanner(input);
        String arguments;
        if (sc.hasNext()) {
            sc.next();
            arguments = sc.hasNext() ? sc.nextLine() : "";
        } else {
            arguments = null;
        }
        sc.close();
        return arguments;
    }

    /**
     * Returns command name from user input. Command name is a first word in the input.
     *
     * @param input User input.
     * @return Command name if there is one, {@code null} otherwise.
     */
    private static String extractCommandName(String input) {
        Scanner sc = new Scanner(input);
        String name;
        if (sc.hasNext()) {
            name = sc.next();
        } else {
            name = null;
        }
        sc.close();
        return name;
    }

    /**
     * Starts the shell. This will never return.
     *
     * @throws ShellIOException If communication with user was not possible.
     */
    public void start() {
        env.writeln("Welcome to shell!");

//            get commands locally
        Map<String, ShellCommand> commands = env.commands();

//            status init
        ShellStatus status = ShellStatus.CONTINUE;
        do {
//                read input
            env.write(env.getPromptSymbol().toString() + " ");
            String l = readLineOrLines();
//                blank input
            if (l.isBlank()) {
                continue;
            }

//                get requested command and its arguments
            String commandName = extractCommandName(l);
            String arguments = extractCommandArguments(l);
            ShellCommand command = commands.get(commandName);
//                unrecognized command
            if (command == null) {
                env.writeln(String.format("Unrecognized command '%s'.", commandName));
                continue;
            }

//                execute a command and update status.
            try {
                status = command.executeCommand(env, arguments);
            } catch (RuntimeException e) {
                if (e instanceof ShellIOException) {
                    throw e;
                }
                env.writeln("ERROR: " + e.getMessage());
                status = ShellStatus.CONTINUE;
            }
        } while (status != ShellStatus.TERMINATE);
    }

    /**
     * Reads one or more lines that user entered.
     *
     * @return User input.
     * @throws ShellIOException If input could not be read.
     */
    private String readLineOrLines() {
        String s = "";
        boolean readMoreLines;
        do {
            readMoreLines = false;
            s += env.readLine();
            if (s.endsWith(env.getMorelinesSymbol().toString())) {
                s = s.substring(0, s.length() - 1);
                readMoreLines = true;
                env.write(env.getMultilineSymbol().toString() + " ");
            }
        } while (readMoreLines);
        return s;
    }
}
