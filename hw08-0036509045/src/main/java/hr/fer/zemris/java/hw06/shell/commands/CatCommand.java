package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shell command that reads a file in given charset and outputs it to the user.
 * If charset is not given, default one will be used.
 *
 * @author Jan Capek
 */
public class CatCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "cat";

        description = new ArrayList<>();
        description.add("Cat command prints out contents of a file to the console. It takes in one or two arguments.");
        description.add("In case two arguments are provided, first has to be path to a readable " +
                "file and second must be charset used to read a file.");
        description.add("In case one argument is provided, it has to be path to a readable file. " +
                "In that case, default charset will be used.");

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

//        create path and charset
        Path file = env.getCurrentDirectory().resolve(Paths.get(args.get(0)));
        Charset charset;
        try {
            charset = args.size() == 2 ? Charset.forName(args.get(1)) : Charset.defaultCharset();
        } catch (IllegalCharsetNameException e) {
            env.writeln("Invalid charset was given.");
            return ShellStatus.CONTINUE;
        }

//        read a file and output it to the console
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                env.writeln(line);
            }
        } catch (IOException e) {
            env.writeln("Either given file does not exist, is a directory or simply is not readable.");
        }
        return ShellStatus.CONTINUE;
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
