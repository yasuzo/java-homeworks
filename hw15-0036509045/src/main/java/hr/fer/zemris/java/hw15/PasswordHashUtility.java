package hr.fer.zemris.java.hw15;

import crypto.MDigest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Utility for hashing passwords and comparing them.
 *
 * @author Jan Capek
 */
public class PasswordHashUtility {

    /**
     * Hashes password using SHA-1 algorithm and returns a result.
     *
     * @param password Password to hash.
     * @return Password hash.
     * @throws NullPointerException If given string is {@code null}.
     */
    public static String hashPassword(String password) {
        Objects.requireNonNull(password);
        InputStream in = new ByteArrayInputStream(password.getBytes(StandardCharsets.UTF_8));
        MDigest mDigest = null;
        try {
            mDigest = new MDigest(in, MessageDigest.getInstance("SHA-1"));
        } catch (NoSuchAlgorithmException ignorable) {

        }
        try {
            in.close();
        } catch (IOException ignorable) {

        }
        return mDigest.toString();
    }

    /**
     * Matches password against password hash and returns result of the match.
     *
     * @param password Password.
     * @param hash     Hash.
     * @return {@code true} if hash matches password, {@code false} otherwise.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public static boolean matchToHash(String password, String hash) {
        Objects.requireNonNull(password);
        Objects.requireNonNull(hash);
        return hashPassword(password).equals(hash);
    }
}
