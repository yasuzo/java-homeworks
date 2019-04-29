package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.util.HexUtil;
import hr.fer.zemris.java.hw06.shell.commands.util.PathResolver;
import hr.fer.zemris.java.hw06.shell.commands.util.arg_checker.ArgumentChecker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shell command that dumps contents of a file in hexadecimal base.
 *
 * @author Jan Capek
 */
public class HexdumpCommand implements ShellCommand {

    private static String name;
    private static List<String> description;

    static {
        name = "hexdump";

        description = new ArrayList<>();
        description.add("Takes in a path which has to be a file and dumps its content in hexadecimal format.");
        description = Collections.unmodifiableList(description);
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args;
        try{
            args = ArgumentChecker.checkExecuteCommandArgs(env, arguments, 1);
        } catch (IllegalArgumentException e) {
            return ShellStatus.CONTINUE;
        }

        Path file = PathResolver.resolveRelativePath(env, Paths.get(args.get(0)));

//        is file a directory?
        if(Files.isDirectory(file)) {
            env.writeln("Cannot dump contents of a directory.");
            return ShellStatus.CONTINUE;
        }

//        hexdump
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file))) {
            dump(env, in);
        } catch (IOException e) {
            env.writeln("File either does not exists or is not readable.");
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Dumps hex representation of a file to the user.
     *
     * @param env Environment which is used to communicate with a user.
     * @param in Opened input data stream which contents will be dumped.
     * @throws IOException If reading from stream was not possible.
     * @throws NullPointerException If any of the arguments are {@code null}.
     * @throws hr.fer.zemris.java.hw06.shell.ShellIOException If communication with user was not possible.
     */
    private void dump(Environment env, InputStream in) throws IOException {
        Objects.requireNonNull(env);
        Objects.requireNonNull(in);

        byte[] line = new byte[16];
        int readLineBytes = 0;
        int readLines = 0;
        for (int dataBit = in.read(); dataBit != -1; dataBit = in.read()) {
//                line is finished, print it.
            if (readLineBytes == 16) {
                env.writeln(String.format("%08X: %s", readLines * 16, getFormattedHexLine(line, 16)));
                readLineBytes = 0;
                readLines++;
            }
            line[readLineBytes] = (byte) dataBit;
            readLineBytes++;
        }
//            check if there is a half finished line
        if (readLineBytes > 0) {
            env.writeln(String.format("%08X: %s", readLines * 16, getFormattedHexLine(line, readLineBytes)));
        }
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
     * Formats a hex line.
     *
     * @param data   Byte data that needs to be dumped.
     * @param length Length of valid bytes.
     * @return Formatted hex line.
     * @throws IllegalArgumentException If {@code length} is greater than {@code data.length}.
     * @throws NullPointerException If given array is {@code null}.
     */
    private String getFormattedHexLine(byte[] data, int length) {
        Objects.requireNonNull(data);
        if (length > data.length) {
            throw new IllegalArgumentException("Length of valid bytes cannot exceed length of array.");
        }

        if (length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (i == 8) {
                sb.append("| ");
            }
            sb.append(String.format("%2s ", i < length ? HexUtil.encodeByte(data[i]).toUpperCase() : ""));
        }
        sb.append("| ");
        for (int i = 0; i < length; i++) {
            char c = data[i] >= 32 && data[i] < 127 ? (char) data[i] : '.';
            sb.append(c);
        }
        return sb.toString();
    }
}
