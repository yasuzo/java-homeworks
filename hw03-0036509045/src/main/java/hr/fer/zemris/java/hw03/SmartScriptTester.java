package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SmartScriptTester {

    public static void main(String[] args) {

//        if(args.length != 1) {
//            System.out.println("Wrong argument count.");
//            return;
//        }

        String filepath = "src/main/resources/doc1.txt";

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

    public static String createOriginalDocumentBody(DocumentNode node) {
        return node.toString();
    }
}
