package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable array implementation of {@link Collection}.
 * This collection does not permit {@code null} values.
 *
 * @author Jan Capek
 */
public class ArrayIndexedCollection implements Collection {

    private static final int DEFAULT_CAPACITY = 16;

    private int size;
    private Object[] elements;

//    modification counter
    private long modificationCount;

    /**
     * Private static class for iterating over collection's elements.
     */
    private static class ArrayElementsGetter implements ElementsGetter {
        private long startingModificationCount;
        private int current;
        private ArrayIndexedCollection c;

        private ArrayElementsGetter(ArrayIndexedCollection c) {
            Objects.requireNonNull(c);
            this.c = c;
            startingModificationCount = c.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if(startingModificationCount != c.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the mean time.");
            }
            return current < c.size;
        }

        /**
         * {@inheritDoc}
         * @throws NoSuchElementException If all elements have already been returned by this ElementsGetter.
         */
        @Override
        public Object getNextElement() {
            if(hasNextElement() == false) {
                throw new NoSuchElementException("All elements were already returned.");
            }
            return c.elements[current++];
        }
    }

    /**
     * Constructs ArrayIndexedCollection with default capacity
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Constructs ArrayIndexedCollection with {@code initialCapacity}.
     * Legal initial capacity is greater than 0.
     *
     * @param initialCapacity Initial capacity of the collection
     * @throws IllegalArgumentException If {@code initialCapacity} is less than 1
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity of a collection cannot be less than 1!");
        }

        elements = new Object[initialCapacity];
    }

    /**
     * Constructs ArrayIndexedCollection containing the elements of the specified
     * collection, in the order they are returned by the collection.
     * Collection's capacity will either be capacity of the given collection or default capacity for this type of collection
     * depending on whichever is larger.
     *
     * @param other The collection whose elements are to be placed into this collection
     * @throws NullPointerException If given collection is {@code null}
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, 1);
    }

    /**
     * Constructs ArrayIndexedCollection containing the elements of the specified
     * collection, in the order they are returned by the collection.
     * Collection's capacity will either be capacity of the given collection or default capacity for this type of collection
     * depending on whichever is larger.
     *
     * @param other           The collection whose elements are to be placed into this collection
     * @param initialCapacity Initial capacity of this collection
     * @throws NullPointerException If given collection is {@code null}
     */
//    TODO: Might need to change this to expect IllegalArgumentException
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        Objects.requireNonNull(other);

//        1 here is only in case that the size of the collection is 0 and the size of initialCapacity is illegal
        int capacity = Math.max(Math.max(initialCapacity, other.size()), 1);

        elements = new Object[capacity];

        addAll(other);
    }

    /**
     * Returns the object that is stored in backing array at position {@code index}. Valid indexes are 0 to size-1.
     * Time complexity is O(1)
     *
     * @param index Index of element which is to be retrieved
     * @return Element at given index
     * @throws IndexOutOfBoundsException If given index is greater or equal to the size of the collection or is negative
     */
    public Object get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Invalid index!");
        }

        return elements[index];
    }

    /**
     * Adds value to the collection. Value cannot be {@code null}.
     * Time complexity is <u>amortized O(1)</u>. (If there is not enough space in the backing array,
     * backing array will need to be resized)
     *
     * @param value Value to add to collection
     * @throws NullPointerException If given value is {@code null}
     */
    @Override
    public void add(Object value) {
        insert(value, size);
    }

    /**
     * Resizes an array where elements are stored to twice the capacity of old array
     */
    private void resize() {
        Object[] oldArray = this.elements;

        this.elements = new Object[oldArray.length * 2];

        for (int i = 0; i < this.size; i++) {
            this.elements[i] = oldArray[i];
        }
    }

    /**
     * {@inheritDoc}
     * This will {@code null} all elements in collection.
     * Time complexity of this is O(n).
     */
    @Override
    public void clear() {
        for(int i = 0; i < size; i++) {
            elements[i] = null;
        }
        this.size = 0;
        modificationCount++;
    }

    @Override
    public ElementsGetter createElementsGetter() {
        return new ArrayElementsGetter(this);
    }

    /**
     * Inserts (does not overwrite) given value at the given position in array.
     * Valid positions are from 0 to size of the collection.
     * Time complexity is O(n) because other elements will need to be shifted.
     *
     * @param value    Value to be inserted
     * @param position Position where the value should be inserted
     * @throws NullPointerException      If given value is null
     * @throws IndexOutOfBoundsException If position is not in the interval [0, size]
     */
    public void insert(Object value, int position) {
//        check if the value is null
        Objects.requireNonNull(value);
//        check if the position is a valid index
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Cannot insert at index less than 0 or greater than the size of the collection!");
        }

//        request free spots
        requestFreeSpots(1);

//        shift all elements at indexes greater or equal to 'position' by 1 to the right
        for (int i = size - 1; i >= position; i--) {
            elements[i + 1] = elements[i];
        }

        elements[position] = value;
        size++;
        modificationCount++;
    }

    /**
     * Ensures that there is always required number of free spots in the backing array.
     * If there is not enough free spots in the array, this method will call method {@code resize()} until there is
     * enough spots available for new elements.
     *
     * @param spotsNeeded Number of free spots needed in the backing array
     * @throws IllegalArgumentException If number of requested spots is less than 1
     */
    private void requestFreeSpots(int spotsNeeded) {
        if (spotsNeeded < 1) {
            throw new IllegalArgumentException("Cannot request less than 1 free spot!");
        }

        while (size + spotsNeeded > elements.length) {
            resize();
        }
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value or -1 if the value is
     * not found. Value can be null. Equality of the value is determined by the {@code Objects.equals(.)} method.
     * Time complexity of this method is O(n).
     *
     * @param value Value to search for in the collection
     * @return Index of the first occurrence of the value in collection or -1 if the value is not in the collection.
     */
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        for (int i = 0; i < size; i++) {
            if (Objects.equals(value, elements[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Removes element at specified index from collection. Element that was previously at location index+1
     * after this operation is on location index, etc. Legal indexes are 0 to size-1.
     * Time complexity of this operation is O(n).
     *
     * @param index Index of element that needs to be removes
     * @throws IndexOutOfBoundsException If index is less than 0 or equal or greater to the size of the collection
     */
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index cannot be equal or greater to the size or smaller than 0!");
        }

//        shift elements to the left by 1 spot
        for (int i = index; i < size - 1; i++) {
            elements[index] = elements[index + 1];
        }

        size--;
        modificationCount++;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    @Override
    public boolean remove(Object value) {
        int indexOfElement = indexOf(value);
        if(indexOfElement == -1) {
            return false;
        }
        remove(indexOfElement);
        return true;
    }

    @Override
    public Object[] toArray() {
        Object[] contents = new Object[size];

        for (int i = 0; i < size; i++) {
            contents[i] = elements[i];
        }

        return contents;
    }
}
