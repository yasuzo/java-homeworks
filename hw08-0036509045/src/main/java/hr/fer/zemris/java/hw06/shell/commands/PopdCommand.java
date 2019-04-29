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
 * Shell command that pops directory path from underlying stack and sets current working directory to that directory.
 *
 * @author Jan Capek
 */
public class PopdCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "popd";

        description = new ArrayList<>();
        description.add("Popd command takes in zero arguments.");
        description.add("It will pop a directory from the stack and change current directory to it.");

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
            env.writeln("Stack is empty, there isn't a directory to pop.");
        } else {
            try {
                env.setCurrentDirectory(stack.pop());
            } catch (RuntimeException e) {
                env.writeln("There was a problem changing to directory. Exiting...");
                return ShellStatus.TERMINATE;
            }
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
