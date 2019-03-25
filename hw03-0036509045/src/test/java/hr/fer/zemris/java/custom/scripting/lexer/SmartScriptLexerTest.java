package hr.fer.zemris.java.custom.scripting.lexer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartScriptLexerTest {

    private SmartScriptLexer lexer;

    private static void test(SmartScriptLexer lexer, SmartScriptToken... tokens){
        for(SmartScriptToken token : tokens){
            SmartScriptToken next = lexer.nextToken();
            assertEquals(token, next);
        }
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
    }

    @Test
    void setStateNull() {
        lexer = new SmartScriptLexer("fsfgsggdsf");
        assertThrows(NullPointerException.class, () -> lexer.setState(null));
    }

    @Test
    void setStateNormal() {
        lexer = new SmartScriptLexer("fsfgsggdsf");
        lexer.setState(SmartScriptLexerState.TAG);
        lexer.setState(SmartScriptLexerState.BASIC);
    }

    @Test
    void getCurrentTokenNull() {
        assertNull(new SmartScriptLexer("").getCurrentToken());
    }

    @Test
    void getCurrentTokenGenerated() {
        lexer = new SmartScriptLexer("");
        lexer.nextToken();
        assertEquals(SmartScriptTokenType.EOF, lexer.getCurrentToken().getType());
    }

    @Test
    void nextTokenNormalText() {
        String input = "Example { bla } blu \\{$=1$}. Nothing interesting {=here}.";
        lexer = new SmartScriptLexer(input);
        assertEquals(SmartScriptTokenType.STRING, lexer.nextToken().getType());
        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    void nextTokenTagFor() {
        String input = "{$ FOR year @sin 10 $}";
        lexer = new SmartScriptLexer(input);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.KEYWORD, "for"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "year"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "sin"),
                new SmartScriptToken(SmartScriptTokenType.INTEGER_CONSTANT, Integer.valueOf(10)),
                new SmartScriptToken(SmartScriptTokenType.CLOSED_TAG_BRACKET, "$}"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        assertEquals(new SmartScriptToken(SmartScriptTokenType.OPEN_TAG_BRACKET, "{$"), lexer.nextToken());
        lexer.setState(SmartScriptLexerState.TAG);
        test(lexer, tokens);
    }

    @Test
    void nextTokenTagInvalidFunctionName() {
        String input = "{$ FOR year @@sin 10 $}";
        lexer = new SmartScriptLexer(input);
        lexer.nextToken(); // {$
        lexer.setState(SmartScriptLexerState.TAG);
        lexer.nextToken(); // for
        lexer.nextToken(); // year
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
    }

    @Test
    void nextTokenTagEnd() {
        String input = "{$EnD$}";
        lexer = new SmartScriptLexer(input);
        assertEquals(new SmartScriptToken(SmartScriptTokenType.OPEN_TAG_BRACKET, "{$"), lexer.nextToken());
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.KEYWORD, "end"),
                new SmartScriptToken(SmartScriptTokenType.CLOSED_TAG_BRACKET, "$}"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        test(lexer, tokens);
    }

    @Test
    void nextTokenTagForWithStrings() {
        String input = "{$ FOR sco_re \"-1\"-10.21 \"1\" $}";
        lexer = new SmartScriptLexer(input);
        assertEquals(new SmartScriptToken(SmartScriptTokenType.OPEN_TAG_BRACKET, "{$"), lexer.nextToken());
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.KEYWORD, "for"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "sco_re"),
                new SmartScriptToken(SmartScriptTokenType.STRING, "-1"),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, -10.21),
                new SmartScriptToken(SmartScriptTokenType.STRING, "1"),
                new SmartScriptToken(SmartScriptTokenType.CLOSED_TAG_BRACKET, "$}"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        test(lexer, tokens);
    }

    @Test
    void nextTokenTagEcho() {
        String input = "{$= i i * @sin \"0.000\" @decfmt $}";
        lexer = new SmartScriptLexer(input);
        assertEquals(new SmartScriptToken(SmartScriptTokenType.OPEN_TAG_BRACKET, "{$"), lexer.nextToken());
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.KEYWORD, "="),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "*"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "sin"),
                new SmartScriptToken(SmartScriptTokenType.STRING, "0.000"),
                new SmartScriptToken(SmartScriptTokenType.FUNCTION, "decfmt"),
                new SmartScriptToken(SmartScriptTokenType.CLOSED_TAG_BRACKET, "$}"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        test(lexer, tokens);
    }
}