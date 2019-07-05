package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeometricalObjectVisitor;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;

import java.awt.*;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Savable implementation of {@link DrawingModel}.
 *
 * @author Jan Capek
 */
public class SavableDrawingModel extends DefaultDrawingModel {

    /**
     * Drawing model's save path.
     */
    private Path savePath;


    /**
     * @return Drawing model's save path.
     */
    public Path getSavePath() {
        return savePath;
    }

    /**
     * Sets a save path for the model.
     *
     * @param savePath Save path.
     * @throws NullPointerException If given path is {@code null}.
     */
    public void setSavePath(Path savePath) {
        this.savePath = Objects.requireNonNull(savePath);
    }

    /**
     * @return String representation of the drawing model.
     */
    @Override
    public String toString() {
        DrawingModelStringExporter exporter = new DrawingModelStringExporter();
        for (GeometricalObject object : this) {
            object.accept(exporter);
        }
        return exporter.toString();
    }

    /**
     * {@link GeometricalObjectVisitor} used for exporting drawing model to string.
     */
    private static class DrawingModelStringExporter implements GeometricalObjectVisitor {
        StringBuilder sb = new StringBuilder();

        @Override
        public void visit(Line line) {
            Point start = line.getStart();
            Point end = line.getEnd();
            Color color = line.getColor();
            sb.append(String.format("LINE %d %d %d %d %d %d %d", start.x, start.y, end.x, end.y,
                    color.getRed(), color.getGreen(), color.getBlue()));
            sb.append('\n');
        }

        @Override
        public void visit(Circle circle) {
            Point center = circle.getCenter();
            Color color = circle.getOutlineColor();
            sb.append(String.format("CIRCLE %d %d %d %d %d %d", center.x, center.y,
                    circle.getRadius(), color.getRed(), color.getGreen(), color.getBlue()));
            sb.append('\n');
        }

        @Override
        public void visit(FilledCircle filledCircle) {
            Point center = filledCircle.getCenter();
            Color outlineColor = filledCircle.getOutlineColor();
            Color fillColor = filledCircle.getFillColor();
            sb.append(
                    String.format("FCIRCLE %d %d %d %d %d %d %d %d %d", center.x, center.y,
                            filledCircle.getRadius(), outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(),
                            fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()
                    )
            );
            sb.append('\n');
        }

        /**
         * @return Final string representing a drawing model.
         */
        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
