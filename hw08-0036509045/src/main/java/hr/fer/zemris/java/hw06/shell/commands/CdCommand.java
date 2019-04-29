package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CdCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "cd";

        description = new ArrayList<>();
        description.add("'cd' command takes one argument which has to be path to a directory " +
                "and switches current working directory of the shell to given directory.");
        description = Collections.unmodifiableList(description);
    }


    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args;
        try {
            args = ArgumentChecker.checkExecuteCommandArgs(env, arguments, 1);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        Path dir = Paths.get(args.get(0));

        try {
            env.setCurrentDirectory(dir);
        } catch (IllegalArgumentException e) {
            env.writeln("Path is not directory.");
        } catch (RuntimeException e) {
            env.writeln("Could not set current directory.");
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
