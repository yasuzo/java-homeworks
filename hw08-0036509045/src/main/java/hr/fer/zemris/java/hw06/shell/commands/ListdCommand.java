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
 * Shell command for listing all directories stored on a stack.
 *
 * @author Jan Capek
 */
public class ListdCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "listd";

        description = new ArrayList<>();
        description.add("Listd command takes in zero arguments.");
        description.add("It will list directories stored on the stack starting from a most recent directory.");

        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        try {
            ArgumentChecker.checkExecuteCommandArgs(env, arguments, 0);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        Stack<Path> stack = (Stack<Path>) env.getSharedData("cdstack");
        if (stack == null || stack.size() == 0) {
            env.writeln("Nema pohranjenih direktorija.");
            return ShellStatus.CONTINUE;
        }

        Object[] dirs = stack.toArray();

        for (int i = dirs.length - 1; i >= 0; i--) {
            Path p = (Path) dirs[i];
            env.writeln(p.normalize().toString());
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
