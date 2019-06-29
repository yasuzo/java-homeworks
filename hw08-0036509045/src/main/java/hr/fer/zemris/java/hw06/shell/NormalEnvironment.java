package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.*;

import java.io.IOError;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Implementation of normal shell environment.
 *
 * @author Jan Capek
 */
public class NormalEnvironment implements Environment {

    private static final Character DEFAULT_PROMPT_SYMBOL = '>';
    private static final Character DEFAULT_MORELINES_SYMBOL = '\\';
    private static final Character DEFAULT_MULTILINE_SYMBOL = '|';
    private static SortedMap<String, ShellCommand> commands;

    static {
        commands = new TreeMap<>();
        commands.put("cat", new CatCommand());
        commands.put("ls", new LsCommand());
        commands.put("charsets", new CharsetsCommand());
        commands.put("tree", new TreeCommand());
        commands.put("copy", new CopyCommand());
        commands.put("mkdir", new MkdirCommand());
        commands.put("hexdump", new HexdumpCommand());
        commands.put("exit", new ExitCommand());
        commands.put("help", new HelpCommand());
        commands.put("symbol", new SymbolCommand());
        commands.put("pwd", new PwdCommand());
        commands.put("cd", new CdCommand());
        commands.put("pushd", new PushdCommand());
        commands.put("popd", new PopdCommand());
        commands.put("listd", new ListdCommand());
        commands.put("dropd", new DropdCommand());
        commands.put("massrename", new MassrenameCommand());
        commands = Collections.unmodifiableSortedMap(commands);
    }

    private Scanner sc;
    private Character promptSymbol;
    private Character morelinesSymbol;
    private Character multilineSymbol;
    private Path currentDir;
    private Map<String, Object> sharedData;

    /**
     * Constructs a normal shell environment with default settings that uses given scanner for reading user input.
     *
     * @param sc Scanner used for reading user input.
     * @throws NullPointerException If given scanner is {@code null}.
     * @throws RuntimeException     If current working directory could not be resolved.
     */
    public NormalEnvironment(Scanner sc) {
        this.sc = Objects.requireNonNull(sc);
        promptSymbol = DEFAULT_PROMPT_SYMBOL;
        morelinesSymbol = DEFAULT_MORELINES_SYMBOL;
        multilineSymbol = DEFAULT_MULTILINE_SYMBOL;
        sharedData = new HashMap<>();
        setCurrentDirectory(Paths.get("."));
    }

    @Override
    public String readLine() throws ShellIOException {
        try {
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            throw new ShellIOException("Couldn't read a line.");
        } catch (IllegalStateException e) {
            throw new ShellIOException("Scanner is closed.");
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
     *
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
     *
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
     *
     * @throws NullPointerException If given symbol is {@code null};
     */
    @Override
    public void setMorelinesSymbol(Character symbol) {
        morelinesSymbol = Objects.requireNonNull(symbol);
    }

    @Override
    public Path getCurrentDirectory() {
        return currentDir;
    }

    @Override
    public void setCurrentDirectory(Path path) {
        Objects.requireNonNull(path);
        try {
            if (Files.isDirectory(path) == false) {
                throw new IllegalArgumentException("Given path is not a directory.");
            }
            currentDir = path.toAbsolutePath().normalize();
        } catch (IOError | SecurityException e) {
            throw new RuntimeException("Error happened while resolving a path, could not set new working directory.");
        }
    }

    @Override
    public Object getSharedData(String key) {
        return sharedData.get(key);
    }

    @Override
    public void setSharedData(String key, Object value) {
        sharedData.put(key, value);
    }
}
