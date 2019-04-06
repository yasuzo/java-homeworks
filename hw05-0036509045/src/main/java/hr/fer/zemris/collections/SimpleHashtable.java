package hr.fer.zemris.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Class representing a simple hash table.
 * Overflow is solved by using a linked list.
 *
 * @param <K> Type of keys.
 * @param <V> Type of values that should be stored.
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

    private static final int DEFAULT_TABLE_SIZE = 16;
    private static final double FILL_RATIO = 0.75;

    private TableEntry<K, V>[] table;
    private int size;

    private long modificationCount;

    private int numberOfFilledSlots;

    /**
     * Constructs a new {@link SimpleHashtable} with default table capacity.
     */
    public SimpleHashtable() {
        this(DEFAULT_TABLE_SIZE);
    }

    /**
     * Constructs a new {@link SimpleHashtable} with size of the closest power of 2
     * that is equal or greater than given initial capacity.
     *
     * @param capacity Wanted initial size.
     * @throws IllegalArgumentException If wanted size is less than 1 or is too large.
     */
    public SimpleHashtable(int capacity) {
        capacity = closestPowerOf2(capacity); // this will throw an exception if initialSize is less than 1

        table = (TableEntry<K, V>[]) new TableEntry[capacity];
    }

    /**
     * Saves given value under given key.
     * If an entry with given key already exists,
     * this will overwrite existing value with the new one.
     *
     * @param key   Key to save value under.
     * @param value Value to save.
     * @throws NullPointerException If the key is {@code null}.
     */
    public void put(K key, V value) {
        Objects.requireNonNull(key);

        TableEntry<K, V> entry = getEntry(key);
        if (entry != null) {
            entry.setValue(value);
            return;
        }

        modificationCount++;

        entry = new TableEntry<>(key, value, null);
        putEntry(entry);
    }

    /**
     * Returns a value stored under a give key.
     * If key does not exists in the table, returns {@code null}.
     *
     * @param key Key under which wanted value is stored.
     * @return Value stored under the key or {@code null} if the key doesn't exist in the table.
     */
    public V get(Object key) {
        TableEntry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    /**
     * Checks if given key exists in the table.
     *
     * @param key Key to check for.
     * @return {@code true} if the key exists, {@code false} otherwise.
     */
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    /**
     * Checks if given value exists in the table.
     *
     * @param value Value to search for.
     * @return {@code true} if the value exists in the table, {@code false} otherwise.
     */
    public boolean containsValue(Object value) {
        for (TableEntry<K, V> entry : this) {
            if (Objects.equals(entry.value, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes an entry stored under given key.
     *
     * @param key Key of the entry that needs to be removed.
     */
    public void remove(Object key) {
        if (key == null) {
            return;
        }

        int slot = calculatePosition(key);

//        slot is already empty
        if (table[slot] == null) {
            return;
        }

//            check if the head of linked list has searched key
        if (Objects.equals(table[slot].key, key)) {
            TableEntry<K, V> temp = table[slot];
            table[slot] = temp.next;
            size--;
            modificationCount++;
//                slot has become empty -> decrease numberOfFilledSlots
            if (table[slot] == null) {
                numberOfFilledSlots--;
            }
            return;
        }

//            check if other entries in linked list have searched key
        for (TableEntry<K, V> entry = table[slot]; entry.next != null; entry = entry.next) {
            if (Objects.equals(entry.next.key, key) == false) {
                continue;
            }
            entry.next = entry.next.next;
            size--;
            modificationCount++;
            return; // "return" has to be here for early exit
        }

    }

    /**
     * Clears all entries from the table.
     */
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        numberOfFilledSlots = 0;
        modificationCount++;
    }

    /**
     * Check if the table is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return Number of entries in the table.
     */
    public int size() {
        return size;
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new LocalIterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (TableEntry<K, V> entry : this) {
            sb.append(String.format("%s, ", entry));
        }
        sb.append("]");
        return sb.toString().replaceAll(", ]$", "]");
    }

    /**
     * Called when a new table slot is filled,
     * this will decide if backing table should be resized.
     */
    private void newSlotFilling() {
        if ((double) (numberOfFilledSlots) / table.length >= FILL_RATIO) {
            resize();
        }
    }

    /**
     * Resizes backing table to twice the size of the old one
     * and fills a new one with entries from the old table.
     */
    private void resize() {
        TableEntry<K, V>[] oldTable = table;

        int newSize = oldTable.length * 2;
        while ((numberOfFilledSlots + 1.0) / newSize >= FILL_RATIO) {
            newSize *= 2;
        }
//        TODO: Check if an overflow happened.
        table = (TableEntry<K, V>[]) new TableEntry[newSize];

        size = 0;
        numberOfFilledSlots = 0;
        for (TableEntry<K, V> entry : oldTable) {
            for (; entry != null; entry = entry.next) {
                TableEntry<K, V> newEntry = new TableEntry<>(entry.key, entry.value, null);
                this.putEntry(newEntry);
            }
        }
    }

    /**
     * Adds given entry to the table.
     * This <b>does not</b> check for duplicate keys since this is a helper method.
     *
     * @param entry Entry to add to the table.
     * @throws NullPointerException if an entry is {@code null}.
     */
    private void putEntry(TableEntry<K, V> entry) {
        Objects.requireNonNull(entry);

        int slot = calculatePosition(entry.key);

//        there is no entries in the slot
        if (table[slot] == null) {
            table[slot] = entry;
            size++;
            numberOfFilledSlots++;
            newSlotFilling();
            return;
        }

//        get to the back of the list
        TableEntry<K, V> temp = table[slot];
        while (temp.next != null) {
            temp = temp.next;
        }
//        append new entry to the list
        temp.next = entry;
        size++;
    }

    /**
     * Finds an entry with given key and returns it.
     * If the entry is not found, {@code null} will be returned.
     *
     * @param key Key of the entry.
     * @return Entry with given key.
     */
    private TableEntry<K, V> getEntry(Object key) {
        if (key == null) {
            return null;
        }
        int slot = calculatePosition(key);
        for (TableEntry<K, V> temp = table[slot]; temp != null; temp = temp.next) {
            if (Objects.equals(temp.key, key)) {
                return temp;
            }
        }
        return null;
    }

    /**
     * Calculates a position in the table for given key.
     *
     * @param key Key to calculate a position for.
     * @return Position in the table for the given key.
     * @throws NullPointerException If the key is {@code null}.
     */
    private int calculatePosition(Object key) {
        Objects.requireNonNull(key);
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Calculates a smallest power of 2 that is equal or greater to the argument.
     *
     * @param a Reference number.
     * @return Power of 2 greater or equal to the {@code a}.
     * @throws IllegalArgumentException If given number is less than 1 or is too large for an integer.
     */
    private int closestPowerOf2(int a) {
        if (a < 1) {
            throw new IllegalArgumentException("Table size cannot be less than 1!");
        }

//        if a isn't a power of 2
        if ((a & (a - 1)) != 0) {
            a = a << 1;
            for (int temp = a & (a - 1); temp != 0; temp = a & (a - 1)) {
                a = temp;
            }
        }

//        check for overflow
        if (a < 0) {
            throw new IllegalArgumentException("Overflow happened, number is too large.");
        }

        return a;
    }

    /**
     * Class representing key-value pair for {@link SimpleHashtable} entries.
     *
     * @param <K> Type of keys.
     * @param <V> Type of values.
     */
    public static class TableEntry<K, V> {
        private K key;
        private V value;
        private TableEntry<K, V> next;

        /**
         * Constructs a new entry with given key and value.
         *
         * @param key   Key of the entry.
         * @param value Value to store under the key.
         * @param next  Next entry in the table.
         * @throws NullPointerException If {@code key} is {@code null}.
         */
        private TableEntry(K key, V value, TableEntry<K, V> next) {
            this.key = Objects.requireNonNull(key);
            this.value = value;
            this.next = next;
        }

        /**
         * Sets value of the entry.
         *
         * @param value New value.
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * @return Key under which the entry is stored.
         */
        public K getKey() {
            return key;
        }

        /**
         * @return Value of the entry.
         */
        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("%s=%s", Objects.toString(key), Objects.toString(value, "-null-"));
        }
    }

    /**
     * Iterator of a {@link SimpleHashtable.TableEntry} for {@link SimpleHashtable}.
     * If the collection is modified outside of the iterator, an instance of this class will
     * throw {@link ConcurrentModificationException}.
     *
     * @author Jan Capek
     */
    private class LocalIterator implements Iterator<TableEntry<K, V>> {

        /**
         * Position of the iterator in the table.
         */
        private int currentIndex;
        /**
         * Last element entry returned by the iterator.
         */
        private TableEntry<K, V> currentEntry;
        /**
         * Modification count that
         */
        private long modificationCount;
        /**
         * Holds how many elements are in the table that haven't yet been returned by the iterator.
         */
        private int numberOfElementsLeftToReturn;
        /**
         * Flag that signalizes if the current entry was already removed.
         */
        private boolean removedCurrent;

        /**
         * Creates a new iterator for the hash table.
         */
        private LocalIterator() {
            modificationCount = SimpleHashtable.this.modificationCount;
            numberOfElementsLeftToReturn = SimpleHashtable.this.size;
            currentIndex = -1;
            removedCurrent = false;
        }

        /**
         * {@inheritDoc}
         *
         * @throws ConcurrentModificationException If the collection was modified in the mean time outside of this iterator.
         */
        @Override
        public void remove() {
            if (currentEntry == null || removedCurrent) {
                throw new IllegalStateException("There is no element to remove, next() has already been " +
                        "called or an element has already been removed.");
            }

            checkConcurrentModification();

            SimpleHashtable.this.remove(currentEntry.key);
            modificationCount = SimpleHashtable.this.modificationCount;
            removedCurrent = true;
        }

        /**
         * {@inheritDoc}
         *
         * @throws ConcurrentModificationException If the table was modified in the mean time outside of this iterator.
         */
        @Override
        public void forEachRemaining(Consumer<? super TableEntry<K, V>> action) {
            Objects.requireNonNull(action);
            while (true) {
                try {
                    action.accept(this.next());
                } catch (NoSuchElementException e) {
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return numberOfElementsLeftToReturn > 0;
        }

        /**
         * {@inheritDoc}
         *
         * @throws ConcurrentModificationException If the table was modified in the mean time outside of this iterator.
         */
        @Override
        public TableEntry<K, V> next() {
            if (hasNext() == false) {
                throw new NoSuchElementException("There are no elements to return.");
            }

            checkConcurrentModification();

//            if current entry isn't null then go to the next one it points to (that is in the same slot).
            if (currentEntry != null) {
                currentEntry = currentEntry.next;
            }

            /* if now current entry points to null that means the end of the linked list
             * has been reached or no element has been returned yet by the iterator
             * and we should go to another slot */
            if (currentEntry == null) {
                for (currentIndex += 1; ; currentIndex++) {
                    if (table[currentIndex] != null) {
                        currentEntry = table[currentIndex];
                        break;
                    }
                }
            }
            numberOfElementsLeftToReturn--;
            removedCurrent = false;
            return currentEntry;
        }

        /**
         * @throws ConcurrentModificationException If the table was modified in the meantime outside of this iterator.
         */
        private void checkConcurrentModification() {
            if (modificationCount != SimpleHashtable.this.modificationCount) {
                throw new ConcurrentModificationException("Element structure was modified in the mean time.");
            }
        }
    }
}
