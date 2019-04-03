package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ArrayIndexedCollectionTest {

    private static class A {
        int i;

        private A(int value) {
            i = value;
        }

        @Override
        public String toString() {
            return "A_" + i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            A a = (A) o;
            return i == a.i;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i);
        }
    }

    private static class B extends A {

        private B(int i) {
            super(i);
        }

        @Override
        public String toString() {
            return "B_" + i;
        }
    }

    private ArrayIndexedCollection<A> c;

    @BeforeEach
    void setUp() {
        c = new ArrayIndexedCollection(5);
        c.add(new A(0));
        c.add(new B(1));
        c.add(new A(2));
        c.add(new B(3));
        c.add(new A(4));
    }

    //    empty constructor
    @Test
    void emptyConstructor() {
        new ArrayIndexedCollection<>(); // will succeed if nothing happens
    }

    //    constructor with initial capacity
    @Test
    void illegalCapacityGivenToConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(0));
    }

    @Test
    void legalCapacityGivenToConstructor() {
        new ArrayIndexedCollection<>(324);
    }

    //    constructor with Collection argument
    @Test
    void constructFromNullArgument() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null));
    }

    @Test
    void constructFromEmptyCollection() {
        new ArrayIndexedCollection<>(new ArrayIndexedCollection<>());
    }

    @Test
    void constructFromNotEmptyCollection() {
        ArrayIndexedCollection<A> coll = new ArrayIndexedCollection<>(c);
        assertArrayEquals(c.toArray(), coll.toArray());
    }

    //    constructor with Collection and initial capacity arguments
    @Test
    void constructFromLegalCapacityAndNullCollection() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null, 223));
    }

    @Test
    void constructFromLegalCapacityAndNotEmptyCollection() {
        ArrayIndexedCollection<A> coll = new ArrayIndexedCollection<>(c, 21);
        assertArrayEquals(c.toArray(), coll.toArray());
    }

    @Test
    void constructFromIllegalCapacityAndNotEmptyCollection() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(c, -21));
    }

    @Test
    void constructFromIllegalCapacityAndEmptyCollection() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(new ArrayIndexedCollection<>(), -21));
    }

    //    isEmpty()
    @Test
    void isEmptyOnNotEmptyCollection() {
        assertFalse(c.isEmpty(), "Collection should not be empty.");
    }

    @Test
    void isEmptyOnEmptyCollection() {
        assertTrue(new ArrayIndexedCollection<>().isEmpty(), "Collection should be empty.");
    }

    //    size()
    @Test
    void sizeOnNotEmpty() {
        assertEquals(5, c.size());
    }

    @Test
    void sizeOnEmpty() {
        assertEquals(0, new ArrayIndexedCollection<>().size());
    }

    //    get()
    @Test
    void getIndexInBounds() {
        assertEquals(new B(3), c.get(3));
    }

    @Test
    void getIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(9));
        assertThrows(IndexOutOfBoundsException.class, () -> c.get(-1));
    }

    //    add()
    @Test
    void add() {
        ArrayIndexedCollection<Object> coll = new ArrayIndexedCollection<>();
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
        ArrayIndexedCollection<Integer> col = new ArrayIndexedCollection<>(1);
        for (int i = 0; i < 200; i++) {
            col.add(i);
        }

        assertEquals(200, col.size());
        assertFalse(col.isEmpty());
    }

    //    addAll()
    @Test
    void addAllFromNotEmptyCollection() {
        ArrayIndexedCollection<A> coll = new ArrayIndexedCollection<>();
        coll.addAll(c);

        assertArrayEquals(new Object[]{new A(0), new B(1), new A(2), new B(3), new A(4)}, coll.toArray());
    }

    @Test
    void addAllFromNull() {
        Object[] oldContent = c.toArray();
        assertThrows(NullPointerException.class, () -> c.addAll(null));
    }

    //    clear()
    @Test
    void clearEmpty() {
        ArrayIndexedCollection<Object> coll = new ArrayIndexedCollection<>();
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
        assertThrows(IndexOutOfBoundsException.class, () -> c.insert(new B(32), -1));
        assertThrows(IndexOutOfBoundsException.class, () -> c.insert(new A(23), c.size() + 1));
    }

    @Test
    void insertNull() {
        assertThrows(NullPointerException.class, () -> c.insert(null, c.size()));
    }

    @Test
    void insertNormal() {
        c.insert(new B(100), 2);

        assertArrayEquals(new Object[]{new A(0), new B(1), new B(100), new A(2), new B(3), new A(4)}, c.toArray());
    }

    //    indexOf()
    @Test
    void indexOfAbsentElement() {
        assertEquals(-1, c.indexOf("makarska"));
    }

    @Test
    void indexOfPresentElement() {
        assertEquals(2, c.indexOf(new A(2)));
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
        c.remove(c.indexOf(new A(2)));
        assertEquals(1, oldSize - c.size());
        assertFalse(c.contains(new A(2)));
    }

    //    contains()
    @Test
    void containsNull() {
        assertFalse(c.contains(null));
    }

    @Test
    void containsPresentObject() {
        assertTrue(c.contains(new B(3)));
    }

    @Test
    void containsAbsentObject() {
        assertFalse(c.contains("IWGEW#"));
    }

    //    remove(Object obj)
    @Test
    void removePresentObject() {
        B value = new B(3);
        assertTrue(c.remove(value));
        assertFalse(c.contains(value));
        assertEquals(-1, c.indexOf(value));
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
        assertArrayEquals(new Object[]{}, new ArrayIndexedCollection<>().toArray());
    }

    @Test
    void toArrayNotEmptyCollection() {
        assertArrayEquals(new Object[]{new A(0), new B(1), new A(2), new B(3), new A(4)}, c.toArray());
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
        ElementsGetter<?> getter = c.createElementsGetter();

        assertEquals(new A(0), getter.getNextElement());
        assertEquals(new B(1), getter.getNextElement());
        assertEquals(new A(2), getter.getNextElement());
        assertEquals(new B(3), getter.getNextElement());
        assertEquals(new A(4), getter.getNextElement());
        assertThrows(NoSuchElementException.class, () -> getter.getNextElement());
    }

    @Test
    void elementsGetterWithConcurrentModification() {
        ElementsGetter<A> getter = c.createElementsGetter();
        c.remove(0);
        assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
    }

    @Test
    void forEachRemainingElementsGetter() {
        ElementsGetter<A> getter = c.createElementsGetter();
        getter.getNextElement();

        Collection<A> b = new ArrayIndexedCollection<>();
        getter.forEachRemaining(b::add);
        assertArrayEquals(new Object[]{new B(1), new A(2), new B(3), new A(4)}, b.toArray());
    }

    @Test
    void forEachRemainingOfNullElementsGetter() {
        ElementsGetter<A> getter = c.createElementsGetter();
        getter.getNextElement();
        assertThrows(NullPointerException.class, () -> getter.forEachRemaining(null));
    }

    @Test
    void addAllSatisfyingTest() {
        Collection<A> b = new ArrayIndexedCollection<>();
        b.addAllSatisfying(c, o -> ((A) o).i > 2);
        assertArrayEquals(new Object[]{new B(3), new A(4)}, b.toArray());
    }

    @Test
    void equalsTest() {
        ArrayIndexedCollection<A> coll = new ArrayIndexedCollection<>(c);
        ArrayIndexedCollection<Integer> coll2 = new ArrayIndexedCollection<>();
        coll.forEach(value -> coll2.add(value.i));
        assertEquals(c, coll);
        assertNotEquals(coll, coll2);
    }
}