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
        assertEquals(
                new SmartScriptToken(
                        SmartScriptTokenType.NORMAL_TEXT,
                        "Example { bla } blu {$=1$}. Nothing interesting {=here}."
                ),
                lexer.nextToken()
        );
        assertEquals(new SmartScriptToken(SmartScriptTokenType.EOF, null), lexer.nextToken());
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

    @Test
    void nextTokenMixedStates() {
        String input = "This is some king of \\{$ for test $}{$= i -.3 + i * @sin \"0.000\" @decfmt $}";
        lexer = new SmartScriptLexer(input);
        assertEquals(new SmartScriptToken(SmartScriptTokenType.NORMAL_TEXT, "This is some king of {$ for test $}"), lexer.nextToken());
        assertEquals(new SmartScriptToken(SmartScriptTokenType.OPEN_TAG_BRACKET, "{$"), lexer.nextToken());
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.KEYWORD, "="),
                new SmartScriptToken(SmartScriptTokenType.IDENTIFIER, "i"),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, -.3),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "+"),
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

    @Test
    void nextTokenTagStateNumbers() {
        String input = "3.23 .23 -.13 324 323.34324 -23423 23. -23.";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, 3.23),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, .23),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, -.13),
                new SmartScriptToken(SmartScriptTokenType.INTEGER_CONSTANT, 324),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, 323.34324),
                new SmartScriptToken(SmartScriptTokenType.INTEGER_CONSTANT, -23423),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, 23.),
                new SmartScriptToken(SmartScriptTokenType.DOUBLE_CONSTANT, -23.),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        test(lexer, tokens);
    }

    @Test
    void nextTokenTagStateInvalidFunction() {
        String input = "@@adaa";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
    }

    @Test
    void nextTokenTagStateInvalidFunction2() {
        String input = "@Kad_21|aa";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        lexer.nextToken();
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
    }

    @Test
    void nextTokenTagStateInvalidFunction3() {
        String input = "@Kad_21|)aa";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        lexer.nextToken();
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
    }

    @Test
    void nextTokenTagStateInvalidVariable() {
        String input = "fdadsfaf{";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        lexer.nextToken();
        assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
    }

    @Test
    void nextTokenTagStateOperators() {
        String input = "*-+^/";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "*"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "-"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "+"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "^"),
                new SmartScriptToken(SmartScriptTokenType.OPERATOR, "/"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        test(lexer, tokens);
    }

    @Test
    void nextTokenTagStateStrings() {
        String input = "\"aaaa\\\"\" \"\naaabbb\" \"$}\"";
        lexer = new SmartScriptLexer(input);
        lexer.setState(SmartScriptLexerState.TAG);
        SmartScriptToken[] tokens = {
                new SmartScriptToken(SmartScriptTokenType.STRING, "aaaa\""),
                new SmartScriptToken(SmartScriptTokenType.STRING, "\naaabbb"),
                new SmartScriptToken(SmartScriptTokenType.STRING, "$}"),
                new SmartScriptToken(SmartScriptTokenType.EOF, null)
        };
        test(lexer, tokens);
    }
}