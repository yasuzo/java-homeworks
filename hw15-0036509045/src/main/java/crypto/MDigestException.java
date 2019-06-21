package crypto;

/**
 * Thrown by {@link MDigest} in case data couldn't be digested.
 *
 * @author Jan Capek
 */
public class MDigestException extends RuntimeException {

    public MDigestException() {
    }

    public MDigestException(String message) {
        super(message);
    }
}
