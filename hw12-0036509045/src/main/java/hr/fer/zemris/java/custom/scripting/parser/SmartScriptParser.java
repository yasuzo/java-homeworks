package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Class for parsing text. This class uses {@link SmartScriptLexer} for
 * token generation.
 *
 * @author Jan Capek
 */
public class SmartScriptParser {

    private SmartScriptLexer lexer;
    //    main node of the document
    private DocumentNode documentNode;

    /**
     * Constructs a new {@code Parser} object for parsing given text.
     *
     * @param text Text to parse.
     * @throws NullPointerException If {@code text} is {@code null}.
     */
    public SmartScriptParser(String text) {
        lexer = new SmartScriptLexer(text);
        documentNode = new DocumentNode();
        parse();
    }

    /**
     * @return Root node of a document.
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Parses given text.
     *
     * @throws SmartScriptParserException If an error occurred.
     */
    private void parse() {
        Stack<Node> stack = new Stack<>();
        stack.push(documentNode);
        try {
            for (SmartScriptToken token = lexer.nextToken(); token.getType() != SmartScriptTokenType.EOF; token = lexer.nextToken()) {
                if (token.getType() == SmartScriptTokenType.NORMAL_TEXT) {
                    (stack.peek()).addChildNode(new TextNode((String) token.getValue()));
                    continue;
                }
                lexer.setState(SmartScriptLexerState.TAG);
                extractTag(stack);
            }
        } catch (SmartScriptLexerException e) {
            throw new SmartScriptParserException(e.getMessage());
        }
        stack.pop();
        if (stack.isEmpty() == false) {
            throw new SmartScriptParserException("There are unclosed tags!");
        }
    }


    /**
     * Extracts tags and sets lexer's state to {@link SmartScriptLexerState#BASIC}
     *
     * @param stack Stack used for node placement
     * @throws NullPointerException If {@code stack} is {@code null}.
     */
    private void extractTag(Stack<Node> stack) {
        Objects.requireNonNull(stack);
        SmartScriptToken token = lexer.nextToken();
        if (token.getType() != SmartScriptTokenType.KEYWORD) {
            throw new SmartScriptParserException("Wrong tag usage, keyword not found!");
        }
        switch ((String) token.getValue()) {
            case "for":
                ForLoopNode loop = extractForLoop();
                ((Node) stack.peek()).addChildNode(loop);
                stack.push(loop);
                break;
            case "=":
                ((Node) stack.peek()).addChildNode(extractEcho());
                break;
            case "end":
                extractEnd(stack);
                break;
            default:
                throw new SmartScriptParserException("Keyword not recognized!");
        }
        lexer.setState(SmartScriptLexerState.BASIC);
    }

    /**
     * Extracts {$= $} (echo) tag and all its elements.
     *
     * @return New echo node.
     * @throws SmartScriptParserException If a tag is invalid.
     * @throws SmartScriptLexerException  If EOF was found too soon or if a grammar error occurred.
     */
    private EchoNode extractEcho() {
        List<Element> elements = new ArrayList<>();
        for (SmartScriptToken token = lexer.nextToken(); token.getType() != SmartScriptTokenType.CLOSED_TAG_BRACKET; token = lexer.nextToken()) {
            Element el;
            switch (token.getType()) {
                case STRING:
                    el = new ElementString((String) token.getValue());
                    break;
                case DOUBLE_CONSTANT:
                    el = new ElementConstantDouble((Double) token.getValue());
                    break;
                case INTEGER_CONSTANT:
                    el = new ElementConstantInteger((Integer) token.getValue());
                    break;
                case IDENTIFIER:
                    el = new ElementVariable((String) token.getValue());
                    break;
                case FUNCTION:
                    el = new ElementFunction((String) token.getValue());
                    break;
                case OPERATOR:
                    el = new ElementOperator((String) token.getValue());
                    break;
                default:
                    throw new SmartScriptParserException("Wrong token types in echo.");
            }
            elements.add(el);
        }
//        array of elements
        Element[] elementArray = elements.toArray(new Element[elements.size()]);
        return new EchoNode(elementArray);
    }

    /**
     * Handles END tag.
     * Removes a for-loop element from the stack.
     *
     * @param stack Stack used for node placement.
     * @throws SmartScriptParserException If there isn't a for-loop to close or if a tag is invalid.
     * @throws SmartScriptLexerException  If grammar error occurred.
     * @throws NullPointerException       If {@code stack} is {@code null}.
     */
    private void extractEnd(Stack<Node> stack) {
        Objects.requireNonNull(stack);
//        check if there is garbage around end tag
        if (lexer.nextToken().getType() != SmartScriptTokenType.CLOSED_TAG_BRACKET) {
            throw new SmartScriptParserException("End statement must be alone in a tag and have a closing tag bracket.");
        }
//        check if there is a for-loop to close, if not throw
        if (stack.pop() instanceof ForLoopNode == false) {
            throw new SmartScriptParserException("Misplaced {$ END $}");
        }

    }

    /**
     * Extracts a for-loop tag
     *
     * @return For-loop node with all parameters.
     * @throws SmartScriptParserException If for-loop is not valid.
     * @throws SmartScriptLexerException  If grammar error occurred or EOF came too soon.
     */
    private ForLoopNode extractForLoop() {
        SmartScriptToken token = lexer.nextToken();
        if (token.getType() != SmartScriptTokenType.IDENTIFIER) {
            throw new SmartScriptParserException("For loop expression must start with a variable!");
        }
        ElementVariable variable = new ElementVariable((String) token.getValue());
        Element[] params = new Element[3];
        int i = 0;
        for (token = lexer.nextToken(); i < 3 && token.getType() != SmartScriptTokenType.CLOSED_TAG_BRACKET; token = lexer.nextToken()) {
            switch (token.getType()) {
                case STRING:
                    params[i] = new ElementString((String) token.getValue());
                    break;
                case INTEGER_CONSTANT:
                    params[i] = new ElementConstantInteger((Integer) token.getValue());
                    break;
                case DOUBLE_CONSTANT:
                    params[i] = new ElementConstantDouble((Double) token.getValue());
                    break;
                case IDENTIFIER:
                    params[i] = new ElementVariable((String) token.getValue());
                    break;
                default:
                    throw new SmartScriptParserException("For loop can only have variables and constants as arguments!");
            }
            i++;
        }
        if (token.getType() != SmartScriptTokenType.CLOSED_TAG_BRACKET) {
            throw new SmartScriptParserException("Too many arguments for for-loop.");
        }
        if (i < 2) {
            throw new SmartScriptParserException("Too few arguments for for-loop.");
        }
        return new ForLoopNode(variable, params[0], params[1], params[2]);
    }
}
