package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ArrayIndexedCollectionTest {

    private ArrayIndexedCollection c;

    @BeforeEach
    void setUp() {
        c = new ArrayIndexedCollection(5);
        c.add(0);
        c.add(1);
        c.add(2);
        c.add(3);
        c.add(4);
    }

    //    empty constructor
    @Test
    void emptyConstructor() {
        new ArrayIndexedCollection(); // will succeed if nothing happens
    }

    //    constructor with initial capacity
    @Test
    void illegalCapacityGivenToConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
    }

    @Test
    void legalCapacityGivenToConstructor() {
        new ArrayIndexedCollection(324);
    }

    //    constructor with Collection argument
    @Test
    void constructFromNullArgument() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
    }

    @Test
    void constructFromEmptyCollection() {
        new ArrayIndexedCollection(new ArrayIndexedCollection());
    }

    @Test
    void constructFromNotEmptyCollection() {
        ArrayIndexedCollection coll = new ArrayIndexedCollection(c);
        assertArrayEquals(c.toArray(), coll.toArray());
    }

    //    constructor with Collection and initial capacity arguments
    @Test
    void constructFromLegalCapacityAndNullCollection() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 223));
    }

    @Test
    void constructFromLegalCapacityAndNotEmptyCollection() {
        ArrayIndexedCollection coll = new ArrayIndexedCollection(c, 21);
        assertArrayEquals(c.toArray(), coll.toArray());
    }

    @Test
    void constructFromIllegalCapacityAndNotEmptyCollection() {
        ArrayIndexedCollection coll = new ArrayIndexedCollection(c, -21);
        assertArrayEquals(c.toArray(), coll.toArray());
    }

    @Test
    void constructFromIllegalCapacityAndEmptyCollection() {
//        TODO: Might need to change this to expect IllegalArgumentException
        ArrayIndexedCollection coll = new ArrayIndexedCollection(new ArrayIndexedCollection(), -21);
    }

    //    isEmpty()
    @Test
    void isEmptyOnNotEmptyCollection() {
        assertFalse(c.isEmpty(), "Collection should not be empty.");
    }

    @Test
    void isEmptyOnEmptyCollection() {
        assertTrue(new ArrayIndexedCollection().isEmpty(), "Collection should be empty.");
    }

    //    size()
    @Test
    void sizeOnNotEmpty() {
        assertEquals(5, c.size());
    }

    @Test
    void sizeOnEmpty() {
        assertEquals(0, new ArrayIndexedCollection().size());
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
        ArrayIndexedCollection coll = new ArrayIndexedCollection();
        coll.add(32);
        coll.add("marko");
        assertEquals(2, coll.size());
        assertArrayEquals(new Object[]{32, "marko"}, coll.toArray());
    }

    @Test
    void addNull() {
        assertThrows(NullPointerException.class, () -> c.add(null));
    }

    @Test
    void addEnoughToResize() {
        ArrayIndexedCollection col = new ArrayIndexedCollection(1);
        for (int i = 0; i < 200; i++) {
            col.add(i);
        }

        assertEquals(200, col.size());
        assertFalse(col.isEmpty());
    }

    //    addAll()
    @Test
    void addAllFromNotEmptyCollection() {
        ArrayIndexedCollection coll = new ArrayIndexedCollection();
        coll.addAll(c);

        assertArrayEquals(new Object[]{0, 1, 2, 3, 4}, coll.toArray());
    }

    @Test
    void addAllFromNull() {
        Object[] oldContent = c.toArray();
        c.addAll(null);
        assertArrayEquals(oldContent, c.toArray());
    }

    //    clear()
    @Test
    void clearEmpty() {
        ArrayIndexedCollection coll = new ArrayIndexedCollection();
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
        assertArrayEquals(new Object[]{}, new ArrayIndexedCollection().toArray());
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
        class LocalProcessor extends Processor {
            int size;

            @Override
            public void process(Object value) {
                size++;
            }
        }

        LocalProcessor processor = new LocalProcessor();
        c.forEach(processor);

        assertEquals(5, processor.size);
    }
}