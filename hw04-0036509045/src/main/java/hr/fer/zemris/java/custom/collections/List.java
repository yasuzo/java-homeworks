package hr.fer.zemris.java.custom.collections;

/**
 * List represents an ordered {@link Collection}.
 * Each element is on the specific position in a collection and can be fetched or removed from specific index.
 * This interface also offers methods for inserting at specific index and searching an index of a value in a collection.
 *
 * @author Jan Capek
 */
public interface List<T> extends Collection<T> {

    /**
     * Returns the object that is stored in collection at position {@code index} (counting from 0).
     *
     * @param index Index of element which is to be retrieved.
     * @return Element at given index.
     */
    T get(int index);

    /**
     * Inserts (does not overwrite) given value at the given position in array.
     * Valid positions are from 0 to size of the collection.
     *
     * @param value    Value to be inserted.
     * @param position Position where the value should be inserted.
     */
    void insert(T value, int position);

    /**
     * Searches the collection and returns the index of the first occurrence of the given value if it is in the collection.
     *
     * @param value Value to search for in the collection.
     * @return Index of the first occurrence of the value in collection if found.
     */
    int indexOf(Object value);

    /**
     * Removes element at specified index from collection. Element that was previously at location index+1
     * after this operation is on location index, etc.
     *
     * @param index Index of an element that needs to be removed.
     */
    void remove(int index);
}
