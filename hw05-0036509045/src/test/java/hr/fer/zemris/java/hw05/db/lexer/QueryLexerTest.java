package hr.fer.zemris.java.hw05.db.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryLexerTest {

    void test(Token[] expected, QueryLexer lexer) {
        for (Token exp : expected) {
            assertEquals(exp, lexer.next());
        }
        assertThrows(QueryLexerException.class, lexer::next);
        assertEquals(new Token(TokenType.EOF, null), lexer.getLastReturned());
    }

    @Test
    void getLastReturned() {
        QueryLexer lexer = new QueryLexer("");
        assertEquals(null, lexer.getLastReturned());
        lexer.next();
        assertEquals(new Token(TokenType.EOF, null), lexer.getLastReturned());
    }

    @Test
    void next() {
        String input = "jmbag=\"32432423\" aNd lastName!=\"Miric\" and banana<=\"A\tklasa A\"";
        Token[] expected = {
                new Token(TokenType.ATTRIBUTE, "jmbag"),
                new Token(TokenType.COMPARISON_OPERATOR, "="),
                new Token(TokenType.STRING, "32432423"),
                new Token(TokenType.LOGIC_OPERATOR, "and"),
                new Token(TokenType.ATTRIBUTE, "lastName"),
                new Token(TokenType.COMPARISON_OPERATOR, "!="),
                new Token(TokenType.STRING, "Miric"),
                new Token(TokenType.LOGIC_OPERATOR, "and"),
                new Token(TokenType.ATTRIBUTE, "banana"),
                new Token(TokenType.COMPARISON_OPERATOR, "<="),
                new Token(TokenType.STRING, "A\tklasa A"),
                new Token(TokenType.EOF, null)
        };
        test(expected, new QueryLexer(input));
    }

    @Test
    void nextInvalid() {
        String input = "!\t-";
        assertThrows(QueryLexerException.class, () -> new QueryLexer(input).next());
    }

    @Test
    void hasNext() {
        QueryLexer lexer = new QueryLexer("");
        assertTrue(lexer.hasNext());
        lexer.next();
        assertFalse(lexer.hasNext());
    }
}