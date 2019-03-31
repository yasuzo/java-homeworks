package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Resizable array implementation of {@link List}.
 * This collection does not permit {@code null} values.
 *
 * @param <T> Type of objects in the collection.
 *
 * @author Jan Capek
 */
public class ArrayIndexedCollection<T> implements List<T> {

    private static final int DEFAULT_CAPACITY = 16;

    private int size;
    private T[] elements;

    //    modification counter
    private long modificationCount;

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

        elements = (T[]) new Object[initialCapacity];
    }

    /**
     * Constructs ArrayIndexedCollection containing the elements of the specified
     * collection, in the order they are returned by the collection.
     * Collection's capacity will either be capacity of the given collection or default capacity for this type of collection
     * depending on whichever is larger.
     *
     * @param other The collection whose elements are to be placed into this collection.
     * @throws NullPointerException If given collection is {@code null}.
     */
    public ArrayIndexedCollection(Collection<? extends T> other) {
        this(other, DEFAULT_CAPACITY);
    }

    /**
     * Constructs ArrayIndexedCollection containing the elements of the specified
     * collection, in the order they are returned by the collection.
     * Collection's capacity will either be capacity of the given collection or default capacity for this type of collection
     * depending on whichever is larger.
     *
     * @param other           The collection whose elements are to be placed into this collection.
     * @param initialCapacity Initial capacity of this collection.
     * @throws NullPointerException     If given collection is {@code null}.
     * @throws IllegalArgumentException If given initial capacity is less than 1.
     */
    public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
        Objects.requireNonNull(other);
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity cannot be less than 1!");
        }

        int capacity = Math.max(initialCapacity, other.size());
        elements = (T[]) new Object[capacity];

        addAll(other);
    }

    /**
     * {@inheritDoc}
     * Valid indexes are 0 to size-1.
     * Time complexity is O(1).
     *
     * @throws IndexOutOfBoundsException If given index is greater or equal to the size of the collection or is negative.
     */
    @Override
    public T get(int index) {
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
     * @param value Value to add to collection.
     * @throws NullPointerException If given value is {@code null}.
     */
    @Override
    public void add(T value) {
        insert(value, size);
    }

    /**
     * Resizes an array where elements are stored to twice the capacity of old array.
     */
    private void resize() {
        T[] oldArray = this.elements;

        this.elements = (T[]) new Object[oldArray.length * 2];

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
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        this.size = 0;
        modificationCount++;
    }

    /**
     * {@inheritDoc}
     * Time complexity is O(n) because other elements will need to be shifted.
     *
     * @throws NullPointerException      If given value is null.
     * @throws IndexOutOfBoundsException If position is not in the interval [0, size].
     */
    @Override
    public void insert(T value, int position) {
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
     * @param spotsNeeded Number of free spots needed in the backing array.
     * @throws IllegalArgumentException If number of requested spots is less than 1.
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
     * {@inheritDoc}
     * -1 if the value is not found.
     * Value can be null. Equality of the value is determined by the {@link Objects#equals(Object, Object)} method.
     * Time complexity of this method is O(n).
     *
     * @return Index of the first occurrence of the value in collection or -1 if the value is not in the collection.
     */
    @Override
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
     * {@inheritDoc}
     * Legal indexes are 0 to size-1.
     * Time complexity of this operation is O(n).
     *
     * @throws IndexOutOfBoundsException If index is less than 0 or equal or greater to the size of the collection.
     */
    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index cannot be equal or greater to the size or smaller than 0!");
        }

//        shift elements to the left by 1 spot
        for (int i = index; i < size - 1; i++) {
            elements[index] = elements[index + 1];
        }

        elements[size - 1] = null;
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
        if (indexOfElement == -1) {
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

    /**
     * Checks if {@code o} is equal to {@code this}.
     * Two ArrayIndexedCollections are equal if they have the same element count
     * and if elements on the same indexes are equal to one another determined by
     * {@link Objects#equals(Object, Object)}.
     *
     * @param o Object to check for equality.
     * @return {@code true} if collections are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayIndexedCollection<?> that = (ArrayIndexedCollection<?>) o;

        if (size != that.size) return false;
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], that.elements[i]) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        for (int i = 0; i < size; i++) {
            result = 31 * result + Objects.hashCode(elements[i]);
        }
        return result;
    }

    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new ArrayElementsGetter<>(this);
    }

    /**
     * Private static class for iterating over collection's elements.
     */
    private static class ArrayElementsGetter<E> implements ElementsGetter<E> {
        private long startingModificationCount;
        private int current;
        private ArrayIndexedCollection<E> c;

        private ArrayElementsGetter(ArrayIndexedCollection<E> c) {
            Objects.requireNonNull(c);
            this.c = c;
            startingModificationCount = c.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if (startingModificationCount != c.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the mean time.");
            }
            return current < c.size;
        }

        /**
         * {@inheritDoc}
         *
         * @throws NoSuchElementException If all elements have already been returned by this ElementsGetter.
         */
        @Override
        public E getNextElement() {
            if (hasNextElement() == false) {
                throw new NoSuchElementException("All elements were already returned.");
            }
            return c.elements[current++];
        }
    }
}
