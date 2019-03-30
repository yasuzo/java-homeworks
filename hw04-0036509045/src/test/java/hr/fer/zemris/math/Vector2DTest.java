package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    private Vector2D[] vectors;

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

    @Test
    void translate() {

    }

    @Test
    void translated() {
    }

    @Test
    void rotate() {
    }

    @Test
    void rotated() {
    }

    @Test
    void scale() {
    }

    @Test
    void scaled() {
    }

    @Test
    void copy() {
    }
}