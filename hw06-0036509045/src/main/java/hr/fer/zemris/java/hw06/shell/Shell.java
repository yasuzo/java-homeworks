package hr.fer.zemris.java.hw06.shell;

import java.util.Map;
import java.util.Scanner;

/**
 * Shell program used for executing commands on system.
 *
 * @author Jan Capek
 */
public class Shell {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
//            init
            Environment env = new NormalEnvironment(sc);
            env.writeln("Welcome to MyShell v 1.0");

//            get commands locally
            Map<String, ShellCommand> commands = env.commands();

//            status init
            ShellStatus status = ShellStatus.CONTINUE;
            do {
//                read input
                env.write(env.getPromptSymbol().toString() + " ");
                String l = readLineOrLines(env);
//                blank input
                if(l.isBlank()) {
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
                status = command.executeCommand(env, arguments);
            } while (status != ShellStatus.TERMINATE);
        }catch (ShellIOException e) {
            System.err.println("An error occurred. Exiting...");
            return;
        }
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
        if(sc.hasNext()) {
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
        if(sc.hasNext()) {
            name = sc.next();
        } else {
            name = null;
        }
        sc.close();
        return name;
    }

    /**
     * Reads one or more lines that user entered.
     *
     * @param env Environment used for reading user input.
     * @return User input.
     * @throws ShellIOException If input could not be read.
     */
    private static String readLineOrLines(Environment env) {
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
