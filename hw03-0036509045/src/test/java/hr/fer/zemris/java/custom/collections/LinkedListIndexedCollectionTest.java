package hr.fer.zemris.java.custom.collections;

import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListIndexedCollectionTest {

    private LinkedListIndexedCollection c;

    @BeforeEach
    void setUp() {
        c = new LinkedListIndexedCollection();
        c.add(0);
        c.add(1);
        c.add(2);
        c.add(3);
        c.add(4);
    }

    //    empty constructor
    @Test
    void emptyConstructor() {
        new LinkedListIndexedCollection(); // will succeed if nothing happens
    }


    //    constructor with Collection argument
    @Test
    void constructFromNullArgument() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
    }

    @Test
    void constructFromEmptyCollection() {
        new LinkedListIndexedCollection(new LinkedListIndexedCollection());
    }

    @Test
    void constructFromNotEmptyCollection() {
        LinkedListIndexedCollection coll = new LinkedListIndexedCollection(c);
        assertArrayEquals(c.toArray(), coll.toArray());
    }

    //    isEmpty()
    @Test
    void isEmptyOnNotEmptyCollection() {
        assertFalse(c.isEmpty(), "Collection should not be empty.");
    }

    @Test
    void isEmptyOnEmptyCollection() {
        assertTrue(new LinkedListIndexedCollection().isEmpty(), "Collection should be empty.");
    }

    //    size()
    @Test
    void sizeOnNotEmpty() {
        assertEquals(5, c.size());
    }

    @Test
    void sizeOnEmpty() {
        assertEquals(0, new LinkedListIndexedCollection().size());
    }

    //    get()
    @Test
    void getIndexInBounds() {
        assertEquals(3, c.get(3));
    }

    @Test
    void getIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(9));
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(-1));
    }

    //    add()
    @Test
    void add() {
        LinkedListIndexedCollection coll = new LinkedListIndexedCollection();
        coll.add(32);
        coll.add("marko");
        assertEquals(2, coll.size());
        assertArrayEquals(new Object[]{32, "marko"}, coll.toArray());
    }

    @Test
    void addNull() {
        assertThrows(NullPointerException.class, () -> c.add(null));
    }

    //    addAll()
    @Test
    void addAllFromNotEmptyCollection() {
        LinkedListIndexedCollection coll = new LinkedListIndexedCollection();
        coll.addAll(c);

        assertArrayEquals(new Object[]{0, 1, 2, 3, 4}, coll.toArray());
    }

    @Test
    void addAllFromNull() {
        Object[] oldContent = c.toArray();
        assertThrows(NullPointerException.class, () -> c.addAll(null));
    }

    //    clear()
    @Test
    void clearEmpty() {
        LinkedListIndexedCollection coll = new LinkedListIndexedCollection();
        coll.clear();
        assertTrue(coll.isEmpty());
        assertEquals(0, coll.size());
    }

    @Test
    void clearNonEmpty() {
        c.clear();
        assertTrue(c.isEmpty());
        assertEquals(0, c.size());
    }

    //    insert
    @Test
    void insertOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> c.insert(324, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> c.insert(23, c.size() + 1));
    }

    @Test
    void insertNull() {
        assertThrows(NullPointerException.class, () -> c.insert(null, c.size()));
    }

    @Test
    void insertNormal() {
        c.insert("Kokica", 2);

        assertArrayEquals(new Object[]{0, 1, "Kokica", 2, 3, 4}, c.toArray());
    }

    @Test
    void insertNormal2() {
        c.insert("Kokica", 4);

        assertArrayEquals(new Object[]{0, 1, 2, 3, "Kokica", 4}, c.toArray());
    }

    @Test
    void insertAt0() {
        c.insert("Kokica", 0);

        assertArrayEquals(new Object[]{"Kokica", 0, 1, 2, 3, 4}, c.toArray());
    }

    @Test
    void insertAtEnd() {
        c.insert("Kokica", c.size());

        assertArrayEquals(new Object[]{0, 1, 2, 3, 4, "Kokica"}, c.toArray());
    }

    //    indexOf()
    @Test
    void indexOfAbsentElement() {
        assertEquals(-1, c.indexOf("makarska"));
    }

    @Test
    void indexOfPresentElement() {
        assertEquals(2, c.indexOf(2));
    }

    @Test
    void indexOfNull() {
        assertEquals(-1, c.indexOf(null));
    }

    //    remove(int index)
    @Test
    void removeFromIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> c.remove(c.size()));
    }

    @Test
    void removeFromIndexInBounds() {
        int oldSize = c.size();
        c.remove(c.indexOf(2));
        assertEquals(1, oldSize - c.size());
        assertFalse(c.contains(2));
    }

    //    contains()
    @Test
    void containsNull() {
        assertFalse(c.contains(null));
    }

    @Test
    void containsPresentObject() {
        assertTrue(c.contains(3));
    }

    @Test
    void containsAbsentObject() {
        assertFalse(c.contains("IWGEW#"));
    }

    //    remove(Object obj)
    @Test
    void removePresentObject() {
        assertTrue(c.remove(Integer.valueOf(3)));
        assertFalse(c.contains(3));
        assertEquals(-1, c.indexOf(3));
    }

    @Test
    void removeNull() {
        int oldSize = c.size();
        assertFalse(c.remove(null));
        assertEquals(oldSize, c.size());
    }

    @Test
    void removeAbsentObject() {
        int oldSize = c.size();
        assertFalse(c.remove("marko"));
        assertEquals(oldSize, c.size());
    }

    //    toArray()
    @Test
    void toArrayEmptyCollection() {
        assertArrayEquals(new Object[]{}, new LinkedListIndexedCollection().toArray());
    }

    @Test
    void toArrayNotEmptyCollection() {
        assertArrayEquals(new Object[]{0, 1, 2, 3, 4}, c.toArray());
    }

    //    forEach()
    @Test
    void forEachWithNullProcessor() {
        assertThrows(NullPointerException.class, () -> c.forEach(null));
    }

    @Test
    void forEach() {
        var processor = new Processor() {
            int size;

            @Override
            public void process(Object value) {
                size++;
            }
        };

        c.forEach(processor);

        assertEquals(5, processor.size);
    }

    @Test
    void elementsGetterTest() {
        ElementsGetter getter = c.createElementsGetter();

        assertEquals(0, getter.getNextElement());
        assertEquals(1, getter.getNextElement());
        assertEquals(2, getter.getNextElement());
        assertEquals(3, getter.getNextElement());
        assertEquals(4, getter.getNextElement());
        assertThrows(NoSuchElementException.class, () -> getter.getNextElement());
    }

    @Test
    void elementsGetterWithConcurrentModification() {
        ElementsGetter getter = c.createElementsGetter();

        getter.getNextElement();
        c.remove(0);
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }

    @Test
    void forEachRemainingElementsGetter() {
        ElementsGetter getter = c.createElementsGetter();
        getter.getNextElement();

        Collection b = new LinkedListIndexedCollection();
        getter.forEachRemaining(b::add);
        assertArrayEquals(new Object[]{1, 2, 3, 4}, b.toArray());
    }

    @Test
    void addAllSatisfyingTest() {
        Collection b = new LinkedListIndexedCollection();
        b.addAllSatisfying(c, o -> (Integer)o > 2);
        assertArrayEquals(new Object[]{3, 4}, b.toArray());
    }
}