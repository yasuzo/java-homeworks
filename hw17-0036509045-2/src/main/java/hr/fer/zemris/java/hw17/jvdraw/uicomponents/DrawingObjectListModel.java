package hr.fer.zemris.java.hw17.jvdraw.uicomponents;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;

import javax.swing.*;
import java.util.Objects;

/**
 * List model that models a list of geometrical objects.
 *
 * @author Jan Capek
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

    private DrawingModel drawingModel;

    /**
     * Constructs a new drawing list model.
     *
     * @param drawingModel Drawing model whose elements will be in the list.
     * @throws NullPointerException If given model is {@code null}.
     */
    public DrawingObjectListModel(DrawingModel drawingModel) {
        this.drawingModel = Objects.requireNonNull(drawingModel);
        drawingModel.addDrawingModelListener(this);
    }

    @Override
    public int getSize() {
        return drawingModel.getSize();
    }

    @Override
    public GeometricalObject getElementAt(int index) {
        return drawingModel.getObject(index);
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        fireIntervalAdded(this, index0, index1);
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        fireIntervalRemoved(this, index0, index1);
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        fireContentsChanged(this, index0, index1);
    }
}
