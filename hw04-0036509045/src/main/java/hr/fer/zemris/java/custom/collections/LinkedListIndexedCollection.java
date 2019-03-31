package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Linked list implementation of {@link Collection}.
 * This collection does not permit {@code null} values.
 *
 * @param <T> Type of objects stored in the collection.
 *
 * @author Jan Capek
 */
public class LinkedListIndexedCollection<T> implements List<T> {

    private int size;
    //    first element of the collection
    private ListNode<T> first;
    //    last element of the collection
    private ListNode<T> last;

    private long modificationCount;

    /**
     * Private static class for iterating over collection's elements.
     */
    private static class LinkedListElementsGetter<E> implements ElementsGetter<E> {
        private long startingModificationCount;
        private ListNode<E> currentNode;
        private LinkedListIndexedCollection c;

        private LinkedListElementsGetter(LinkedListIndexedCollection c) {
            Objects.requireNonNull(c);
            this.c = c;
            currentNode = c.first;
            startingModificationCount = c.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if(startingModificationCount != c.modificationCount) {
                throw new ConcurrentModificationException("Collection was modified in the mean time.");
            }
            return currentNode != null;
        }

        /**
         * {@inheritDoc}
         * @throws NoSuchElementException If all elements have already been returned by this ElementsGetter.
         */
        @Override
        public E getNextElement() {
            if(hasNextElement() == false) {
                throw new NoSuchElementException("All elements were already returned.");
            }
            E value = currentNode.value;
            currentNode = currentNode.next;
            return value;
        }
    }

    /**
     * Constructs linked list collection.
     */
    public LinkedListIndexedCollection() {
    }

    /**
     * Constructs linked list with elements from given collection in order they are returned by the collection.
     *
     * @param other Collection whose elements should be stored in a new collection
     * @throws NullPointerException If given collection is {@code null}
     */
    public LinkedListIndexedCollection(Collection<? extends T> other) {
        Objects.requireNonNull(other);

        addAll(other);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     * {@code null} as a value is not permitted.
     *
     * @param value Value to add to the collection
     * @throws NullPointerException If given value is {@code null}
     */
    @Override
    public void add(T value) {
        insert(value, size);
    }

    /**
     * {@inheritDoc}
     *
     * @param value    Value to be inserted. Must not be {@code null}.
     * @throws IndexOutOfBoundsException If illegal position is given (legal position is in interval of [0, size]).
     * @throws NullPointerException      If the value is null.
     */
    @Override
    public void insert(T value, int position) {
        Objects.requireNonNull(value);

        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Can't insert at index less than 0 or greater than size of the collection!");
        }

        if (position <= size / 2) {
            insertFromLeft(value, position);
        } else {
            insertFromRight(value, position);
        }

        size++;
        modificationCount++;
    }

    /**
     * Inserts value at given position. Position will be searched from the start of the linked list.
     *
     * @param value    Value to insert
     * @param position Position to insert at
     */
    private void insertFromLeft(T value, int position) {
        ListNode<T> temp = first;
        for (int i = 0; i < position; i++) {
            temp = temp.next;
        }

        insertBefore(temp, new ListNode<>(value));
    }

    /**
     * Inserts value at given position. Position will be searched from the end of the linked list.
     *
     * @param value    Value to insert
     * @param position Position where the value will be inserted
     */
    private void insertFromRight(T value, int position) {
        ListNode<T> temp = last;
        for (int i = size - 1; i > position - 1; i--) {
            temp = temp.previous;
        }

        insertAfter(temp, new ListNode<>(value));
    }

    /**
     * This method will insert new node in front of the node given as the first argument.
     * This will also update first and last node in the collection if needed.
     *
     * @param node         Node that should be before new node
     * @param nodeToInsert New node that should be inserted
     */
    private void insertAfter(ListNode<T> node, ListNode<T> nodeToInsert) {
        if (node == null) {
            first = nodeToInsert;
            last = first;
        } else {
            nodeToInsert.previous = node;
            nodeToInsert.next = node.next;
            node.next = nodeToInsert;

            if (nodeToInsert.next == null) {
                last = nodeToInsert;
            } else {
//                update next node to point to the new node
                nodeToInsert.next.previous = nodeToInsert;
            }
        }
    }

