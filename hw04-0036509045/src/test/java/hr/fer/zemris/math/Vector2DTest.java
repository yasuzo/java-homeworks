package hr.fer.zemris.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Vector2DTest {

    private Vector2D[] vectors;

    @BeforeEach
    void setUp() {
        Vector2D[] vectors = {
                new Vector2D(2, 2),
                new Vector2D(-2, 2),
                new Vector2D(0, 5),
                new Vector2D(0, -5),
                new Vector2D(-5, 0),
                new Vector2D(5, 0),
                new Vector2D(2, -2),
                new Vector2D(-2, -2)
        };
        this.vectors = vectors;
    }

    //    translate()
    @Test
    void translate() {
        Vector2D offset = new Vector2D(-3, 3);
        Vector2D[] expected = {
                new Vector2D(-1, 5),
                new Vector2D(-5, 5),
                new Vector2D(-3, 8),
                new Vector2D(-3, -2),
                new Vector2D(-8, 3),
                new Vector2D(2, 3),
                new Vector2D(-1, 1),
                new Vector2D(-5, 1)
        };
        for (int i = 0; i < vectors.length; i++) {
            vectors[i].translate(offset);
            assertEquals(expected[i], vectors[i]);
        }

    }

    //    translated()
    @Test
    void translated() {
        Vector2D offset = new Vector2D(-3, 3);
        Vector2D[] expected = {
                new Vector2D(-1, 5),
                new Vector2D(-5, 5),
                new Vector2D(-3, 8),
                new Vector2D(-3, -2),
                new Vector2D(-8, 3),
                new Vector2D(2, 3),
                new Vector2D(-1, 1),
                new Vector2D(-5, 1)
        };
        for (int i = 0; i < vectors.length; i++) {
            Vector2D old = vectors[i].copy();
            assertEquals(expected[i], vectors[i].translated(offset));
            assertEquals(old, vectors[i]);
        }
    }

    //    rotate
    @Test
    void rotate() {
        double angle = Math.PI / 4;
        Vector2D[] expected = {
                new Vector2D(0, Math.sqrt(8)),
                new Vector2D(-Math.sqrt(8), 0),
                new Vector2D(-Math.sqrt(12.5), Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(12.5), -Math.sqrt(12.5)),
                new Vector2D(-Math.sqrt(12.5), -Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(12.5), Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(8), 0),
                new Vector2D(0, -Math.sqrt(8))
        };
        for (int i = 0; i < vectors.length; i++) {
            vectors[i].rotate(angle);
            assertEquals(expected[i], vectors[i]);
        }
    }

    @Test
    void rotateNegative() {
        double angle = -7 * Math.PI / 4;
        Vector2D[] expected = {
                new Vector2D(0, Math.sqrt(8)),
                new Vector2D(-Math.sqrt(8), 0),
                new Vector2D(-Math.sqrt(12.5), Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(12.5), -Math.sqrt(12.5)),
                new Vector2D(-Math.sqrt(12.5), -Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(12.5), Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(8), 0),
                new Vector2D(0, -Math.sqrt(8))
        };
        for (int i = 0; i < vectors.length; i++) {
            vectors[i].rotate(angle);
            assertEquals(expected[i], vectors[i]);
        }
    }

    @Test
    void rotateExceeding2PI() {
        double angle = Math.PI / 2;
        Vector2D vec = new Vector2D(2, -2);
        vec.rotate(angle);

        Vector2D expected = new Vector2D(2, 2);
        assertEquals(expected, vec);
    }

    //    rotated()
    @Test
    void rotated() {
        double angle = Math.PI / 4;
        Vector2D[] expected = {
                new Vector2D(0, Math.sqrt(8)),
                new Vector2D(-Math.sqrt(8), 0),
                new Vector2D(-Math.sqrt(12.5), Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(12.5), -Math.sqrt(12.5)),
                new Vector2D(-Math.sqrt(12.5), -Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(12.5), Math.sqrt(12.5)),
                new Vector2D(Math.sqrt(8), 0),
                new Vector2D(0, -Math.sqrt(8))
        };
        for (int i = 0; i < vectors.length; i++) {
            Vector2D old = vectors[i].copy();
            assertEquals(expected[i], vectors[i].rotated(angle));
            assertEquals(old, vectors[i]);
        }
    }

    //    scale()
    @Test
    void scale() {
        double scalar = 0.5;
        Vector2D[] expected = {
                new Vector2D(1, 1),
                new Vector2D(-1, 1),
                new Vector2D(0, 2.5),
                new Vector2D(0, -2.5),
                new Vector2D(-2.5, 0),
                new Vector2D(2.5, 0),
                new Vector2D(1, -1),
                new Vector2D(-1, -1)
        };
        for (int i = 0; i < vectors.length; i++) {
            vectors[i].scale(scalar);
            assertEquals(expected[i], vectors[i]);
        }
    }

    @Test
    void scaleNegative() {
        double scalar = -0.5;
        Vector2D[] expected = {
                new Vector2D(-1, -1),
                new Vector2D(1, -1),
                new Vector2D(0, -2.5),
                new Vector2D(0, 2.5),
                new Vector2D(2.5, 0),
                new Vector2D(-2.5, 0),
                new Vector2D(-1, 1),
                new Vector2D(1, 1)
        };
        for (int i = 0; i < vectors.length; i++) {
            vectors[i].scale(scalar);
            assertEquals(expected[i], vectors[i]);
        }
    }

    //    scaled()
    @Test
    void scaled() {
        double scalar = 0.5;
        Vector2D[] expected = {
                new Vector2D(1, 1),
                new Vector2D(-1, 1),
                new Vector2D(0, 2.5),
                new Vector2D(0, -2.5),
                new Vector2D(-2.5, 0),
                new Vector2D(2.5, 0),
                new Vector2D(1, -1),
                new Vector2D(-1, -1)
        };
        for (int i = 0; i < vectors.length; i++) {
            Vector2D old = vectors[i].copy();
            assertEquals(expected[i], vectors[i].scaled(scalar));
            assertEquals(old, vectors[i]);
        }
    }

    //    copy()
    @Test
    void copy() {
        Vector2D v = new Vector2D(2, 2);
        Vector2D v2 = v.copy();
        assertEquals(v, v2);
        v2.scale(2);
        assertNotEquals(v, v2);
    }
}