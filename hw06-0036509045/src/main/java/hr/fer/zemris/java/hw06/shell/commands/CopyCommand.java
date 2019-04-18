package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shell command that copies a file to a location.
 *
 * @author Jan Capek
 */
public class CopyCommand implements ShellCommand {

    private static String name;
    private static List<String> description;

    static {
        name = "copy";

        description = new ArrayList<>();
        description.add("Copy command copies a file to a location.");
        description.add("If location is a directory, new file will be created with the same name inside the directory.");
        description.add("If a location is not a directory, file will be created with new given name inside parent directory given on location.");
        description.add("Parent directory must exist for file to be successfully copied.");

        description = Collections.unmodifiableList(description);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user is not possible.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(arguments);

//        check attributes
        ArgumentChecker.Result checkResult = ArgumentChecker.checkArguments(arguments, 2);
        if (checkResult.isValid() == false) {
            checkResult.getMessages().forEach(env::writeln);
            return ShellStatus.CONTINUE;
        }

        Path file = Paths.get(checkResult.getArguments().get(0));
        Path dest = Paths.get(checkResult.getArguments().get(1));

//        is file readable?
        if(Files.isReadable(file) == false) {
            env.writeln("Either given path is not a file or file is unreadable.");
            return ShellStatus.CONTINUE;
        }

//        is destination a directory?
        if(Files.isDirectory(dest)) {
            dest = Paths.get(dest.toString(), file.getFileName().toString());
        }

//        destination file exists?
        if(Files.exists(dest)) {
            env.write("Destination file already exists. Overwrite [y/N]: ");
            String answer = env.readLine().trim().toLowerCase();
            if(answer.equals("y") == false) {
                env.writeln("Operation aborted.");
                return ShellStatus.CONTINUE;
            }
        }

//        copy
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file));
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(dest))) {
            for(int dataBit = in.read(); dataBit != -1; dataBit = in.read()) {
                out.write(dataBit);
            }
        } catch (IOException e) {
            env.writeln("Operation failed. Either file does not exist, or destination folder structure does not exist.");
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
