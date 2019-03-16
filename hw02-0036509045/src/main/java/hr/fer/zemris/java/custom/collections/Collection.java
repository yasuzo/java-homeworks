package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Class that reprisents a collection of Objects.
 *
 * @author Jan Capek
 */
public class Collection {

    protected Collection() {}

    /**
     * Method for checking if the collection is empty.
     *
     * @return {@code true} if the collection contains no objects and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns number of elements in the collection.
     *
     * @return Number of elements in the collection
     */
    public int size() {
        return 0;
    }

    /**
     * Adds value to the collection.
     *
     * @param value Value to add to collection
     */
    public void add(Object value) {}

    /**
     * Returns {@code true} only if the collection contains given value, as determined by {@code equals} method.
     *
     * @param value Value to check for presence
     * @return {@code true} if the value is present in the collection as determined by {@code equals} method, {@code false} otherwise
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Returns {@code true} only if the collection contains given value as determined by {@code equals} method and removes
     * one occurrence of it (in this class it is not specified which one).
     *
     * @param value Value to remove from the collection
     * @return {@code true} if the collection contains given value as determined by {@code equals} method, {@code false} otherwise
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns {@code null}.
     *
     * @return Array filled with contents of the collection
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Method calls processor.process(.) for each element of this collection. The order in which elements
     * will be sent is undefined in this class.
     *
     * @param processor Processor whose {@code process(.)} method will be called for each element in the collection
     */
    public void forEach(Processor processor) {}

    /**
     * Method adds into the current collection all elements from the given collection. <u>This other collection remains unchanged.</u>
     *
     * @param other Collection to add elements from
     */
    public void addAll(Collection other) {
        if(other == null) {
            return;
        }

        /**
         * Processor whose method {@code process(.)} will be called to add each element to a collection given to instance
         * of this class in a constructor.
         */
        class LocalProcessor extends Processor {

            private Collection c;

            public LocalProcessor(Collection c) {
                this.c = Objects.requireNonNull(c);
            }

            @Override
            public void process(Object value) {
//                add value to Collection c
                c.add(value);
            }
        }

//        Calls process(.) method for each of its element which will then be added to this collection by the process(.) method
        other.forEach(new LocalProcessor(this));
    }

    /**
     * Removes all elements from this collection.
     */
    public void clear() {}
}
