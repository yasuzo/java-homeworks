package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.CatCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsCommand;

import java.util.*;

/**
 * Implementation of normal shell environment.
 *
 * @author Jan Capek
 */
public class NormalEnvironment implements Environment {

    private static SortedMap<String, ShellCommand> commands;
    private static final Character DEFAULT_PROMPT_SYMBOL = '>';
    private static final Character DEFAULT_MORELINES_SYMBOL = '\\';
    private static final Character DEFAULT_MULTILINE_SYMBOL = '|';
    static {
        commands = new TreeMap<>();
        commands.put("cat", new CatCommand());
        commands.put("ls", new LsCommand());
        commands.put("charsets", new CharsetsCommand());
        commands = Collections.unmodifiableSortedMap(commands);
    }

    private Scanner sc;
    private Character promptSymbol;
    private Character morelinesSymbol;
    private Character multilineSymbol;

    /**
     * Constructs a normal shell environment with default settings that uses given scanner for reading user input.
     *
     * @param sc Scanner used for reading user input.
     * @throws NullPointerException If given scanner is {@code null}.
     */
    public NormalEnvironment(Scanner sc) {
        this.sc = Objects.requireNonNull(sc);
        promptSymbol = DEFAULT_PROMPT_SYMBOL;
        morelinesSymbol = DEFAULT_MORELINES_SYMBOL;
        multilineSymbol = DEFAULT_MULTILINE_SYMBOL;
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            throw new ShellIOException("Couldn't read a line.");
        }
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return NormalEnvironment.commands;
    }

    @Override
    public Character getMultilineSymbol() {
        return multilineSymbol;
    }

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given symbol is {@code null};
     */
    @Override
    public void setMultilineSymbol(Character symbol) {
        multilineSymbol = Objects.requireNonNull(symbol);
    }

    @Override
    public Character getPromptSymbol() {
        return promptSymbol;
    }

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given symbol is {@code null};
     */
    @Override
    public void setPromptSymbol(Character symbol) {
        promptSymbol = Objects.requireNonNull(symbol);
    }

    @Override
    public Character getMorelinesSymbol() {
        return morelinesSymbol;
    }

    /**
     * {@inheritDoc}
     * @throws NullPointerException If given symbol is {@code null};
     */
    @Override
    public void setMorelinesSymbol(Character symbol) {
        morelinesSymbol = Objects.requireNonNull(symbol);
    }
}
