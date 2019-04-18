package hr.fer.zemris.java.hw06.crypto.commands;

import hr.fer.zemris.java.hw06.crypto.FileCrypter;
import hr.fer.zemris.java.hw06.crypto.FileCrypterException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Objects;

/**
 * Command used for file encryption. It outputs appropriate message to standard output.
 *
 * @author Jan Capek
 */
public class EncryptCommand implements Command {

    private Path fileToEncrypt;
    private Path encryptedFile;
    private FileCrypter fileCrypter;

    /**
     * Creates a new command used for encryption.
     *
     * @param fileToEncrypt File that needs to be encrypted.
     * @param encryptedFile Destination file.
     * @param fileCrypter   Object used to encrypt a file.
     * @throws NullPointerException     If any of the arguments are {@code null}.
     * @throws IllegalArgumentException If {@code fileToEncrypt} is not readable.
     */
    public EncryptCommand(Path fileToEncrypt, Path encryptedFile, FileCrypter fileCrypter) {
        if (Files.isReadable(fileToEncrypt) == false) {
            throw new IllegalArgumentException("File that needs to be encrypted isn't readable.");
        }
        this.fileToEncrypt = Objects.requireNonNull(fileToEncrypt);
        this.encryptedFile = Objects.requireNonNull(encryptedFile);
        this.fileCrypter = Objects.requireNonNull(fileCrypter);
    }

    /**
     * Encrypts a file and creates a new encrypted file.
     * This method will also output appropriate message to user.
     */
    @Override
    public void execute() {
        try (InputStream in = new BufferedInputStream(Files.newInputStream(fileToEncrypt));
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(encryptedFile))) {
            fileCrypter.encrypt(in, out);
        } catch (FileCrypterException e) {
            System.out.println("File couldn't be encrypted.");
            return;
        } catch (IOException e) {
            System.out.print("Error occurred while reading/writing to disk.");
            return;
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Invalid algorithm parameter specification was provided - encryption couldn't be completed.");
            return;
        } catch (InvalidKeyException e) {
            System.out.println("Invalid key was provided - encryption couldn't be completed.");
            return;
        }

        System.out.format("Encryption completed. Generated file %s based on file %s.", encryptedFile.toString(), fileToEncrypt.toString());
    }
}
