package hr.fer.zemris.java.hw17.jvdraw.geoobjectvisitors;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;

import java.awt.*;

/**
 * Visitor used for calculating minimal bounding box containing all visited {@link hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject}.
 *
 * @author Jan Capek
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

    // Coordinates of the bounding box;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    /**
     * Flag that indicates if this visitor has already accepted one object.
     */
    private boolean firstObjectAccepted;

    @Override
    public void visit(Line line) {
        Point start = line.getStart();
        Point end = line.getEnd();
        int minX = Math.min(start.x, end.x);
        int maxX = Math.max(start.x, end.x);
        int minY = Math.min(start.y, end.y);
        int maxY = Math.max(start.y, end.y);

        this.maxX = Math.max(maxX, this.maxX);
        this.maxY = Math.max(maxY, this.maxY);
        if (firstObjectAccepted == false) {
            this.minX = minX;
            this.minY = minY;
            firstObjectAccepted = true;
        } else {
            this.minX = Math.min(minX, this.minX);
            this.minY = Math.min(minY, this.minY);
        }
    }

    @Override
    public void visit(Circle circle) {
        visitCircle(circle.getCenter(), circle.getRadius());
    }

    @Override
    public void visit(FilledCircle filledCircle) {
        visitCircle(filledCircle.getCenter(), filledCircle.getRadius());
    }


    /**
     * Updates bounding box considering circle with given center and radius.
     *
     * @param center Circle's center.
     * @param radius Circle's radius.
     * @throws NullPointerException If given center is {@code null}.
     */
    private void visitCircle(Point center, int radius) {
        maxX = Math.max(center.x + radius, maxX);
        maxY = Math.max(center.y + radius, maxY);
        if (firstObjectAccepted == false) {
            this.minX = center.x - radius;
            this.minY = center.y - radius;
            firstObjectAccepted = true;
        } else {
            minX = Math.min(center.x - radius, minX);
            minY = Math.min(center.y - radius, minY);
        }
    }

    /**
     * @return Bounding box containing all visited objects.
     */
    public Rectangle getBoundingBox() {
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}
