package hr.fer.zemris.java.hw06.crypto;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MDigestTest {

    @Test
    void matchDigests() throws IOException, NoSuchAlgorithmException {
        Path p = Paths.get("src/test/resources/hw06test.bin");
        InputStream in = new BufferedInputStream(Files.newInputStream(p), 4 * 1024);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        MDigest mDigest = new MDigest(in, md);

        in.close();

        assertTrue(mDigest.matches("2e7b3a91235ad72cb7e7f6a721f077faacfeafdea8f3785627a5245bea112598"));
        assertFalse(mDigest.matches("d03d4424461e22a458c6c716395f07dd9cea2180a996e78349985eda78e8b800"));
    }
}