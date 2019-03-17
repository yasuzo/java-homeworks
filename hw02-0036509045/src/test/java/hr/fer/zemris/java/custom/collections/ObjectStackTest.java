package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectStackTest {

    ObjectStack stack;

    @BeforeEach
    void setUp() {
        stack = new ObjectStack();
        stack.push(0);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
    }

    @Test
    void isEmptyOnNotEmpty() {
        assertFalse(stack.isEmpty());
    }

    @Test
    void isEmptyOnEmpty() {
        assertTrue(new ObjectStack().isEmpty());
    }

    @Test
    void size() {
        assertEquals(5, stack.size());
    }

    @Test
    void pushNull() {
        assertThrows(NullPointerException.class, () -> stack.push(null));
    }

    @Test
    void pushNormal() {
        stack.push("kokica");
        assertEquals("kokica", stack.peek());
        assertEquals(6, stack.size());
    }

    @Test
    void popEmpty() {
        assertThrows(EmptyStackException.class, () -> new ObjectStack().pop());
    }

    @Test
    void popNotEmpty() {
        assertEquals(4, stack.pop());
        assertEquals(3, stack.pop());
    }

    @Test
    void peekEmpty() {
        assertThrows(EmptyStackException.class, () -> new ObjectStack().peek());
    }

    @Test
    void peekNotEmpty() {
        assertEquals(4, stack.peek());
        assertEquals(4, stack.peek());
    }

    @Test
    void clear() {
        stack.clear();

        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
    }
}