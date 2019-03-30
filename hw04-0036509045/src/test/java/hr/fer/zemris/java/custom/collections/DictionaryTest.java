package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {

    private Dictionary<String, Integer> d;

    @BeforeEach
    void setUp() {
        d = new Dictionary<>();
        d.put("kruska", 0);
        d.put("jabuka", 2);
        d.put("sljiva", 4);
    }

    @Test
    void isEmptyEmptyDictionary() {
        assertTrue(new Dictionary<>().isEmpty());
    }

    @Test
    void isEmptyNonEmptyDictionary() {
        assertFalse(d.isEmpty());
    }

    @Test
    void sizeNonEmptyDictionary() {
        assertEquals(3, d.size());
    }

    @Test
    void sizeEmptyDictionary() {
        assertEquals(0, new Dictionary<>().size());
    }

    @Test
    void clearEmpty() {
        Dictionary<String, Integer> dict = new Dictionary<>();
        dict.clear();
        assertTrue(dict.isEmpty());
        assertEquals(0, dict.size());
    }

    @Test
    void clearNonEmpty() {
        d.clear();
        assertTrue(d.isEmpty());
        assertEquals(0, d.size());
    }

    @Test
    void putNullKey() {
        assertThrows(NullPointerException.class, () -> d.put(null, 2));
    }

    @Test
    void putNormal() {
        int oldSize = d.size();
        d.put("kokica", 15);
        assertEquals(15, d.get("kokica"));
        assertEquals(1, d.size() - oldSize);
    }

    @Test
    void putNullValue() {
        int oldSize = d.size();
        d.put("kokica", null);
        assertEquals(null, d.get("kokica"));
        assertEquals(1, d.size() - oldSize);
    }

    @Test
    void getNonExistant() {
        assertEquals(null, d.get("kokica"));
    }

    @Test
    void getExistant() {
        assertEquals(4, d.get("sljiva"));
    }
}