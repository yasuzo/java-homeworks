package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Command that pushes current working directory on the stack and changes current working directory to a new directory.
 *
 * @author Jan Capek
 */
public class PushdCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "pushd";

        description = new ArrayList<>();
        description.add("Pushd command takes in one argument which needs to be a path to a directory.");
        description.add("It will push current working directory on the stack and change it to the new given directory.");

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

        Path dir = env.getCurrentDirectory().resolve(Path.of(args.get(0)));
        Path currentDir = env.getCurrentDirectory();

//        sets working directory to dir
        try {
            env.setCurrentDirectory(dir);
        } catch (IllegalArgumentException e) {
            env.writeln("Path is not a directory.");
            return ShellStatus.CONTINUE;
        } catch (RuntimeException e) {
            env.writeln("Path could not be resolved.");
            return ShellStatus.CONTINUE;
        }

//        pushes last working directory on the stack
        Stack<Path> cdStack = (Stack<Path>) env.getSharedData("cdstack");
        if (cdStack == null) {
            cdStack = new Stack<>();
            env.setSharedData("cdstack", cdStack);
        }
        cdStack.push(currentDir);
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
