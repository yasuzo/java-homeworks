package hr.fer.zemris.java.hw01;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static hr.fer.zemris.java.hw01.UniqueNumbers.*;
import static org.junit.jupiter.api.Assertions.*;

class UniqueNumbersTest {

    private TreeNode head;

    @BeforeEach
    private void setUp() {
        head = addNode(head, 76);
        head = addNode(head, 21);
        head = addNode(head, 42);
        head = addNode(head, 76);
        head = addNode(head, 35);
    }

    @AfterEach
    private void cleanUp() {
        head = null;
    }

    @Test
    public void normalOrderOfElements() {
        assertArrayEquals(new int[]{21, 35, 42, 76}, treeValues(head));
    }


    @Test
    public void containsTest() {
        assertTrue(containsValue(head, 76));
    }

    @Test
    public void doesNotContain() {
        assertFalse(containsValue(head, 100));
    }

    @Test
    public void treeSizeTest() {
        assertEquals(4, treeSize(head));
    }

    @Test
    public void addExistingValueTest() {
        addNode(head, 35);
        assertEquals(4, treeSize(head));
    }

    @Test
    public void addValueTest() {
        addNode(head, 37);
        assertArrayEquals(new int[]{21, 35, 37, 42, 76}, treeValues(head));
    }

}