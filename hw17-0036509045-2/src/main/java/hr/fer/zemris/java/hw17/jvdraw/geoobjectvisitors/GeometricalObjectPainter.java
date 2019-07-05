package hr.fer.zemris.java.hw17.jvdraw.geoobjectvisitors;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;

import java.awt.*;
import java.util.Objects;

/**
 * Implementation of {@link GeometricalObjectVisitor} used for painting {@link hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject}.
 *
 * @author Jan Capek
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

    private Graphics2D g2d;

    /**
     * Constructs a new painter that paints using given graphics object.
     *
     * @param g2d Graphics object used for painting.
     * @throws NullPointerException If given graphics object is {@code null}.
     */
    public GeometricalObjectPainter(Graphics2D g2d) {
        this.g2d = Objects.requireNonNull(g2d);
    }

    @Override
    public void visit(Line line) {
        Color previousColor = g2d.getColor();
        g2d.setColor(line.getColor());
        Point start = line.getStart();
        Point end = line.getEnd();
        g2d.drawLine(start.x, start.y, end.x, end.y);
        g2d.setColor(previousColor);
    }

    @Override
    public void visit(Circle circle) {
        Rectangle circleBoundingBox = getCircleBoundingRectangle(circle.getCenter(), circle.getRadius());
        Color previousColor = g2d.getColor();
        g2d.setColor(circle.getOutlineColor());
        g2d.drawOval(circleBoundingBox.x, circleBoundingBox.y, circleBoundingBox.width, circleBoundingBox.height);
        g2d.setColor(previousColor);
    }

    @Override
    public void visit(FilledCircle filledCircle) {
        Rectangle circleBoundingBox = getCircleBoundingRectangle(filledCircle.getCenter(), filledCircle.getRadius());
        Color previousColor = g2d.getColor();
        g2d.setColor(filledCircle.getFillColor());
        g2d.fillOval(circleBoundingBox.x, circleBoundingBox.y, circleBoundingBox.width, circleBoundingBox.height);
        g2d.setColor(filledCircle.getOutlineColor());
        g2d.drawOval(circleBoundingBox.x, circleBoundingBox.y, circleBoundingBox.width, circleBoundingBox.height);
        g2d.setColor(previousColor);
    }

    /**
     * Returns circle's bounding rectangle.
     *
     * @param center Center of the circle.
     * @param radius Radius of the circle.
     * @return Circle's bounding box.
     * @throws NullPointerException If given center is {@code null}.
     */
    private Rectangle getCircleBoundingRectangle(Point center, int radius) {
        return new Rectangle(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }
}
