package security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Instance of this class is used to read data from input stream,
 * encrypt/decrypt it and send transformed data to output stream.
 *
 * @author Jan Capek
 */
public class FileCrypter {

    private Cipher cipher;
    private InputStream in;
    private OutputStream out;

    /**
     * Constructs a new {@code FileCrypter} object that encrypts/decrypts data
     * from input stream and writes it to output stream.<br>
     * Neither of the streams will be closed automatically, it is expected they will be closed externally.
     *
     * @param cipher Cipher that is used for encryption/decription.
     *               It has to be initialized.
     * @param in     Input stream of data to encrypt/decrypt.
     * @param out    Output stream on which transformed data will be outputted.
     * @throws NullPointerException If any of the arguments are {@code null}.
     * @throws IOException          If data couldn't be read or written.
     * @throws FileCrypterException If encryption/decryption couldn't be completed.
     */
    public FileCrypter(Cipher cipher, InputStream in, OutputStream out) throws IOException {
        this.cipher = Objects.requireNonNull(cipher);
        this.in = Objects.requireNonNull(in);
        this.out = Objects.requireNonNull(out);
        cipher();
    }

    /**
     * Encrypts/decrypts data from input stream and sends transformed data to output stream.
     *
     * @throws IOException          If data couldn't be read or written.
     * @throws FileCrypterException If encryption/decryption couldn't be completed.
     */
    private void cipher() throws IOException {
        byte[] readBytes = new byte[4 * 1024];
        for (int numberOfReadBytes = in.read(readBytes); numberOfReadBytes != -1; numberOfReadBytes = in.read(readBytes)) {
            byte[] newDataBlocks = cipher.update(readBytes, 0, numberOfReadBytes);
            out.write(newDataBlocks);
        }
        try {
            out.write(cipher.doFinal());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new FileCrypterException("Couldn't finish encryption/decryption.");
        }
        out.flush();
    }
}
