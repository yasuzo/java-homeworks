package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.massrename_util.FilterResult;
import hr.fer.zemris.java.hw06.shell.commands.massrename_util.NameBuilder;
import hr.fer.zemris.java.hw06.shell.commands.massrename_util.NameBuilderParser;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
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

        if (Files.isDirectory(dir1) == false || Files.isDirectory(dir2) == false) {
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

        if (args.size() == 4 && (command.equals("filter") || command.equals("groups"))) {
            if (command.equals("filter")) {
                filterCommand(env, results);
            } else {
                groupsCommand(env, results);
            }
        } else if (args.size() == 5 && (command.equals("execute") || command.equals("show"))) {
            String renamingPattern = args.get(4);
            showOrExecuteCommand(env, dir1, dir2, results, renamingPattern, command);
        } else {
            env.writeln("Wrong number of arguments or given CMD is not recognized. Call 'help' for more info.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Executes show or execute command.
     *
     * @param env Environment used to communicate with user.
     * @param sourceDir Source directory.
     * @param destDir Destination directory in case of execute command.
     * @param results Filtered files.
     * @param renamePattern Renaming pattern.
     * @param command Command that needs to be executed ('show' or 'execute').
     * @throws NullPointerException If any of the arguments is {@code null} or in case a command is "show" then dirs can be null.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user failed.
     */
    private void showOrExecuteCommand(Environment env, Path sourceDir, Path destDir, List<FilterResult> results, String renamePattern, String command) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(renamePattern);
        Objects.requireNonNull(results);
        Objects.requireNonNull(command);

        List<String> names;
        try {
            names = getRenamedList(results,renamePattern);
        } catch (IllegalArgumentException e) {
            env.writeln("Renaming pattern is invalid.");
            return;
        } catch (RuntimeException e) {
            env.writeln(e.getMessage());
            return;
        }

        Iterator<FilterResult> resultIterator = results.iterator();
        Iterator<String> newNameIterator = names.iterator();

//        show
        if(command.equals("show")) {
            while (resultIterator.hasNext()) {
                String line = String.format("%s => %s", resultIterator.next().toString(), newNameIterator.next());
                env.writeln(line);
            }
            return;
        }

//        execute
        while (resultIterator.hasNext()) {
            Path sourceFile = sourceDir.resolve(resultIterator.next().toString());
            Path destFile = destDir.resolve(newNameIterator.next());
            try {
                Files.move(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
                String line = String.format("%s => %s", sourceFile.normalize().toString(), destFile.normalize().toString());
                env.writeln(line);
            } catch (IOException | IOError | SecurityException e) {
                env.writeln("Could not access the file system.");
                return;
            }
        }
    }

    /**
     * Returns a list of new names of files.
     *
     * @param results       Filtered files.
     * @param renamePattern Renaming pattern.
     * @return List of new file names.
     * @throws IllegalArgumentException If renaming pattern is invalid.
     * @throws RuntimeException If an error happened in {@link NameBuilder#execute(FilterResult, StringBuilder)} method.
     */
    private List<String> getRenamedList(List<FilterResult> results, String renamePattern) {
        Objects.requireNonNull(results);
        Objects.requireNonNull(renamePattern);

        NameBuilder nameBuilder = new NameBuilderParser(renamePattern).getNameBuilder();

        List<String> newNames = new ArrayList<>(results.size());

        results.forEach(result -> {
            StringBuilder sb = new StringBuilder();
            nameBuilder.execute(result, sb);
            newNames.add(sb.toString());
        });
        return newNames;
    }

    /**
     * Executes groups command.
     *
     * @param env     Environment used to communicate with user.
     * @param results Filtered results.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException In case communication with user is not possible.
     * @throws NullPointerException                           If any of the arguments are {@code null}.
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
     * @param env     Environment used to communicate with user.
     * @param results Filtered results.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException In case communication with user is not possible.
     * @throws NullPointerException                           If any of the arguments are {@code null}.
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
