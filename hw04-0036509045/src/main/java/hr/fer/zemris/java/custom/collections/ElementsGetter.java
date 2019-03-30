package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Interface for fetching elements of collections.
 *
 * @author Jan Capek
 */
public interface ElementsGetter<T> {
    /**
     * @return {@code true} if there is an element in collection that wasn't fetched, {@code false} otherwise.
     */
    boolean hasNextElement();

    /**
     * @return Next element of the collection.
     */
    T getNextElement();

    /**
     * Calls Processor's method {@link Processor#process(Object)} for each remaining element of collection.
     *
     * @param p Processor which will process each element.
     * @throws NullPointerException If processor {@code p} is {@code null}.
     */
    default void forEachRemaining(Processor<? super T> p) {
        Objects.requireNonNull(p);
        while (hasNextElement()) {
            p.process(getNextElement());
        }
    }
}
