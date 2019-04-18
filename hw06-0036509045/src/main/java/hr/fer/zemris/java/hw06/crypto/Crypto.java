package hr.fer.zemris.java.hw06.crypto;

import hr.fer.zemris.java.hw06.crypto.commands.CheckDigestCommand;
import hr.fer.zemris.java.hw06.crypto.commands.Command;
import hr.fer.zemris.java.hw06.crypto.commands.DecryptCommand;
import hr.fer.zemris.java.hw06.crypto.commands.EncryptCommand;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import static hr.fer.zemris.java.hw06.crypto.util.Util.hextobyte;

/**
 * Program that can check message digests of a file, encrypt a file or decrypt it.
 *
 * @author Jan Capek
 */
public class Crypto {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        Command command = null;
        Scanner sc = new Scanner(System.in);
        switch (args[0]) {
            case "checksha":
                command = createCheckShaCommand(sc, args);
                break;
            case "encrypt":
            case "decrypt":
                command = createEncryptDecryptCommand(sc, args);
                break;
            default:
                System.out.println("Command not recognized.");
        }
        sc.close();

        if (command != null) {
            command.execute();
        }

    }

    /**
     * Creates a new {@link EncryptCommand} or {@link DecryptCommand} command if possible, if not, null is returned.
     *
     * @param sc   Scanner used to read user's input with.
     * @param args Program arguments.
     * @return {@link EncryptCommand} or {@link DecryptCommand} if a command could be created, {@code null} otherwise.
     */
    private static Command createEncryptDecryptCommand(Scanner sc, String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return null;
        }
        Path originalFile = Paths.get(args[1]);
        Path transformedFile = Paths.get(args[2]);

//        get password and init vector
        System.out.format("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
        String password = sc.nextLine().trim();
        System.out.format("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
        String initVector = sc.nextLine().trim();

        SecretKeySpec keySpec;
        AlgorithmParameterSpec paramSpec;
        Cipher cipher;
        try {
//            create and algorithm specs
            keySpec = new SecretKeySpec(hextobyte(password), "AES");
            paramSpec = new IvParameterSpec(hextobyte(initVector));

//            create cipher
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (IllegalArgumentException e) {
            System.out.println("Key and/or initialization vector consists of illegal characters.");
            return null;
        }catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//            never gonna happen
            System.out.println("Unexpected error occured.");
            return null;
        }

//        create file crypter
        FileCrypter fc = new FileCrypter(cipher, keySpec, paramSpec);

//        create appropriate command
        try {
            if (args[0].equals("encrypt")) {
                return new EncryptCommand(originalFile, transformedFile, fc);
            }
            return new DecryptCommand(originalFile, transformedFile, fc);
        } catch (IllegalArgumentException e) {
            System.out.println("Original file is not readable.");
            return null;
        }
    }

    /**
     * Creates a new {@link CheckDigestCommand} if possible.
     *
     * @param sc   Scanner used to read user's input with.
     * @param args Program arguments.
     * @return {@link CheckDigestCommand} if possible, {@code null} otherwise.
     */
    private static Command createCheckShaCommand(Scanner sc, String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments.");
            return null;
        }
        Path p = Paths.get(args[1]);
        System.out.format("Please provide expected sha-256 digest for %s:%n> ", p.toString());
        String expectedSha = sc.nextLine().trim();
        return new CheckDigestCommand(expectedSha, p);
    }
}
