package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Interface for collections.
 *
 * @param <T> Type of objects stored in the collection.
 * @author Jan Capek
 */
public interface Collection<T> {
    /**
     * Method for checking if the collection is empty.
     *
     * @return {@code true} if the collection contains no objects, {@code false} otherwise
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns number of elements in the collection.
     *
     * @return Number of elements in the collection
     */
    int size();

    /**
     * Adds value to the collection.
     *
     * @param value Value to add to collection
     */
    void add(T value);

    /**
     * Returns {@code true} only if the collection contains given value.
     *
     * @param value Value to check for presence.
     * @return {@code true} if the value is present in the collection, {@code false} otherwise.
     */
    boolean contains(Object value);

    /**
     * Returns {@code true} only if the collection contains given value and removes
     * one occurrence of it.
     *
     * @param value Value to remove from the collection.
     * @return {@code true} if the collection contains given value, {@code false} otherwise.
     */
    boolean remove(Object value);

    /**
     * Allocates new array with size equals to the size of collections, fills it with collection content and
     * returns the array.
     *
     * @return Array filled with contents of the collection.
     */
    Object[] toArray();

    /**
     * Method calls {@link Processor#process(Object)} for each element of this collection.
     *
     * @param processor Processor which will process each element of the collection.
     * @throws NullPointerException If {@code processor} is {@code null}.
     */
    default void forEach(Processor<? super T> processor) {
        Objects.requireNonNull(processor); // I know I don't need to check this here but I don't want to think about it

        ElementsGetter<T> getter = this.createElementsGetter();
        getter.forEachRemaining(processor);
    }

    /**
     * Method adds into the current collection all elements from the given collection. <u>This other collection remains unchanged.</u>
     *
     * @param other Collection to add elements from.
     * @throws NullPointerException If {@code other} is {@code null}.
     */
    default void addAll(Collection<? extends T> other) {
        Objects.requireNonNull(other);
        other.forEach(this::add);
    }

    /**
     * Removes all elements from the collection.
     */
    void clear();

    /**
     * Creates an instance of {@link ElementsGetter} used for getting elements from collection.
     *
     * @return ElementsGetter of the collection.
     */
    ElementsGetter<T> createElementsGetter();

    /**
     * Adds all elements from given colleciton {@code col} that satisfy {@link Tester#test(Object)} to this collection.
     *
     * @param col    Collection to add elements from.
     * @param tester Tester which will test all elements of collection {@code col} against statement.
     * @throws NullPointerException If either {@code col} or {@code tester} is {@code null}.
     */
    default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
        Objects.requireNonNull(col);
        Objects.requireNonNull(tester);
        ElementsGetter<? extends T> getter = col.createElementsGetter();
        getter.forEachRemaining(
                value -> {
                    if (tester.test(value)) {
                        this.add(value);
                    }
                });
    }
}
