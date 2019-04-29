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
 * Shell command that either prints current requested symbol for i.e. PROMPT or sets a new symbol.
 *
 * @author Jan Capek
 */
public class SymbolCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "symbol";

        description = new ArrayList<>();
        description.add("Symbol command prints out current symbol for an action (if one argument is given) or sets a new symbol for an action.");
        description.add("Available symbols for actions are: PROMPT, MULTILINE, MORELINES.");
        description.add("Example for symbol changing is: symbol PROMPT &");
        description.add("Example for retrieving current symbol is: symbol PROMPT");

        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args;
        try {
            args = ArgumentChecker.checkExecuteCommandArgs(env, arguments, 1, 2);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        if (args.size() == 1) {
            printCurrentSymbol(env, args.get(0));
            return ShellStatus.CONTINUE;
        }

        String symbol = args.get(1);
        if (symbol.length() == 1) {
            setSymbol(env, args.get(0), symbol.charAt(0));
        } else {
            env.writeln("Symbol has to be one character.");
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Prints out currently used symbol.
     *
     * @param env        Environment used for communication with user.
     * @param symbolName Name of the symbol that needs to be printed.
     * @throws NullPointerException                           If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user was not possible.
     */
    private void printCurrentSymbol(Environment env, String symbolName) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(symbolName);
        switch (symbolName) {
            case "PROMPT":
                env.writeln(String.format("Symbol for PROMPT is '%s'", env.getPromptSymbol()));
                break;
            case "MORELINES":
                env.writeln(String.format("Symbol for MORELINES is '%s'", env.getMorelinesSymbol()));
                break;
            case "MULTILINE":
                env.writeln(String.format("Symbol for MULTILINE is '%s'", env.getMultilineSymbol()));
                break;
            default:
                env.writeln("Symbol name is not recognized. Recognizable symbols are PROMPT, MORELINES and MULTILINE.");
        }
    }

    /**
     * Sets a new symbol for appropriate type and prints an appropriate message.
     *
     * @param env        Environment whose symbol should be changed. It is also used for communication with user.
     * @param symbolName Name of the symbol that should be changed. Legal names are PROMPT, MORELINES or MULTILINE.
     * @param newSymbol  New symbol that should be used.
     * @throws NullPointerException                           If environment or symbol's name is {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user was not possible.
     */
    private void setSymbol(Environment env, String symbolName, char newSymbol) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(symbolName);
        Character oldSym;
        switch (symbolName) {
            case "PROMPT":
                oldSym = env.getPromptSymbol();
                env.setPromptSymbol(newSymbol);
                env.writeln(String.format("Symbol for PROMPT changed from '%s' to '%c'", oldSym, newSymbol));
                break;
            case "MORELINES":
                oldSym = env.getMorelinesSymbol();
                env.setMorelinesSymbol(newSymbol);
                env.writeln(String.format("Symbol for MORELINES changed from '%s' to '%c'", oldSym, newSymbol));
                break;
            case "MULTILINE":
                oldSym = env.getMultilineSymbol();
                env.setMultilineSymbol(newSymbol);
                env.writeln(String.format("Symbol for MULTILINE changed from '%s' to '%c'", oldSym, newSymbol));
                break;
            default:
                env.writeln("Symbol name is not recognized. Recognizable symbols are PROMPT, MORELINES and MULTILINE.");
        }
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
