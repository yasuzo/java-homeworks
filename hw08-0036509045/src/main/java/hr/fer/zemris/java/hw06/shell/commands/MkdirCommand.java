package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.PathResolver;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shell command that creates wanted directory structure if it does not already exist.
 *
 * @author Jan Capek
 */
public class MkdirCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "mkdir";

        description = new ArrayList<>();
        description.add("Takes in a path which can be already existing directory, in that case nothing will happen, " +
                "or it has to be a potential directory in which case it will create required directory structure.");
        description.add("If given path is an existing file, operation will be aborted.");
        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args;
        try{
            args = ArgumentChecker.checkExecuteCommandArgs(env, arguments, 1, 2);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        Path dirsToCreate = PathResolver.resolveRelativePath(env, Paths.get(args.get(0)));

//        mkdir
        try {
            Files.createDirectories(dirsToCreate);
        } catch (FileAlreadyExistsException e) {
            env.writeln("Path already exists but is not a directory.");
        } catch (IOException e) {
            env.writeln("Error occurred while trying to access file system.");
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
