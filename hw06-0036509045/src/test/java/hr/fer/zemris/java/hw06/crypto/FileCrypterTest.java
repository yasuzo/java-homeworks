package hr.fer.zemris.java.hw06.crypto;

import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import static hr.fer.zemris.java.hw06.crypto.util.Util.hextobyte;

class FileCrypterTest {

    @Test
    void testDecryption() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        String keyText = "e52217e3ee213ef1ffdee3a192e2ac7e";
        String ivText = "000102030405060708090a0b0c0d0e0f";
        SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        FileCrypter fc = new FileCrypter(cipher, keySpec, paramSpec);

        Path pIn = Paths.get("src/test/resources/hw06part2.bin");
        Path pOut = Paths.get("src/test/resources/hw06part2.pdf");

        InputStream in = new BufferedInputStream(Files.newInputStream(pIn));
        OutputStream out = new BufferedOutputStream(Files.newOutputStream(pOut));

        fc.decrypt(in, out);

        in.close();
        out.close();
    }
}