package hr.fer.zemris.java.hw05.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldValueGettersTest {

    StudentRecord student = new StudentRecord("0000000000", "Marko", "Markic", 5);

    @Test
    void testJmbagGetter() {
        assertEquals("0000000000", FieldValueGetters.JMBAG.get(student));
    }

    @Test
    void testLastNameGetter() {
        assertEquals("Markic", FieldValueGetters.LAST_NAME.get(student));
    }

    @Test
    void testFirstNameGetter() {
        assertEquals("Marko", FieldValueGetters.FIRST_NAME.get(student));
    }

    @Test
    void testGetterNull() {
        assertThrows(NullPointerException.class, () -> FieldValueGetters.JMBAG.get(null));
        assertThrows(NullPointerException.class, () -> FieldValueGetters.FIRST_NAME.get(null));
        assertThrows(NullPointerException.class, () -> FieldValueGetters.LAST_NAME.get(null));
    }
}