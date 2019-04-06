package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.math.Vector2D;

import java.awt.*;
import java.util.Objects;

/**
 * This class models state of the turtle on screen.
 *
 * @author Jan Capek
 */
public class TurtleState {

    private Vector2D position;
    private Vector2D direction;
    private Color drawingColor;
    private double translationDistance;

    /**
     * Constructs a turtle state with starting parameters
     * such as position, direction, drawing color and translation distance.
     *
     * @param position            Position of a turtle.
     * @param initialRotation     Rotation of the turtle in radians (0 radians means right).
     * @param drawingColor        Drawing color of the turtle.
     * @param translationDistance Distance that the turtle will travel in one drawing unit.
     * @throws NullPointerException If either of the arguments are {@code null}.
     * @throws IllegalArgumentException If {@code translationDistance} is less than 0.
     */
    public TurtleState(Vector2D position, double initialRotation, Color drawingColor, double translationDistance) {
        if(translationDistance <= 0) {
            throw new IllegalArgumentException("Translation distance must be positive.");
        }
        this.translationDistance = translationDistance;
        this.position = Objects.requireNonNull(position).copy();
        this.direction = new Vector2D(1, 0);
        this.direction.rotate(initialRotation);
        this.drawingColor = Objects.requireNonNull(drawingColor);
    }

    /**
     * Returns current position of the turtle.
     * Modifications to the returning vector will not
     * modify position in the state state.
     *
     * @return Position of the turtle.
     */
    public Vector2D getPosition() {
        return position.copy();
    }

    /**
     * Scales translation distance by given factor.
     *
     * @param factor Scaling factor.
     * @throws IllegalArgumentException If {@code factor} is less than 0.
     */
    public void scaleTranslationDistance(double factor) {
        if(factor < 0) {
            throw new IllegalArgumentException("Scaling factor must be greater or equal to 0.");
        }
        translationDistance *= factor;
    }

    /**
     * @return Drawing color in this state.
     */
    public Color getDrawingColor() {
        return drawingColor;
    }

    /**
     * Sets drawing color to given color.
     *
     * @param drawingColor New drawing color.
     * @throws NullPointerException If new color is {@code null}.
     */
    public void setDrawingColor(Color drawingColor) {
        this.drawingColor = Objects.requireNonNull(drawingColor);
    }

    /**
     * Moves a turtle forward by {@code step * [translation distance]}.
     *
     * @param step Scalar that scales turtle's move.
     * @return New turtles position.
     * @throws IllegalArgumentException If {@code step} is less than 0.
     */
    public Vector2D move(double step) {
        if (step < 0) {
            throw new IllegalArgumentException("Step cannot be less than 0.");
        }
        position.translate(direction.scaled(step * translationDistance));
        return getPosition();
    }

    /**
     * Rotates a turtle by given angle.
     *
     * @param angle Angle to rotate for.
     */
    public void rotate(double angle) {
        direction.rotate(angle);
    }

    /**
     * Creates a copy of the state and guarantees that if the returned state changes, this will not.
     *
     * @return Copy of the state.
     */
    public TurtleState copy() {
        TurtleState copy =  new TurtleState(position.copy(), 0, drawingColor, translationDistance);
        copy.direction = direction.copy();
        return copy;
    }
}
