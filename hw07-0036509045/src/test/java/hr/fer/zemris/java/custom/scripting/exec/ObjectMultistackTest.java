package hr.fer.zemris.java.custom.scripting.exec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMultistackTest {

    private ObjectMultistack multistack;

    private void test(String stackKey, Object... objects) {
        for(Object o : objects) {
            assertEquals(o, multistack.pop(stackKey).getValue());
        }
        assertThrows(EmptyStackException.class, () -> multistack.pop(stackKey));
    }

    @BeforeEach
    void setUp() {
        multistack = new ObjectMultistack();
        multistack.push("stack1", new ValueWrapper(1));
        multistack.push("stack1", new ValueWrapper(2));


        multistack.push("stack2", new ValueWrapper(1));
    }

    @Test
    void pushNull() {
        assertThrows(NullPointerException.class, () -> multistack.push("stack1", null));
        assertThrows(NullPointerException.class, () -> multistack.push(null, new ValueWrapper(32)));
    }

    @Test
    void push() {
        multistack.push("stack1", new ValueWrapper("banana"));
        test("stack1", "banana", 2, 1);
    }

    @Test
    void popEmpty() {
        assertThrows(EmptyStackException.class, () -> multistack.pop("nonexistent"));
    }

    @Test
    void popNonempty() {
        assertEquals(1, multistack.pop("stack2").getValue());
        assertThrows(EmptyStackException.class, () -> multistack.pop("stack2"));
    }

    @Test
    void peekEmpty() {
        assertThrows(EmptyStackException.class, () -> multistack.peek("nonexistent"));
    }

    @Test
    void peekNonempty() {
        assertEquals(1, multistack.peek("stack2").getValue());
        assertEquals(1, multistack.peek("stack2").getValue());
    }

    @Test
    void isEmpty() {
        assertTrue(multistack.isEmpty(null));
        assertTrue(multistack.isEmpty("nonexistent"));
        assertFalse(multistack.isEmpty("stack1"));
    }
}