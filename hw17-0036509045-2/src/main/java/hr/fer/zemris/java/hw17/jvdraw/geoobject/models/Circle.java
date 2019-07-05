package hr.fer.zemris.java.hw17.jvdraw.geoobject.models;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.GeometricalObjectEditor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Circle without filling implementation of {@link GeometricalObject}.
 *
 * @author Jan Capek
 */
public class Circle extends GeometricalObject {

    private FilledCircle filledCircle;

    /**
     * Constructs a new filled circle object.
     *
     * @param center       Center of the circle.
     * @param radius       Circle radius; must be greater than 0.
     * @param outlineColor Circle's outline color.
     * @throws NullPointerException     If any of the parameters is {@code null}.
     * @throws IllegalArgumentException If given radius is less than 1.
     */
    public Circle(Point center, int radius, Color outlineColor) {
        filledCircle = new FilledCircle(center, radius, outlineColor, new Color(0, 0, 0, 1));
    }

    /**
     * @return Circle's center.
     */
    public Point getCenter() {
        return filledCircle.getCenter();
    }

    /**
     * Sets circle's center to the given point.
     *
     * @param center New center.
     * @throws NullPointerException If given point is {@code null}.
     */
    public void setCenter(Point center) {
        filledCircle.setCenter(center);
        notifyGeometricObjectListeners();
    }

    /**
     * @return Radius of the circle.
     */
    public int getRadius() {
        return filledCircle.getRadius();
    }

    /**
     * Sets radius of the circle.
     *
     * @param radius Circle's radius.
     * @throws IllegalArgumentException If given radius is less than 0.
     */
    public void setRadius(int radius) {
        filledCircle.setRadius(radius);
        notifyGeometricObjectListeners();
    }

    /**
     * @return Color of the circle's outline.
     */
    public Color getOutlineColor() {
        return filledCircle.getOutlineColor();
    }

    /**
     * Sets circle's outline color.
     *
     * @param outlineColor New outline color.
     * @throws NullPointerException If given color is {@code null}.
     */
    public void setOutlineColor(Color outlineColor) {
        filledCircle.setOutlineColor(outlineColor);
        notifyGeometricObjectListeners();
    }

    @Override
    public void accept(GeometricalObjectVisitor v) {
        v.visit(this);
    }

    @Override
    public GeometricalObjectEditor createGeometricalObjectEditor() {
        return new CircleEditor(this);
    }

    @Override
    public String toString() {
        Point center = getCenter();
        return String.format("Circle (%d, %d), %d", center.x, center.y, getRadius());
    }
}
