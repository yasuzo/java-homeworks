package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Default implementation of {@link DrawingModel}.
 *
 * @author Jan Capek
 */
public class DefaultDrawingModel implements DrawingModel, GeometricalObjectListener {

    Set<DrawingModelListener> listeners = new CopyOnWriteArraySet<>();
    List<GeometricalObject> objects = new ArrayList<>();
    private boolean isModified;

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public GeometricalObject getObject(int index) {
        return objects.get(index);
    }

    @Override
    public void add(GeometricalObject object) {
        Objects.requireNonNull(object);
        objects.add(object);
        object.addGeometricalObjectListener(this);
        isModified = true;
        listeners.forEach(l -> l.objectsAdded(this, getSize() - 1, getSize() - 1));
    }

    @Override
    public void remove(GeometricalObject object) {
        int objectIndex = objects.indexOf(object);
        if (objectIndex == -1) {
            return;
        }
        objects.remove(objectIndex).removeGeometricalObjectListener(this);
        isModified =true;
        listeners.forEach(l -> l.objectsRemoved(this, objectIndex, objectIndex));
    }

    @Override
    public void changeOrder(GeometricalObject object, int offset) {
        int currentIndex = objects.indexOf(object);
        if (currentIndex == -1) {
            return;
        }
        if (currentIndex + offset < 0 || currentIndex + offset >= objects.size()) {
            throw new IllegalArgumentException("Invalid offset!");
        }
        if (offset == 0) {
            return;
        }
        objects.remove(currentIndex);
        objects.add(currentIndex + offset, object);
        isModified = true;
        int index0 = Math.min(currentIndex + offset, currentIndex);
        int index1 = Math.max(currentIndex + offset, currentIndex);
        listeners.forEach(l -> {
            l.objectsChanged(this, index0, index1);
        });
    }

    @Override
    public int indexOf(GeometricalObject object) {
        return objects.indexOf(object);
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public void clearModifiedFlag() {
        isModified = false;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void addDrawingModelListener(DrawingModelListener l) {
        Objects.requireNonNull(l);
        listeners.add(l);
    }

    @Override
    public void removeDrawingModelListener(DrawingModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void geometricalObjectChanged(GeometricalObject o) {
        int index = indexOf(o);
        listeners.forEach(l -> l.objectsChanged(this, index, index));
    }

    @Override
    public Iterator<GeometricalObject> iterator() {
        return Collections.unmodifiableList(objects).iterator();
    }
}
