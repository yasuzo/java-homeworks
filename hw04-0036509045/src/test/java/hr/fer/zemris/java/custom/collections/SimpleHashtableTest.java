package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHashtableTest {

    SimpleHashtable<String, Integer> table;

    void test(Object[] expected, SimpleHashtable<?, ?> table) {
        assertEquals(expected.length, table.size());
        for (Object o : expected) {
            Pair p = (Pair) o;
            assertEquals(p.second, table.get(p.first));
        }
    }

    @BeforeEach
    void setUp() {
        table = new SimpleHashtable<>(1);
        table.put("kruska", 0);
        table.put("jabuka", 2);
        table.put("sljiva", 4);
    }

    @Test
    void constructIllegalCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<>(0));
    }

    @Test
    void constructCapacity() {
        SimpleHashtable<?, ?> hashTable = new SimpleHashtable<>(128);
        SimpleHashtable<?, ?> hashTable2 = new SimpleHashtable<>(97);
        SimpleHashtable<?, ?> hashTable3 = new SimpleHashtable<>(1);
    }

    //    put()
    @Test
    void putNormal() {
        table.put("banana", 100);
        table.put("marelica", null);
        Pair[] expected = {
                new Pair("kruska", 0),
                new Pair("jabuka", 2),
                new Pair("sljiva", 4),
                new Pair("banana", 100),
                new Pair("marelica", null)
        };
        test(expected, table);
    }

    @Test
    void putNullKey() {
        assertThrows(NullPointerException.class, () -> table.put(null, 3));
    }

    //    get()
    @Test
    void get() {
        assertEquals(4, table.get("sljiva"));
    }

    @Test
    void getNonExistent() {
        assertEquals(null, table.get("banana"));
    }

    @Test
    void getNull() {
        assertEquals(null, table.get(null));
    }

    //    containsKey()
    @Test
    void containsKeyExistent() {
        assertTrue(table.containsKey("sljiva"));
    }

    @Test
    void containsKeyNonExistent() {
        assertFalse(table.containsKey("banana"));
    }

    @Test
    void containsKeyNull() {
        assertFalse(table.containsKey(null));
    }

    //    containsValue()
    @Test
    void containsValue() {
        assertFalse(table.containsValue("jabuka"));
        assertTrue(table.containsValue(4));
        assertFalse(table.containsValue(null));
        table.put("banana", null);
        assertTrue(table.containsValue(null));
    }

    //    remove()
    @Test
    void removeNormal() {
        table.remove("jabuka");
        Pair[] expected = {
                new Pair("kruska", 0),
                new Pair("sljiva", 4)
        };
        test(expected, table);
    }

    @Test
    void removeNonExistant() {
        table.remove("banana");
        Pair[] expected = {
                new Pair("kruska", 0),
                new Pair("jabuka", 2),
                new Pair("sljiva", 4)
        };
        test(expected, table);
    }

    @Test
    void removeNull() {
        table.remove(null);
        Pair[] expected = {
                new Pair("kruska", 0),
                new Pair("jabuka", 2),
                new Pair("sljiva", 4)
        };
        test(expected, table);
    }

    //    isEmpty()
    @Test
    void isEmpty() {
        assertFalse(table.isEmpty());
        assertTrue(new SimpleHashtable<>().isEmpty());
    }

    //    size
    @Test
    void size() {
        assertEquals(3, table.size());
        assertEquals(0, new SimpleHashtable<>().size());
    }

    //    iterator test
    @Test
    void iteratorTest() {
        Pair[] expected = {
                new Pair("jabuka", 2),
                new Pair("kruska", 0),
                new Pair("sljiva", 4)
        };

        Pair[] pairs = new Pair[table.size()];
        int i = 0;
        for (SimpleHashtable.TableEntry<String, Integer> entry : table) {
            pairs[i++] = new Pair(entry.getKey(), entry.getValue());
        }
        test(pairs, table);
        assertArrayEquals(expected, pairs);
    }

    @Test
    void iteratorRemoveTest() {
        Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table.iterator();
        int oldSize = table.size();

//        get next and remove it
        assertEquals("jabuka", it.next().getKey());
        it.remove();

//        check if last was removed
        assertFalse(table.containsKey("jabuka"));
        assertNull(table.get("jabuka"));
        assertEquals(1, oldSize - table.size());

        Pair[] expected = {
                new Pair("kruska", 0),
                new Pair("sljiva", 4)
        };
        Pair[] pairs = new Pair[table.size()];
        for (int i = 0; it.hasNext(); i++) {
            SimpleHashtable.TableEntry<?, ?> entry = it.next();
            pairs[i] = new Pair(entry.getKey(), entry.getValue());
        }
        test(pairs, table);
        assertArrayEquals(expected, pairs);
    }

    @Test
    void iteratorRemoveIllegal() {
        Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table.iterator();
        assertThrows(IllegalStateException.class, () -> it.remove());
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, () -> it.remove());
    }

    @Test
    void iteratorConcurrentModification() {
        Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table.iterator();
        assertTrue(it.hasNext());
        it.next();
        table.remove("jabuka");
        assertThrows(ConcurrentModificationException.class, () -> it.next());
    }

    @Test
    void iteratorForEachRemaining() {
        Iterator<SimpleHashtable.TableEntry<String, Integer>> it = table.iterator();

        assertEquals("jabuka", it.next().getKey());
        it.remove();

        Pair[] expected = {
                new Pair("kruska", 0),
                new Pair("sljiva", 4)
        };

        ArrayIndexedCollection<Pair> pairs = new ArrayIndexedCollection<>();
        it.forEachRemaining(entry -> pairs.add(new Pair(entry.getKey(), entry.getValue())));
        assertArrayEquals(expected, pairs.toArray());
        test(expected, table);
    }

    private static class Pair {
        private Object first, second;

        public Pair(Object first, Object second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(first, pair.first) &&
                    Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}