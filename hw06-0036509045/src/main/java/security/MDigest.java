package security;

import security.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

/**
 * Instances of this class are used to digest data from input stream and calculate hash using {@link MessageDigest} object.
 *
 * @author Jan Capek
 */
public class MDigest {

    /**
     * Data input stream.
     */
    private InputStream in;
    /**
     * Calculated digest in byte form.
     */
    private byte[] digest;
    /**
     * Calculated digest in string form.
     */
    private String stringDigest;
    /**
     * {@link MessageDigest} instance used for calculating digest.
     */
    private MessageDigest messageDigest;

    /**
     * Creates a new message digest object that reads from given input stream and calculate digest.
     * <b>This class does not buffer data internally</b>, it is important that correct type of input
     * stream is used for maximum performance. <br>
     * Instance of this class will not close given input stream, it needs to be closed externally.
     * Input stream can be closed right after constructor call since digest will happen automatically.
     *
     *
     * @param inputStream   Input stream of data to digest.
     * @param messageDigest {@link MessageDigest} object that will calculate the digest.
     * @throws NullPointerException If any of the parameters are {@code null}.
     * @throws MDigestException If data couldn't be read.
     */
    public MDigest(InputStream inputStream, MessageDigest messageDigest) {
        this.in = Objects.requireNonNull(inputStream);
        this.messageDigest = Objects.requireNonNull(messageDigest);
        digest();
    }

    /**
     * Digests data through input stream given in constructor and calculates hash.
     *
     * @throws MDigestException If data couldn't be read.
     */
    private void digest() {
        try {
            for (int read = in.read(); read != -1; read = in.read()) {
                messageDigest.update((byte) read);
            }
        } catch (IOException e) {
//            TODO: Maybe not the best idea to rethrow.
            throw new MDigestException("Couldn't read data.");
        }
        digest = messageDigest.digest();
    }

    /**
     * Compares digests for equality.
     *
     * @param expectedDigest Digest that needs to be compared.
     * @return {@code true} if digests are equal, {@code false} otherwise.
     * @throws NullPointerException If given digest is {@code null}.
     */
    public boolean matchDigests(String expectedDigest) {
        Objects.requireNonNull(expectedDigest);

        byte[] expected;
        try {
            expected = Util.hextobyte(expectedDigest);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return Arrays.equals(expected, digest);
    }

    /**
     * @return String representation of computed digest.
     */
    @Override
    public String toString() {
        return stringDigest = stringDigest == null ? Util.bytetohex(digest) : stringDigest;
    }
}
