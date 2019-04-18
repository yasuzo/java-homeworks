package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.util.List;
import java.util.Objects;

/**
 * Shell command that either prints current requested symbol for i.e. PROMPT or sets a new symbol.
 *
 * @author Jan Capek
 */
public class SymbolCommand implements ShellCommand {

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException                           If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user has failed.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(arguments);

        ArgumentChecker.Result checkResult = ArgumentChecker.checkArguments(arguments, 1, 2);
        if (checkResult.isValid() == false) {
            checkResult.getMessages().forEach(env::writeln);
            return ShellStatus.CONTINUE;
        }

        List<String> args = checkResult.getArguments();

        if (args.size() == 1) {
            switch (args.get(0)) {
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
            return ShellStatus.CONTINUE;
        }

        String symbol = args.get(1);
        if (symbol.length() > 1) {
            env.writeln("Symbol has to be one character.");
            return ShellStatus.CONTINUE;
        }

        char sym = symbol.charAt(0);
        Character oldSym;
        switch (args.get(0)) {
            case "PROMPT":
                oldSym = env.getPromptSymbol();
                env.setPromptSymbol(sym);
                env.writeln(String.format("Symbol for PROMPT changed from '%s' to '%c'", oldSym, sym));
                break;
            case "MORELINES":
                oldSym = env.getMorelinesSymbol();
                env.setMorelinesSymbol(sym);
                env.writeln(String.format("Symbol for MORELINES changed from '%s' to '%c'", oldSym, sym));
                break;
            case "MULTILINE":
                oldSym = env.getMultilineSymbol();
                env.setMultilineSymbol(sym);
                env.writeln(String.format("Symbol for MULTILINE changed from '%s' to '%c'", oldSym, sym));
                break;
            default:
                env.writeln("Symbol name is not recognized. Recognizable symbols are PROMPT, MORELINES and MULTILINE.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public List<String> getCommandDescription() {
        return null;
    }
}
