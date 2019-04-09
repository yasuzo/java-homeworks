package hr.fer.zemris.java.hw05.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryFilterTest {

    @Test
    void constructorTestNull() {
        assertThrows(NullPointerException.class, () -> new QueryFilter(null));
    }

    @Test
    void accept() {
        QueryParser parser = new QueryParser("jmbag>\"0000000004\" and jmbag <= \"0000000007\" and jmbag!=\"0000000006\"");
        QueryFilter filter = new QueryFilter(parser.getQuery());

        StudentRecord s1 = new StudentRecord("0000000004", "Marko", "Banankic", 2);

        StudentRecord s2 = new StudentRecord("0000000005", "Marko", "Banankic", 2);
        StudentRecord s3 = new StudentRecord("0000000006", "Marko", "Banankic", 2);
        StudentRecord s4 = new StudentRecord("0000000007", "Marko", "Banankic", 2);

        StudentRecord s5 = new StudentRecord("0000000008", "Marko", "Banankic", 2);

        assertFalse(filter.accepts(s1));

        assertTrue(filter.accepts(s2));

        assertFalse(filter.accepts(s3));

        assertTrue(filter.accepts(s4));

        assertFalse(filter.accepts(s5));
    }
}