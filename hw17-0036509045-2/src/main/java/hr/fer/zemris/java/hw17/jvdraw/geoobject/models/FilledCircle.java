package hr.fer.zemris.java.hw17.jvdraw.geoobject.models;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.GeometricalObjectEditor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Filled circle implementation of {@link GeometricalObject}.
 *
 * @author Jan Capek
 */
public class FilledCircle extends GeometricalObject {

    private Point center;
    private int radius;

    private Color outlineColor;
    private Color fillColor;

    /**
     * Constructs a new filled circle object.
     *
     * @param center Center of the circle.
     * @param radius Circle radius; must be greater than 0.
     * @param outlineColor Circle's outline color.
     * @param fillColor Circle's fill color.
     * @throws NullPointerException If any of the parameters is {@code null}.
     * @throws IllegalArgumentException If given radius is less than 1.
     */
    public FilledCircle(Point center, int radius, Color outlineColor, Color fillColor) {
        setCenter(center);
        setRadius(radius);
        setOutlineColor(outlineColor);
        setFillColor(fillColor);
    }

    /**
     * @return Circle's center.
     */
    public Point getCenter() {
        return center.getLocation();
    }

    /**
     * Sets circle's center to the given point.
     *
     * @param center New center.
     * @throws NullPointerException If given point is {@code null}.
     */
    public void setCenter(Point center) {
        this.center = center.getLocation();
        notifyGeometricObjectListeners();
    }

    /**
     * @return Radius of the circle.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets radius of the circle.
     *
     * @param radius Circle's radius.
     * @throws IllegalArgumentException If given radius is less than 1.
     */
    public void setRadius(int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Circle's radius cannot be less than 1!");
        }
        this.radius = radius;
        notifyGeometricObjectListeners();
    }

    /**
     * @return Color of the circle's outline.
     */
    public Color getOutlineColor() {
        return outlineColor;
    }

    /**
     * Sets circle's outline color.
     *
     * @param outlineColor New outline color.
     * @throws NullPointerException If given color is {@code null}.
     */
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = Objects.requireNonNull(outlineColor);
        notifyGeometricObjectListeners();
    }

    /**
     * @return Circle's fill color.
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Sets circle's fill color.
     *
     * @param fillColor New fill color.
     * @throws NullPointerException If given color is {@code null}.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = Objects.requireNonNull(fillColor);
        notifyGeometricObjectListeners();
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new FilledCircleEditor(this);
    }

    @Override
    public String toString() {
        return String.format(
                "Filled circle (%d, %d), %d, #%02X%02X%02X",
                center.x,
                center.y,
                radius,
                fillColor.getRed(),
                fillColor.getGreen(),
                fillColor.getBlue()
        );
    }
}
