package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.FilterResult;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Filter;
import java.util.regex.PatternSyntaxException;

/**
 * Shell command for massive renaming/moving of files (<b>not directories</b>).
 *
 * @author Jan Capek
 */
public class MassrenameCommand implements ShellCommand {
    private static String name;
    private static List<String> description;

    static {
        name = "massrename";

        description = new ArrayList<>();
        description.add("Massrename command renames/moves all files that match a regex from one directory to another.");
        description.add("Syntax of the command is: massrename DIR1 DIR2 CMD REGEX other_arguments");
        description.add("DIR1 is source directory, DIR2 is destination directory, CMD is name of the specific command etc.");
        description.add("Available commands are:");
        description.add("\tfilter\t- Prints names of files that match regex (usage: massrename DIR1 DIR2 filterCommand REGEX)");
        description.add("\tgroups\t- Prints all groups of all files that match regex (usage: massrename DIR1 DIR2 groups REGEX))");
        description.add("\tshow\t- Takes an additional argument [expression] which is a pattern to which files should be renamed to");
        description.add("\t\t\tand shows possible results of renaming (usage: massrename DIR1 DIR2 show REGEX EXPRESSION))");
        description.add("\texecute\t- Takes exact arguments as 'show' but it will actually rename/move files that match regex (usage: massrename DIR1 DIR2 show REGEX EXPRESSION)");

        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args;
        try {
            args = ArgumentChecker.checkExecuteCommandArgs(env, arguments, 4, 5);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        Path dir1 = env.getCurrentDirectory().resolve(args.get(0));
        Path dir2 = env.getCurrentDirectory().resolve(args.get(1));

        if(Files.isDirectory(dir1) == false || Files.isDirectory(dir2) == false) {
            env.writeln("First two arguments should be directories!");
            return ShellStatus.CONTINUE;
        }

        String command = args.get(2);
        String regex = args.get(3);


        List<FilterResult> results;
        try {
            results = FilterResult.filter(dir1, regex);
        } catch (IOException e) {
            env.writeln("There was a problem accessing a file system.");
            return ShellStatus.CONTINUE;
        } catch (PatternSyntaxException e) {
            env.writeln("Invalid regex provided.");
            return ShellStatus.CONTINUE;
        }

        if(args.size() == 4 && (command.equals("filter") || command.equals("groups"))) {
            if(command.equals("filter")){
                filterCommand(env, results);
            } else {
                groupsCommand(env, results);
            }
        } else if (args.size() == 5 && (command.equals("execute") || command.equals("show"))) {

        } else {
            env.writeln("Wrong number of arguments or given CMD is not recognized. Call 'help' for more info.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Executes groups command.
     *
     * @param env Environment used to communicate with user.
     * @param results Filtered results.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException In case communication with user is not possible.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    private void groupsCommand(Environment env, List<FilterResult> results) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(results);
        results.forEach(res -> {
            StringBuilder sb = new StringBuilder();
            sb.append(res.toString());

            int groupCount = res.numberOfGroups();
            for (int i = 0; i <= groupCount; i++) {
                sb.append(String.format(" %d: %s", i, res.group(i)));
            }

            env.writeln(sb.toString());
        });
    }

    /**
     * Executes filter command.
     *
     * @param env Environment used to communicate with user.
     * @param results Filtered results.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException In case communication with user is not possible.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    private void filterCommand(Environment env, List<FilterResult> results) {
        Objects.requireNonNull(results);
        Objects.requireNonNull(env);
        results.forEach(result -> env.writeln(result.toString()));
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
