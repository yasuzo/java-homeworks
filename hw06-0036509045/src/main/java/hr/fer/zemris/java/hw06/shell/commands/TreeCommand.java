package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shell command used for recursive listing of directory contents.
 *
 * @author Jan Capek
 */
public class TreeCommand implements ShellCommand {

    private static String name;
    private static List<String> description;

    static {
        name = "tree";

        description = new ArrayList<>();
        description.add("Tree command takes one argument which is a directory and prints out all its contents " +
                "including children of children of children etc.");

        description = Collections.unmodifiableList(description);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException                           If any of the parameters are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user is not possible.
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(arguments);

//        check attributes
        ArgumentChecker.Result checkResult = ArgumentChecker.checkArguments(arguments, 1);
        if (checkResult.isValid() == false) {
            checkResult.getMessages().forEach(env::writeln);
            return ShellStatus.CONTINUE;
        }

        Path dir = Paths.get(checkResult.getArguments().get(0));
//        Check if given path is a directory
        if (Files.isDirectory(dir) == false) {
            env.writeln("Given path is not a directory.");
            return ShellStatus.CONTINUE;
        }

        LocalVisitor visitor = new LocalVisitor();

        try {
            Files.walkFileTree(dir, visitor);
        } catch (IOException e) {
            env.writeln("Could not access file system.");
            return ShellStatus.CONTINUE;
        }

        env.writeln(visitor.toString());
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

    /**
     * {@link FileVisitor} that goes through all files and fills a {@link StringBuilder} with directory tree.
     */
    private static class LocalVisitor implements FileVisitor<Path> {
        private int level = 0;
        private StringBuilder sb = new StringBuilder();

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            appendPathName(dir);
            level++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            appendPathName(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            level--;
            return FileVisitResult.CONTINUE;
        }

        /**
         * Appends path name to the {@link StringBuilder} object.
         *
         * @param p Path to a file.
         */
        private void appendPathName(Path p) {
            for (int i = 0; i < level; i++) {
                sb.append(' ');
                sb.append(' ');
            }
            sb.append(p.getFileName().toString());
            sb.append('\n');
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
