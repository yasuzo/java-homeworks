package hr.fer.zemris.java.hw17.jvdraw.geoobject;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;

/**
 * Visitor interface used for visiting a {@link GeometricalObject}.
 *
 * @author Jan Capek
 */
public interface GeometricalObjectVisitor {

    /**
     * Visits a line.
     *
     * @param line Line that needs to be visited.
     * @throws NullPointerException If given line is {@code null} and implementation does not support nulls.
     */
    void visit(Line line);

    /**
     * Visits a circle.
     *
     * @param circle Circle that needs to be visited.
     * @throws NullPointerException If given circle is {@code null} and implementation does not support nulls.
     */
    void visit(Circle circle);

    /**
     * Visits a filled circle.
     *
     * @param filledCircle Filled circle that needs to be visited.
     * @throws NullPointerException If given filled circle is {@code null} and implementation does not support nulls.
     */
    void visit(FilledCircle filledCircle);
}