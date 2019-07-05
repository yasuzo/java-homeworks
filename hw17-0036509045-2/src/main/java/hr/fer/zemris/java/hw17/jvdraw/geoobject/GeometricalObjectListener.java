package hr.fer.zemris.java.hw17.jvdraw.geoobject;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;

/**
 * Listener interface for listening for change of {@link GeometricalObject}.
 *
 * @author Jan Capek
 */
public interface GeometricalObjectListener {

    /**
     * Method called for each change on the given object.
     *
     * @param o Geometrical object that is changed.
     * @throws NullPointerException If given object is {@code null} and implementation does not permit nulls.
     */
    void geometricalObjectChanged(GeometricalObject o);
}
