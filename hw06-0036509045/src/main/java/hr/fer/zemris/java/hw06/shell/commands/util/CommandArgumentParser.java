package hr.fer.zemris.java.hw06.shell.commands.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Instances of this class are used to parse shell command arguments.
 *
 * @author Jan Capek
 */
public class CommandArgumentParser {

    private CommandArgumentLexer lexer;
    private List<String> arguments;

    /**
     * Constructs a new parser for parsing command arguments.
     *
     * @param arguments Command arguments that need to be parsed.
     * @throws NullPointerException If given string is {@code null}.
     * @throws RuntimeException If some arguments are invalid.
     */
    public CommandArgumentParser(String arguments) {
        lexer = new CommandArgumentLexer(arguments); // this will throw NullPointerException if arguments are null
        this.arguments = new ArrayList<>();
        parse();
    }

    /**
     * Fills a {@link this#arguments} with arguments returned by lexer.
     *
     * @throws RuntimeException If some arguments are invalid.
     */
    private void parse() {
        for(String arg = lexer.next(); arg != null; arg = lexer.next()) {
            arguments.add(arg);
        }
    }

    /**
     * @return Parsed arguments.
     */
    public List<String> getArguments() {
        return arguments;
    }
}
