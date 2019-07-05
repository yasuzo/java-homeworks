package hr.fer.zemris.java.hw17.jvdraw.geoobject.models;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.LineEditor;

import java.awt.*;
import java.util.Objects;

/**
 * Line implementation of {@link GeometricalObject}.
 *
 * @author Jan Capek
 */
public class Line extends GeometricalObject {

    private Point start;
    private Point end;

    private Color color;

    /**
     * Constructs a new line.
     *
     * @param start Line's starting point.
     * @param end Line's ending point.
     * @param color Line's color.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public Line(Point start, Point end, Color color) {
        setStart(start);
        setEnd(end);
        setColor(color);
    }

    /**
     * @return Line's starting point.
     */
    public Point getStart() {
        return start.getLocation();
    }

    /**
     * Sets line's starting point.
     *
     * @param start New starting point.
     * @throws NullPointerException If given starting point is {@code null}.
     */
    public void setStart(Point start) {
        this.start = start.getLocation();
        notifyGeometricObjectListeners();
    }

    /**
     * @return Line's ending point.
     */
    public Point getEnd() {
        return end.getLocation();
    }

    /**
     * Sets line's ending point.
     *
     * @param end New ending point.
     * @throws NullPointerException If given point is {@code null}.
     */
    public void setEnd(Point end) {
        this.end = end.getLocation();
        notifyGeometricObjectListeners();
    }

    /**
     * @return Line's color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color of the line.
     *
     * @param color New line color.
     * @throws NullPointerException  If given color is {@code null}.
     */
    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
        notifyGeometricObjectListeners();
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new LineEditor(this);
    }

    @Override
    public String toString() {
        return String.format("Line (%d, %d)-(%d, %d)", start.x, start.y, end.x, end.y);
    }
}