    /**
     * This method will insert new node before node given as the first argument.
     * This will also update first and last node in the collection if needed.
     *
     * @param node         Node that should be after new node
     * @param nodeToInsert New node that should be inserted
     */
    private void insertBefore(ListNode<T> node, ListNode<T> nodeToInsert) {
        if (node == null) {
            first = nodeToInsert;
            last = first;
        } else {
            nodeToInsert.next = node;
            nodeToInsert.previous = node.previous;
            node.previous = nodeToInsert;

            if (nodeToInsert.previous == null) {
                first = nodeToInsert;
            } else {
//                update previous node to point to the new node
                nodeToInsert.previous.next = nodeToInsert;
            }
        }
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    /**
     * {@inheritDoc}
     * Time complexity of this method is O(n).
     *
     * @return -1 if the value is not in the collection, index of the first occurrence otherwise.
     */
    @Override
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        ListNode<T> current = first;
        for (int i = 0; i < size; i++, current = current.next) {
            if (Objects.equals(current.value, value)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Removes given node from the linked list. This will also update the size of the collection.
     *
     * @param node Node to remove
     */
    private void removeNode(ListNode<T> node) {
        if (node == null) {
            return;
        }

        if (node == first) {
            first = node.next;
        }
        if (node == last) {
            last = node.previous;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        }
        if (node.previous != null) {
            node.previous.next = node.next;
        }

        size--;
        modificationCount++;
    }

    /**
     * {@inheritDoc}
     * Time complexity of this method is O(n).
     *
     * @param value Value to remove from the collection
     * @return {@code true} if element is in collection, {@code false} otherwise
     */
    @Override
    public boolean remove(Object value) {
        if (value == null) {
            return false;
        }

        for (ListNode<T> current = first; current != null; current = current.next) {
            if (Objects.equals(current.value, value)) {
                removeNode(current);
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * Index must not be less than 0 or greater than {@code size - 1}.
     * Time complexity of this is O(n)
     *
     * @throws IndexOutOfBoundsException If given index is less than 0 or greater or equal to the size of the collection
     */
    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Can't remove element at position that does not exist!");
        }

        ListNode<T> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        removeNode(current);
    }

    @Override
    public Object[] toArray() {
        Object[] content = new Object[size];

        ListNode<T> current = first;
        for (int i = 0; i < size; i++, current = current.next) {
            content[i] = current.value;
        }

        return content;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
        modificationCount++;
    }

    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new LinkedListElementsGetter<>(this);
    }

    /**
     * {@inheritDoc}
     * Valid indexes are 0 to size-1.
     * Time complexity of this is O(n).
     *
     * @throws IndexOutOfBoundsException If given index is greater or equal to the size of the collection or is negative.
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Can't access element that is out of bounds!");
        }

        if (index <= size / 2) {
            return getFromLeft(index);
        }

        return getFromRight(index);
    }

    /**
     * Runs from the start of the linked list to the given index and returns value stored at given index.
     *
     * @param index Index at which the value is to be retrieved
     * @return Value at given index
     */
    private T getFromLeft(int index) {
        ListNode<T> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.value;
    }

    /**
     * Runs from the end of the linked list to the given index and returns value stored at given index.
     *
     * @param index Index at which the value is to be retrieved
     * @return Value at given index
     */
    private T getFromRight(int index) {
        ListNode<T> current = last;
        for (int i = size - 1; i > index; i--) {
            current = current.previous;
        }

        return current.value;
    }

    /**
     * Instances of this class will represent nodes of doubly-linked list.
     */
    private static class ListNode<E> {
        private ListNode<E> previous;
        private ListNode<E> next;
        private E value;

        private ListNode(E value) {
            this.value = value;
        }
    }
}
