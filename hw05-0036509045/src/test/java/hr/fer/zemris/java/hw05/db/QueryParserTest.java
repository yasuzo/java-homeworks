package hr.fer.zemris.java.hw05.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {

    @Test
    void isDirectQuery() {
        String directQuery = "   jmbag   \t = \"0032324\"";
        QueryParser parser = new QueryParser(directQuery);
        assertTrue(parser.isDirectQuery());

        String notDirectQuery = "jmbag=\"327864\" and firstName>=\"banana\"";
        parser = new QueryParser(notDirectQuery);
        assertFalse(parser.isDirectQuery());
    }

    @Test
    void getQueriedJMBAG() {
        String directQuery = "   jmbag   \t = \"0032324\"";
        QueryParser parser = new QueryParser(directQuery);
        assertEquals("0032324", parser.getQueriedJMBAG());
        assertEquals("0032324", parser.getQueriedJMBAG());
    }

    @Test
    void getQueriedJMBAGIndirectQuery() {
        String notDirectQuery = "jmbag=\"327864\" and firstName>=\"banana\"";
        QueryParser parser = new QueryParser(notDirectQuery);
        assertThrows(IllegalStateException.class, parser::getQueriedJMBAG);
    }

    @Test
    void getQueryAndParseTest() {
        String query = "firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>=\"0000000002\"" +
                " and jmbag<=\"0000000002\" and jmbag!=\"0000000004\"";
        QueryParser parser = new QueryParser(query);

        Object[] parsedExpressions = parser.getQuery().toArray();
        ConditionalExpression[] expected = {
                new ConditionalExpression(
                        FieldValueGetters.FIRST_NAME,
                        "A",
                        ComparisonOperators.GREATER),
                new ConditionalExpression(
                        FieldValueGetters.FIRST_NAME,
                        "C",
                        ComparisonOperators.LESS),
                new ConditionalExpression(
                        FieldValueGetters.LAST_NAME,
                        "B*ć",
                        ComparisonOperators.LIKE),
                new ConditionalExpression(
                        FieldValueGetters.JMBAG,
                        "0000000002",
                        ComparisonOperators.GREATER_OR_EQUALS),
                new ConditionalExpression(
                        FieldValueGetters.JMBAG,
                        "0000000002",
                        ComparisonOperators.LESS_OR_EQUALS),
                new ConditionalExpression(
                        FieldValueGetters.JMBAG,
                        "0000000004",
                        ComparisonOperators.NOT_EQUALS),
        };

        assertArrayEquals(expected, parsedExpressions);
    }

    @Test
    void parseInvalid() {
        String input = "jmbag ";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalid2() {
        String input = "jmbag =";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalid3() {
        String input = "jmbag ==";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalid4() {
        String input = "jmbag = =";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalidUnclosedString() {
        String input = "jmbag = \"dadasd";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalidAnd() {
        String input = "and jmbag = \"dadaf\"";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalidAnd2() {
        String input = "jmbag = \"dadaf\" and";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalidUnrecognizedAttribute() {
        String input = "banana = \"dadaf\"";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }

    @Test
    void parseInvalidMultipleOperators() {
        String input = "jmbag = != \"dadaf\"";
        assertThrows(QueryParserException.class, () -> new QueryParser(input));
    }
}