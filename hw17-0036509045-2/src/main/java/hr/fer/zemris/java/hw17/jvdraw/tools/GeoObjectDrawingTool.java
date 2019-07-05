package hr.fer.zemris.java.hw17.jvdraw.tools;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;

import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Abstract implementation of {@link Tool}. Mouse event methods are implemented to do nothing.
 *
 * @author Jan Capek
 */
public abstract class GeoObjectDrawingTool implements Tool {

    protected IColorProvider outlineColorProvider;
    protected IColorProvider fillingColorProvider;
    protected DrawingModel drawingModel;
    protected JDrawingCanvas canvas;

    /**
     * Constructs a new tool.
     *
     * @param outlineColorProvider Color provider for outline color.
     * @param fillingColorProvider Color provider for fill color.
     * @param drawingModel         Drawing model used for drawing.
     * @param canvas               Drawing canvas.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public GeoObjectDrawingTool(IColorProvider outlineColorProvider, IColorProvider fillingColorProvider, DrawingModel drawingModel, JDrawingCanvas canvas) {
        this.outlineColorProvider = Objects.requireNonNull(outlineColorProvider);
        this.fillingColorProvider = Objects.requireNonNull(fillingColorProvider);
        this.drawingModel = Objects.requireNonNull(drawingModel);
        this.canvas = Objects.requireNonNull(canvas);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
