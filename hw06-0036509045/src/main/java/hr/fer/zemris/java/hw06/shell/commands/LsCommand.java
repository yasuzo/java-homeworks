package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class LsCommand implements ShellCommand {

    private static String name;
    private static List<String> description;

    static {
        name = "ls";

        description = new ArrayList<>();
        description.add("Lists all items in given directory with their basic details " +
                "such as type, permissions, size, modification date and name");
        description.add("It takes a single argument which has to be a directory.");

        description = Collections.unmodifiableList(description);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException                           If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If a message could not be written to user.
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
        List<String> args = checkResult.getArguments();

//        check if given path is a directory
        Path directory = Paths.get(args.get(0));
        if (Files.isDirectory(directory) == false) {
            env.writeln("Given path is not a directory.");
            return ShellStatus.CONTINUE;
        }

//        actual execution
        FileConsumer consumer = new FileConsumer(env);
        try (Stream<Path> children = Files.walk(directory, 1)) {
            children.filter(path -> {
                try {
                    return Files.isSameFile(directory, path) == false;
                } catch (IOException e) {
                    throw new RuntimeException("Could not access file system.");
                }
            }).forEach(consumer);
        } catch (IOException | RuntimeException e) {
            env.writeln("There was a problem reading file structure from disk.");
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

    /**
     * File consumer used by {@link Stream#forEach(Consumer)} to get attributes of a file.
     * Attributes of each file will be written out to the user.
     */
    private static class FileConsumer implements Consumer<Path> {

        private Environment env;

        /**
         * @param env Environment used for communication with a user.
         * @throws NullPointerException If given environment is {@code null}.
         */
        private FileConsumer(Environment env) {
            this.env = Objects.requireNonNull(env);
        }

        /**
         * Creates a string representation of path's attributes and outputs it to the user.
         *
         * @param path Path that should be analyzed.
         * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If messages could not be sent to user.
         */
        @Override
        public void accept(Path path) {
            String firstColumn = getFirstColumn(path);
            long size;
            String creationDate;
            try {
                size = Files.size(path);
                creationDate = getCreationDatetime(path);
            } catch (IOException e) {
                env.writeln("File " + path.getFileName().toString() + " could not be read.");
                return;
            }
            String name = path.getFileName().toString();
            env.writeln(String.format("%s %10d %s %s", firstColumn, size, creationDate, name));
        }

        /**
         * Creates a string that indicates if path is a directory, readable, writable and executable.
         *
         * @param path Path to check.
         * @return Formatted string that holds above attributes.
         */
        private String getFirstColumn(Path path) {
            return String.format("%c%c%c%c",
                    Files.isDirectory(path) ? 'd' : '-',
                    Files.isReadable(path) ? 'r' : '-',
                    Files.isWritable(path) ? 'w' : '-',
                    Files.isExecutable(path) ? 'x' : '-');
        }

        /**
         * Creates a string representing creation date/time.
         *
         * @param path Path whose creation datetime should be created.
         * @return String that represents creation datetime of a file.
         * @throws IOException In case attributes couldn't be read.
         */
        private String getCreationDatetime(Path path) throws IOException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BasicFileAttributeView faView = Files.getFileAttributeView(
                    path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
            );
            BasicFileAttributes attributes = faView.readAttributes();
            FileTime fileTime = attributes.creationTime();
            return sdf.format(new Date(fileTime.toMillis()));
        }
    }

}
