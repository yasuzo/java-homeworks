package hr.fer.zemris.java.hw17.jvdraw.uicomponents;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geoobjectvisitors.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Canvas that enables drawing on it.
 *
 * @author Jan Capek
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {

    private Supplier<Tool> toolSupplier;
    private DrawingModel drawingModel;

    /**
     * Mouse adapter for drawing canvas.
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            toolSupplier.get().mouseClicked(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            toolSupplier.get().mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            toolSupplier.get().mouseReleased(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            toolSupplier.get().mouseDragged(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            toolSupplier.get().mouseMoved(e);
        }
    };

    /**
     * Constructs a new drawing canvas.
     *
     * @param drawingModel Drawing model.
     * @param toolSupplier Supplier that will supply currently selected tool.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public JDrawingCanvas(DrawingModel drawingModel, Supplier<Tool> toolSupplier) {
        this.drawingModel = Objects.requireNonNull(drawingModel);
        this.toolSupplier = Objects.requireNonNull(toolSupplier);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        drawingModel.addDrawingModelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        GeometricalObjectPainter painter = new GeometricalObjectPainter(g2d);
        for (GeometricalObject o : drawingModel) {
            o.accept(painter);
        }

        toolSupplier.get().paint(g2d);
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        repaint();
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        repaint();
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        repaint();
    }
}
