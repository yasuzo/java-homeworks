package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This program takes a file path from the command line,
 * parses it and reconstructs original document body which is then outputted to the console.
 *
 * @author Jan Capek
 */
public class SmartScriptTester {

    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("Wrong argument count.");
            return;
        }

        String filepath = args[0];

        String docBody;
        try {
            docBody = new String(
                    Files.readAllBytes(Paths.get(filepath)),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            System.out.println("Could not read a file!");
            return;
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch(SmartScriptParserException e) {
            System.out.println("Unable to parse document! " + e.getMessage());
            System.exit(-1);
        } catch(Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = createOriginalDocumentBody(document);
        System.out.println(originalDocumentBody); // should write something like original
        // content of docBody
    }

    /**
     * Creates original document body from root node of the document.
     *
     * @param node Root node of a document produced by a parser.
     * @return Original document.
     */
    public static String createOriginalDocumentBody(DocumentNode node) {
        return node.toString();
    }
}
