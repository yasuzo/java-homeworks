package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Linked list implementation of {@link Collection}.
 * This collection does not permit {@code null} values.
 *
 * @author Jan Capek
 */
public class LinkedListIndexedCollection implements Collection {

    private int size;
    //    first element of the collection
    private ListNode first;
    //    last element of the collection
    private ListNode last;

    private long modificationCount;

    /**
     * Private static class for iterating over collection's elements.
     */
    private static class LinkedListElementsGetter implements ElementsGetter {
        private long startingModificationCount;
        private ListNode currentNode;
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
        public Object getNextElement() {
            if(hasNextElement() == false) {
                throw new NoSuchElementException("All elements were already returned.");
            }
            Object value = currentNode.value;
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
    public LinkedListIndexedCollection(Collection other) {
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
    public void add(Object value) {
        insert(value, size);
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in linked-list. Elements starting from
     * this position are shifted one position. The legal positions are 0 to {@code size}.
     *
     * @param value    Value to be inserted. Must not be {@code null}
     * @param position Position where the value should be inserted
     * @throws IndexOutOfBoundsException If illegal position is given (legal position is in interval of [0, size])
     * @throws NullPointerException      If the value is null
     */
    public void insert(Object value, int position) {
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
    private void insertFromLeft(Object value, int position) {
        ListNode temp = first;
        for (int i = 0; i < position; i++) {
            temp = temp.next;
        }

        insertBefore(temp, new ListNode(value));
    }

    /**
     * Inserts value at given position. Position will be searched from the end of the linked list.
     *
     * @param value    Value to insert
     * @param position Position where the value will be inserted
     */
    private void insertFromRight(Object value, int position) {
        ListNode temp = last;
        for (int i = size - 1; i > position - 1; i--) {
            temp = temp.previous;
        }

        insertAfter(temp, new ListNode(value));
    }

    /**
     * This method will insert new node in front of the node given as the first argument.
     * This will also update first and last node in the collection if needed.
     *
     * @param node         Node that should be before new node
     * @param nodeToInsert New node that should be inserted
     */
    private void insertAfter(ListNode node, ListNode nodeToInsert) {
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
    private void insertBefore(ListNode node, ListNode nodeToInsert) {
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
     * Searches the collection and returns the index of the first occurrence of the given value or -1 if the value is
     * not found. Value can be null. Equality of the value is determined by the {@code Objects.equals(.)} method.
     * Time complexity of this method is O(n).
     *
     * @param value Value to search for
     * @return -1 if the value is not in the collection, index of the first occurrence otherwise
     */
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        ListNode current = first;
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
    private void removeNode(ListNode node) {
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

        for (ListNode current = first; current != null; current = current.next) {
            if (Objects.equals(current.value, value)) {
                removeNode(current);
                return true;
            }
        }

        return false;
    }

    /**
     * Removes an element stored at given index. Index must not be less than 0 or greater than {@code size - 1}.
     * Time complexity of this is O(n)
     *
     * @param index Index of the element that needs to be removed
     * @throws IndexOutOfBoundsException If given index is less than 0 or greater or equal to the size of the collection
     */
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Can't remove element at position that does not exist!");
        }

        ListNode current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        removeNode(current);
    }

    @Override
    public Object[] toArray() {
        Object[] content = new Object[size];

        ListNode current = first;
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
    public ElementsGetter createElementsGetter() {
        return new LinkedListElementsGetter(this);
    }

    /**
     * Returns the object at position {@code index} in linked list. Valid indexes are 0 to size-1.
     * Time complexity of this is O(n).
     *
     * @param index Index of element which is to be retrieved
     * @return Element at given index
     * @throws IndexOutOfBoundsException If given index is greater or equal to the size of the collection or is negative
     */
    public Object get(int index) {
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
    private Object getFromLeft(int index) {
        ListNode current = first;
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
    private Object getFromRight(int index) {
        ListNode current = last;
        for (int i = size - 1; i > index; i--) {
            current = current.previous;
        }

        return current.value;
    }

    /**
     * Instances of this class will represent nodes of doubly-linked list.
     */
    private static class ListNode {
        private ListNode previous;
        private ListNode next;
        private Object value;

        private ListNode(Object value) {
            this.value = value;
        }
    }
}
