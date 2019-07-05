package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;

/**
 * Class that models a drawing of {@link hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject}.
 *
 * @author Jan Capek
 */
public interface DrawingModel extends Iterable<GeometricalObject> {

    /**
     * @return Number of geometrical objects in the drawing.
     */
    int getSize();

    /**
     * Returns a geometric object at given index.
     *
     * @param index Index of the wanted object.
     * @return Object at given index.
     * @throws IndexOutOfBoundsException If there is no object at given index.
     */
    GeometricalObject getObject(int index);

    /**
     * Adds a geometric object to the drawing.
     *
     * @param object Object that is to be added.
     * @throws NullPointerException If given object is {@code null}.
     */
    void add(GeometricalObject object);

    /**
     * Removes given object from the drawing.
     *
     * @param object Object that needs to be removed.
     */
    void remove(GeometricalObject object);

    /**
     * Moves given object from current position to current position + offset.
     *
     * @throws IllegalArgumentException If {@code currentPosition + offset < 0 || currentPosition + offset >= numberOfObjects}.
     */
    void changeOrder(GeometricalObject object, int offset);

    /**
     * Returns an index of given object in the drawing.
     *
     * @param object Object whose index is desired.
     * @return Index of the object in the drawing or -1 if object is not in the drawing.
     */
    int indexOf(GeometricalObject object);

    /**
     * Removes all objects from the drawing.
     */
    void clear();

    /**
     * Clears modification status flag.
     */
    void clearModifiedFlag();

    /**
     * Returns drawings modification status.
     *
     * @return {@code true} if drawing was modified; {@code false} otherwise.
     */
    boolean isModified();

    /**
     * Registers a drawing listener.
     * @param l Listener that is to be registered.
     * @throws NullPointerException If given listener is {@code null}.
     */
    void addDrawingModelListener(DrawingModelListener l);

    /**
     * Unregisters given drawing listener.
     *
     * @param l Listener that is to be unregistered.
     */
    void removeDrawingModelListener(DrawingModelListener l);
}
