package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Class for storing key, value pairs.
 * This class is an adapter of {@link ArrayIndexedCollection} class.
 *
 * @param <K> Type of keys.
 * @param <V> Type of values.
 *
 * @author Jan Capek
 */
public class Dictionary<K, V> {

    private ArrayIndexedCollection<Entry<K, V>> entries;

    /**
     * Constructs an empty dictionary.
     */
    public Dictionary() {
        entries = new ArrayIndexedCollection<>();
    }

    /**
     * Checks if the dictionary is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * @return Number of entries stored in the dictionary.
     */
    public int size() {
        return entries.size();
    }

    /**
     * Removes all entries from the dictionary.
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Adds an entry with given key and given value to the collection.
     * If an entry under given key already exists, old value will be replaced with new value.
     *
     * @param key Key under which the value should be stored.
     * @param value Value to store.
     * @throws NullPointerException If {@code key} is {@code null}.
     */
    public void put(K key, V value) {
        Entry<K, V> newEntry = new Entry<>(key, value); // this will throw an exception if key is null.

        int index = entries.indexOf(newEntry);
        if(index == -1) {
            entries.add(newEntry);
            return;
        }
        entries.get(index).setValue(value);
    }

    /**
     * Returns a value stored under given key.
     * If the key is not in the dictionary this will return {@code null}.
     *
     * @param key Key associated with wanted value.
     * @return Value stored under the given key, or {@code null} if the key is not in the dictionary.
     */
    public V get(Object key) {
        if(key == null) {
            return null;
        }

        ElementsGetter<Entry<K, V>> getter = entries.createElementsGetter();

        V value = null;
        while (getter.hasNextElement()){
            Entry<K, V> entry = getter.getNextElement();
            if(Objects.equals(entry.key, key)) {
                value = entry.value;
                break;
            }
        }
        return value;
    }

    /**
     * Class representing key-value pair for {@link Dictionary} entries.
     *
     * @param <K> Type of keys.
     * @param <V> Type of values.
     */
    public static class Entry<K, V> {
        private K key;
        private V value;

        /**
         * Constructs a new entry.
         *
         * @param key Key to store a value under.
         * @param value Value of the entry.
         * @throws NullPointerException If {@code key} is {@code null}.
         */
        private Entry(K key, V value) {
            this.key = Objects.requireNonNull(key);
            this.value = value;
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
         * @return Value of entry.
         */
        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}
