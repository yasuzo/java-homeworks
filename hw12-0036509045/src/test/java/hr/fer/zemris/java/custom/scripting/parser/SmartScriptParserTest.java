package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.demo.TreeWriter;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class SmartScriptParserTest {

    private static final String[] FILE_PATHS = {
            "src/test/resources/doc1.txt",
            "src/test/resources/doc2.txt",
            "src/test/resources/doc3.txt",
            "src/test/resources/doc4.txt",
            "src/test/resources/doc5.txt",
            "src/test/resources/doc6.txt",
            "src/test/resources/doc7.txt",
            "src/test/resources/doc8.txt"
    };

    private static final String[] EXPECTED_BODIES = {
            "src/test/resources/expected/doc1.txt",
            "src/test/resources/expected/doc2.txt",
            "src/test/resources/expected/doc3.txt",
            "src/test/resources/expected/doc4.txt",
            "src/test/resources/expected/doc5.txt",
            "src/test/resources/expected/doc6.txt",
            "src/test/resources/expected/doc7.txt",
            "src/test/resources/expected/doc8.txt"
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
            String originalDocumentBody = TreeWriter.createOriginalDocumentBody(document);

            SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
            DocumentNode document2 = parser2.getDocumentNode();
            String originalDocumentBody2 = TreeWriter.createOriginalDocumentBody(document2);

            assertEquals(expected, originalDocumentBody);
            assertEquals(originalDocumentBody, originalDocumentBody2);
            assertEquals(document, document2);
        }
    }

    @Test
    void parseNull() {
        assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
    }

    @Test
    void parseEndWithoutForLoop() {
        String input = "{$end$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseForLoopWithFunctionInIt() {
        String input = "{$for i @sin 23 2$}{$end$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseForLoopWithTooFewArgs() {
        String input = "{$for i 2$} {$end$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseForLoopWithTooManyArgs() {
        String input = "{$for i 2 2 3 3$} {$end$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseForLoopWithOperators() {
        String input = "{$for i 2 2 - 3$} {$end$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseForLoopWithoutEnd() {
        String input = "{$for i 2 2 3$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseUnrecognizedKeyword() {
        String input = "{$ aaa i 2 2 3$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseTagWithMultipleKeywords() {
        String input = "{$ for for 2 2 3$}{$end$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseTagWithoutKeyword() {
        String input = "{$ 2 2 3$}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseTagWithoutClosingBracket() {
        String input = "{$= 2 2 3";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

    @Test
    void parseTagWithoutClosingStringQuote() {
        String input = "{$= 2 2 3 \"dddd $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(input));
    }

}