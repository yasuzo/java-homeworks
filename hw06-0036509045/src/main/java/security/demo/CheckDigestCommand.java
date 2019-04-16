package security.demo;

import security.MDigest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Command that compares an expected message digest against calculated digest of a file
 * whose path was given in constructor and outputs appropriate message weather they match or not.
 *
 * @author Jan Capek
 */
public class CheckDigestCommand implements Command {

    /**
     * Expected digest.
     */
    private String expectedDigest;
    /**
     * Path to file whose digest needs to be calculated.
     */
    private Path pathToFile;

    /**
     * Constructs a new command that checks digests and outputs appropriate message.
     *
     * @param expectedDigest Expected digest.
     * @param pathToFile     Path to file whose digest needs to be calculated.
     * @throws NullPointerException If any of the parameters are {@code null};
     */
    public CheckDigestCommand(String expectedDigest, Path pathToFile) {
        this.expectedDigest = Objects.requireNonNull(expectedDigest);
        this.pathToFile = Objects.requireNonNull(pathToFile);
    }

    /**
     * Compares an expected message digest against calculated one and outputs appropriate message to standard output.
     */
    @Override
    public void execute() {
        MDigest mDigest;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(pathToFile))) {
            mDigest = new MDigest(in, MessageDigest.getInstance("SHA-256"));
        } catch (IOException e) {
            System.out.println("Error occurred while trying to read a file.");
            return;
        } catch (NoSuchAlgorithmException ex) {
//            Never gonna happen.
            return;
        }

        if (mDigest.matches(expectedDigest)) {
            System.out.format("Digesting completed. Digest of %s matches expected digest.%n", pathToFile.toString());
            return;
        }

        System.out.format("Digesting completed. Digest of %s does not match the expected digest. " +
                "Digest was: %s%n", pathToFile.toString(), mDigest.toString());
    }
}
