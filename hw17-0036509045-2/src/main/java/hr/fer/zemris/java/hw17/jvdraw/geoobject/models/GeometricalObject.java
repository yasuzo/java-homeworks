package hr.fer.zemris.java.hw17.jvdraw.geoobject.models;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.GeometricalObjectEditor;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Class that models a geometric object.
 *
 * @author Jan Capek
 */
public abstract class GeometricalObject {

    private Set<GeometricalObjectListener> listeners;

    /**
     * Accepts a visitor and calls appropriate method for this implementation of {@link GeometricalObject}.
     *
     * @param v Visitor whose method will be called.
     * @throws NullPointerException If given visitor is {@code null}.
     */
    public abstract void accept(GeometricalObjectVisitor v);

    /**
     * Registers an geometric object listener.
     *
     * @param l Listener that is to be registered.
     * @throws NullPointerException If given listener is {@code null}.
     */
    public void addGeometricalObjectListener(GeometricalObjectListener l) {
        Objects.requireNonNull(l);
        if (listeners == null) {
            listeners = new CopyOnWriteArraySet<>();
        }
        listeners.add(l);
    }

    /**
     * Unregisters given geometric object listener.
     *
     * @param l Listener that is to be unregistered.
     */
    public void removeGeometricalObjectListener(GeometricalObjectListener l) {
        if (listeners == null) {
            return;
        }
        listeners.remove(l);
    }

    /**
     * Notifies all registered {@link GeometricalObjectListener}s of object change.
     */
    void notifyGeometricObjectListeners() {
        if (listeners == null) {
            return;
        }
        listeners.forEach(l -> l.geometricalObjectChanged(this));
    }

    /**
     * Creates an object editor JPanel for editing of this.
     *
     * @return Editor JPanel.
     */
    public abstract GeometricalObjectEditor createGeometricalObjectEditor();
}
