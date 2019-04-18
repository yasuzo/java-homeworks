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
public class DecryptCommand implements Command {

    private Path fileToDecrypt;
    private Path decryptedFile;
    private FileCrypter fileCrypter;

    /**
     * Creates a new command used for encryption.
     *
     * @param fileToDecrypt File that needs to be encrypted.
     * @param decryptedFile Destination file.
     * @param fileCrypter Object used to encrypt a file.
     * @throws NullPointerException If any of the arguments are {@code null}.
     * @throws IllegalArgumentException If {@code fileToDecrypt} is not readable.
     */
    public DecryptCommand(Path fileToDecrypt, Path decryptedFile, FileCrypter fileCrypter) {
        if(Files.isReadable(fileToDecrypt) == false) {
            throw new IllegalArgumentException("File that needs to be encrypted isn't readable.");
        }
        this.fileToDecrypt = Objects.requireNonNull(fileToDecrypt);
        this.decryptedFile = Objects.requireNonNull(decryptedFile);
        this.fileCrypter = Objects.requireNonNull(fileCrypter);
    }

    /**
     * Decrypts file and creates a resulting file.
     * This method will also output appropriate message to user.
     */
    @Override
    public void execute() {
        try(InputStream in = new BufferedInputStream(Files.newInputStream(fileToDecrypt));
            OutputStream out = new BufferedOutputStream(Files.newOutputStream(decryptedFile))) {
            fileCrypter.decrypt(in, out);
        }catch (FileCrypterException e) {
            System.out.println("File couldn't be decrypted.");
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

        System.out.format("Decryption completed. Generated file %s based on file %s.", decryptedFile.toString(), fileToDecrypt.toString());
    }
}
