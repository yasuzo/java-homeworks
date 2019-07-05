package hr.fer.zemris.java.hw17.jvdraw.tools;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;
import hr.fer.zemris.java.hw17.jvdraw.geoobjectvisitors.GeometricalObjectPainter;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Tool for line drawing.
 *
 * @author Jan Capek
 */
public class LineTool extends GeoObjectDrawingTool {

    private Line currentDrawing;

    /**
     * Constructs a new tool for line drawing.
     *
     * @param outlineColorProvider Color provider for outline color.
     * @param fillingColorProvider Color provider for fill color.
     * @param drawingModel         Drawing model used for drawing.
     * @param canvas               Drawing canvas.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public LineTool(IColorProvider outlineColorProvider, IColorProvider fillingColorProvider, DrawingModel drawingModel, JDrawingCanvas canvas) {
        super(outlineColorProvider, fillingColorProvider, drawingModel, canvas);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        on right click remove current drawing
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (currentDrawing == null) {
                return;
            }
            currentDrawing = null;
            canvas.repaint();
        }

//        start drawing
        if (currentDrawing == null) {
            currentDrawing = new Line(e.getPoint(), e.getPoint(), outlineColorProvider.getCurrentColor());
            canvas.repaint();
            return;
        }

//        finish drawing
        currentDrawing.setEnd(e.getPoint());
        drawingModel.add(currentDrawing);
        currentDrawing = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (currentDrawing == null) {
            return;
        }
        currentDrawing.setEnd(e.getPoint());
        canvas.repaint();
    }

    @Override
    public void paint(Graphics2D g2d) {
        if (currentDrawing == null) {
            return;
        }
        currentDrawing.accept(new GeometricalObjectPainter(g2d));
    }
}
