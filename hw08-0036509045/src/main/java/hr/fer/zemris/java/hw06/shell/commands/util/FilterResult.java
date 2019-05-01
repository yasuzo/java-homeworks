package hr.fer.zemris.java.hw06.shell.commands.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Instances of this class represent a single selected file that matched a particular regex.
 *
 * @author Jan Capek
 */
public class FilterResult {
    private Path file;
    private Matcher matcher;

    /**
     * Constructs a new FilterResult that holds a file at given path and a matcher used for grouping file's name.
     *
     * @param file Path to a file a new instance should hold.
     * @param matcher Matcher that groups parts of file's name.
     * @throws NullPointerException If any of the arguments are {@code null}.
     * @throws IllegalArgumentException If matcher does not match.
     */
    private FilterResult(Path file, Matcher matcher) {
        this.file = Objects.requireNonNull(file);
        this.matcher = Objects.requireNonNull(matcher);

        if(matcher.matches() == false) {
            throw new IllegalArgumentException("Matcher should match a file name.");
        }
    }

    /**
     * @return Name of the file that this object hold.
     */
    @Override
    public String toString() {
        return file.getFileName().toString();
    }

    /**
     * Returns number of capturing groups in the file's name.
     * Implicit group 0 is not in the count.
     *
     * @return Number of capturing groups in a file name.
     */
    public int numberOfGroups() {
        return matcher.groupCount();
    }

    /**
     * Group that was found in a name of the file.
     * Group at index 0 is a whole file name.
     * This has to be true for index to be valid: {@code 0 <= index <= nnumberOfGroups()}.
     *
     * @param index Index of the wanted group.
     * @return Group at given index.
     * @throws IndexOutOfBoundsException If index is invalid.
     */
    public String group(int index) {
        return matcher.group(index);
    }

    /**
     * Returns a list of {@link FilterResult} objects that hold paths to files matching a pattern in a given directory.
     *
     * @param dir Directory in which files should be matched.
     * @param pattern Pattern to match files' names against.
     * @return List of results that hold files that match the pattern.
     * @throws IllegalArgumentException If given path is not a directory.
     * @throws java.util.regex.PatternSyntaxException If given pattern is not valid.
     * @throws IOException In case there was a problem accessing a file system.
     */
    public static List<FilterResult> filter(Path dir, String pattern) throws IOException {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(pattern);
        if(Files.isDirectory(dir) == false) {
            throw new IllegalArgumentException();
        }

        Pattern p = Pattern.compile(pattern);

        return Arrays.stream(dir.toFile().listFiles()).map(File::toPath)
                .filter(path -> Files.isDirectory(path) == false && p.matcher(path.getFileName().toString()).matches())
                .map(path -> new FilterResult(path, p.matcher(path.getFileName().toString())))
                .collect(Collectors.toList());
    }
}
