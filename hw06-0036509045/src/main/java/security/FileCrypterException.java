package security;

/**
 * Exception thrown by {@link FileCrypter} in case an error occurred.
 *
 * @author Jan Capek
 */
public class FileCrypterException extends RuntimeException {

    public FileCrypterException() {
    }

    public FileCrypterException(String message) {
        super(message);
    }
}
