package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.hw03.SmartScriptTester;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static hr.fer.zemris.java.hw03.SmartScriptTester.createOriginalDocumentBody;
import static org.junit.jupiter.api.Assertions.*;

class SmartScriptParserTest {

    private static final String[] FILE_PATHS = {
            "src/test/resources/doc1.txt",
            "src/test/resources/doc2.txt",
            "src/test/resources/doc3.txt",
            "src/test/resources/doc4.txt",
            "src/test/resources/doc5.txt",
            "src/test/resources/doc6.txt",
            "src/test/resources/doc7.txt"
    };

    private static final String[] EXPECTED_BODIES = {
            "src/test/resources/expected/doc1.txt",
            "src/test/resources/expected/doc2.txt",
            "src/test/resources/expected/doc3.txt",
            "src/test/resources/expected/doc4.txt",
            "src/test/resources/expected/doc5.txt",
            "src/test/resources/expected/doc6.txt",
            "src/test/resources/expected/doc7.txt"
    };

    private String loader(String filename) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try(InputStream is =
                    new FileInputStream(filename)) {
            byte[] buffer = new byte[1024];
            while(true) {
                int read = is.read(buffer);
                if(read<1) break;
                bos.write(buffer, 0, read);
            }
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch(IOException ex) {
            return null;
        }
    }

    @Test
    void parserTest() {
        for (int i = 0; i < FILE_PATHS.length; i++) {
            String docBody = loader(FILE_PATHS[i]);
            String expected = loader(EXPECTED_BODIES[i]);

            SmartScriptParser parser = new SmartScriptParser(docBody);
            DocumentNode document = parser.getDocumentNode();
            String originalDocumentBody = createOriginalDocumentBody(document);

            SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
            DocumentNode document2 = parser2.getDocumentNode();
            String originalDocumentBody2 = createOriginalDocumentBody(document2);

            assertEquals(expected, originalDocumentBody);
            assertEquals(originalDocumentBody, originalDocumentBody2);
            assertEquals(document, document2);
        }
    }

}