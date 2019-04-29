package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Interface that models a shell environment.
 *
 * @author Jan Capek
 */
public interface Environment {

    /**
     * Reads one line of text from console.
     *
     * @return Read line of text.
     * @throws ShellIOException In case data could not be read.
     */
    String readLine() throws ShellIOException;

    /**
     * Writes given text to the console.
     *
     * @param text Text to write.
     * @throws ShellIOException In case data could not be written.
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes given text to the console and enters new line.
     *
     * @param text Text to write.
     * @throws ShellIOException In case data could not be written.
     */
    void writeln(String text) throws ShellIOException;

    /**
     * @return Sorted map of available commands.
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * @return Symbol that signals a command stretches through multiple lines.
     */
    Character getMultilineSymbol();

    /**
     * Sets a symbol that indicates a command stretches through multiple lines.
     *
     * @param symbol New multiline symbol.
     */
    void setMultilineSymbol(Character symbol);

    /**
     * @return Prompt symbol.
     */
    Character getPromptSymbol();

    /**
     * Sets a new prompt symbol.
     *
     * @param symbol New prompt symbol.
     */
    void setPromptSymbol(Character symbol);

    /**
     * @return Symbol that indicates to the shell that more arguments follow after a new line.
     */
    Character getMorelinesSymbol();

    /**
     * Sets a new more lines symbol.
     *
     * @param symbol New symbol.
     */
    void setMorelinesSymbol(Character symbol);

    /**
     * @return Current working directory.
     */
    Path getCurrentDirectory();

    /**
     * Sets new current working directory.
     *
     * @param path New working directory.
     * @throws IllegalArgumentException If given path is not a directory.
     * @throws RuntimeException         If path could not be resolved.
     * @throws NullPointerException     If given path is {@code null}.
     */
    void setCurrentDirectory(Path path);

    /**
     * Returns data shared between commands.
     * Data is stored in underlying map.
     *
     * @param key Key under which shared data is stored.
     * @return Shared data which was stored under given key or {@code null} if no data was under the key.
     */
    Object getSharedData(String key);

    /**
     * Stores given value under given key.
     *
     * @param key Key to store value under.
     * @param value Value that should be stored.
     */
    void setSharedData(String key, Object value);

}
