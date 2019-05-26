package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Demo program demonstrating visitor design pattern with {@link INodeVisitor}.
 *
 * @author Jan Capek
 */
public class TreeWriter {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong argument count.");
            return;
        }

        String filepath = args[0];

        String docBody;
        try {
            docBody = Files.readString(Paths.get(filepath));
        } catch (IOException e) {
            System.out.println("Could not read a file!");
            return;
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch (SmartScriptParserException e) {
            System.out.println("Unable to parse document! " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = createOriginalDocumentBody(document);
        System.out.println(originalDocumentBody); // should write something like original
    }

    /**
     * Creates original document body from root node of the document.
     *
     * @param node Root node of a document produced by a parser.
     * @return Original document.
     * @throws NullPointerException If given node is {@code null}.
     */
    public static String createOriginalDocumentBody(DocumentNode node) {
        WriterVisitor visitor = new WriterVisitor();
        node.accept(visitor);
        return visitor.toString();
    }

    /**
     * Node visitor that will write each node to string and create one final string.
     */
    private static class WriterVisitor implements INodeVisitor {

        StringBuilder sb = new StringBuilder();

        @Override
        public void visitTextNode(TextNode node) {
            Objects.requireNonNull(node);
            String nodeString = node.getText()
                    .replaceAll("\\\\", "\\\\\\\\")
                    .replaceAll("\\{\\$", "\\\\\\{\\$");
            sb.append(nodeString);
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            Objects.requireNonNull(node);
            sb.append(String.format("{$ FOR %s ", node.getVariable().asText()));
            sb.append(String.format("%s %s ", node.getStartExpression(), node.getEndExpression()));
            if(node.getStepExpression() != null) {
                sb.append(String.format("%s ", node.getStepExpression()));
            }
            sb.append("$}");

            int children = node.numberOfChildren();
            for (int i = 0; i < children; i++) {
                node.getChild(i).accept(this);
            }
            sb.append("{$ END $}");
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            Objects.requireNonNull(node);
            sb.append("{$= ");
            for (Element e : node.getElements()) {
                sb.append(String.format("%s ", e));
            }
            sb.append("$}");
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            Objects.requireNonNull(node);
            int childrenNumber = node.numberOfChildren();
            for (int i = 0; i < childrenNumber; i++) {
                node.getChild(i).accept(this);
            }
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
