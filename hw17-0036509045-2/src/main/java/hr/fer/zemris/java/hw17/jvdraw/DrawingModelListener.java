package hr.fer.zemris.java.hw17.jvdraw;

/**
 * Listener interface used to listen for changes of {@link DrawingModel}.
 *
 * @author Jan Capek
 */
public interface DrawingModelListener {

    /**
     * Method fired when an object is added to the drawing.
     *
     * @param source Source of the event.
     * @param index0 Starting index of affected objects (inclusive).
     * @param index1 Ending index of affected objects (inclusive).
     */
    void objectsAdded(DrawingModel source, int index0, int index1);

    /**
     * Method fired when an object is removed from the drawing.
     *
     * @param source Source of the event.
     * @param index0 Starting index of affected objects (inclusive).
     * @param index1 Ending index of affected objects (inclusive).
     */
    void objectsRemoved(DrawingModel source, int index0, int index1);

    /**
     * Method fired when objects have changed.
     *
     * @param source Source of the event.
     * @param index0 Starting index of affected objects (inclusive).
     * @param index1 Ending index of affected objects (inclusive).
     */
    void objectsChanged(DrawingModel source, int index0, int index1);
}
