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
 * Shell command that pops a directory from the top of the stack and drops it.
 *
 * @author Jan Capek
 */
public class DropdCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "dropd";

        description = new ArrayList<>();
        description.add("Dropd command takes in zero arguments.");
        description.add("It will remove stored directory from top of the stack.");

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
            env.writeln("Stack is empty there are no directories to remove.");
        } else {
            stack.pop();
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
