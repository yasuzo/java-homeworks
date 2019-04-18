package hr.fer.zemris.java.hw06.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;

/**
 * Instance of this class is used to read data from input stream,
 * encrypt/decrypt it and send transformed data to output stream.
 *
 * @author Jan Capek
 */
public class FileCrypter {

    private Cipher cipher;
    private SecretKeySpec keySpec;
    private AlgorithmParameterSpec paramSpec;

    /**
     * Constructs a new {@code FileCrypter} object that encrypts/decrypts data
     * from input stream and writes it to output stream.<br>
     * Neither of the streams will be closed automatically, it is expected they will be closed externally.
     *
     * @param cipher Cipher that is used for encryption/decription.
     *               It has to be initialized.
     * @param keySpec Secret key for encryption/decryption.
     * @param paramSpec Specifies encryption/decryption algorithm specifications.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    public FileCrypter(Cipher cipher, SecretKeySpec keySpec, AlgorithmParameterSpec paramSpec) {
        this.cipher = Objects.requireNonNull(cipher);
        this.keySpec = Objects.requireNonNull(keySpec);
        this.paramSpec = Objects.requireNonNull(paramSpec);
    }

    /**
     * Encrypts/decrypts data from input stream and sends transformed data to output stream.
     *
     * @throws IOException          If data couldn't be read or written.
     * @throws FileCrypterException If encryption/decryption couldn't be completed.
     */
    private void cipher(InputStream in, OutputStream out) throws IOException {
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

    /**
     * Encrypts data coming from input stream and sends transformed data to output stream.
     *
     * @param in Data source.
     * @param out Stream to which transformed data will be sent.
     * @throws InvalidAlgorithmParameterException If algorithm parameters are not valid.
     * @throws InvalidKeyException If key is not valid.
     * @throws IOException If data could not be read/written.
     * @throws FileCrypterException If data could not be encrypted.
     */
    public void encrypt(InputStream in, OutputStream out) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
        cipher(in, out);
    }

    /**
     * Decrypts data coming from input stream and sends transformed data to output stream.
     *
     * @param in Data source.
     * @param out Stream to which transformed data will be sent.
     * @throws InvalidAlgorithmParameterException If algorithm parameters are not valid.
     * @throws InvalidKeyException If key is not valid.
     * @throws IOException If data could not be read/written.
     * @throws FileCrypterException If data could not be decrypted.
     */
    public void decrypt(InputStream in, OutputStream out) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
        cipher(in, out);
    }

    /**
     * Sets new key specification for {@link Cipher} object.
     *
     * @param keySpec New key.
     * @throws NullPointerException If given key is {@code null}.
     */
    public void setKeySpec(SecretKeySpec keySpec) {
        this.keySpec = Objects.requireNonNull(keySpec);
    }

    /**
     * Sets new algorithm parameter specification for {@link Cipher} object.
     *
     * @param paramSpec New algorithm parameter.
     * @throws NullPointerException If given spec is {@code null}.
     */
    public void setParamSpec(AlgorithmParameterSpec paramSpec) {
        this.paramSpec = Objects.requireNonNull(paramSpec);
    }
}
