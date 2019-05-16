package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * Component that draws a {@link BarChart} on its canvas.
 *
 * @author Jan Capek
 */
public class BarChartComponent extends JComponent {

    private BarChart chart;

    /**
     * Constructs a new component that draws given bar chart on its canvas.
     *
     * @param chart Chart that will be drawn.
     * @throws NullPointerException If given char is {@code null}.
     */
    public BarChartComponent(BarChart chart) {
        this.chart = Objects.requireNonNull(chart);
        setSize(new Dimension(500, 500));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point gridOrigin = drawGridWithLabels(g);
        drawBars(g, gridOrigin);
    }

    /**
     * Draws chart's bars.
     *
     * @param g Graphics object used for drawing.
     * @param origin Grid origin point.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    private void drawBars(Graphics g, Point origin) {
        Objects.requireNonNull(g);
        Objects.requireNonNull(origin);

        int minY = chart.getMinY();
        int horizontalLineNumber = (chart.getMaxY() - chart.getMinY()) / chart.getyDiff();
        int verticalLineNumber = getMaxX();
        int xDelta = Math.round((getWidth() - origin.x - 20) / verticalLineNumber);
        double yStepHeight = (origin.y - 20d) / horizontalLineNumber / chart.getyDiff();

        g.setColor(new Color(175, 75, 0));
        for (XYValue value : chart.getXyValueList()) {
            int height = (int)Math.round(yStepHeight * (value.y - minY) - 1);
            int x = origin.x + (value.x - 1) * xDelta + 1;
            int y = origin.y - height - 1;
            g.fillRect(x, y, xDelta - 1, height);
        }
    }

    /**
     * Calculates text box dimensions for given string.
     *
     * @param text Text whose dimensions have to be calculated.
     * @return Dimensions of given text.
     * @throws NullPointerException If given text is {@code null}.
     */
    private Dimension textBoxDimensions(String text) {
        Objects.requireNonNull(text);
        FontMetrics fontMetrics = getFontMetrics(getFont());
        Dimension dim = new Dimension();
        dim.height = fontMetrics.getHeight();
        dim.width = fontMetrics.stringWidth(text);
        return dim;
    }

    /**
     * Draws grid and returns origin of the grid.
     *
     * @param g    Graphics object used for drawing.
     * @param area Area on which drawing is allowed.
     * @return Origin of the grid.
     * @throws NullPointerException If any of the arguments is {@code null}.
     */
    private Point drawGrid(Graphics g, Rectangle area) {
        Objects.requireNonNull(g);
        Objects.requireNonNull(area);

        int axisNumberDistance = 10;
        Dimension maxNumDimension = textBoxDimensions(Integer.toString(chart.getMaxY())); // width is only relevant for y axis and height for x axis

//        plot's origin
        Point origin = new Point(
                area.x + maxNumDimension.width + axisNumberDistance,
                area.height - maxNumDimension.height - axisNumberDistance);

        int horizontalLineNumber = (chart.getMaxY() - chart.getMinY()) / chart.getyDiff();
        int verticalLineNumber = getMaxX();
        double xDelta = (getWidth() - origin.x - 20f) / verticalLineNumber;
        int yDelta = Math.round((origin.y - 20f) / horizontalLineNumber);
        int maxLineCoordinateY = origin.y - horizontalLineNumber * yDelta - 10;
        int maxLineCoordinateX = origin.x + (int)Math.round(verticalLineNumber * xDelta) + 10;

        int currentY = chart.getMinY();
        int yDiff = chart.getyDiff();

//        add horizontal lines and y labels
        for (int i = 0; i <= horizontalLineNumber; i++) {
            g.setColor(Color.ORANGE);
            g.drawLine(origin.x, origin.y - i * yDelta, maxLineCoordinateX, origin.y - i * yDelta);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(origin.x - 5, origin.y - i * yDelta, origin.x, origin.y - i * yDelta);
//            add label
            g.setColor(Color.BLACK);
            String s = Integer.toString(currentY);
            Dimension textDim = textBoxDimensions(s);
            g.drawString(s, origin.x - axisNumberDistance - textDim.width, origin.y - i * yDelta + Math.round(textDim.height / 4f));
            currentY += yDiff;
        }

//        add vertical lines and x labels
        for (int i = 1; i <= verticalLineNumber; i++) {
            int x2 = (int)Math.round(origin.x + i * xDelta);
            g.setColor(Color.ORANGE);
            g.drawLine(x2, origin.y + 5, x2, maxLineCoordinateY);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(x2, origin.y + 5, x2, origin.y);
//            add label
            g.setColor(Color.BLACK);
            String s = Integer.toString(i);
            Dimension textDim = textBoxDimensions(s);
            int stringX = (int) Math.round(origin.x + i * xDelta - xDelta / 2f - textDim.width / 2f);
            int stringY = origin.y + axisNumberDistance + 10;
            g.drawString(s, stringX, stringY);
        }

//        add two main lines and arrows
        g.setColor(Color.DARK_GRAY);
        g.drawLine(origin.x, origin.y + 5, origin.x, maxLineCoordinateY);
        g.drawLine(origin.x - 5, origin.y, maxLineCoordinateX, origin.y);
        g.fillPolygon(
                new int[]{origin.x, origin.x - 3, origin.x + 3},
                new int[]{maxLineCoordinateY, maxLineCoordinateY + 7, maxLineCoordinateY + 7},
                3);
        g.fillPolygon(
                new int[]{maxLineCoordinateX, maxLineCoordinateX - 7, maxLineCoordinateX - 7},
                new int[]{origin.y, origin.y + 3, origin.y - 3},
                3);
        g.setColor(Color.BLACK);

        return origin;
    }

    /**
     * Draws axis labels and grid on canvas.
     *
     * @param g Graphics object used for drawing.
     * @return Graph origin.
     * @throws NullPointerException If given argument is {@code null}.
     */
    private Point drawGridWithLabels(Graphics g) {
        Objects.requireNonNull(g);

        int height = getHeight();
        int width = getWidth();
        Dimension xLabelDimensions = textBoxDimensions(chart.getxLabel());
        Dimension yLabelDimensions = textBoxDimensions(chart.getyLabel());

        Rectangle freeArea = new Rectangle(
                5 + yLabelDimensions.height * 2 + 5,
                0,
                width - (5 + yLabelDimensions.height * 2 + 5),
                height - (5 + xLabelDimensions.height * 2 + 5));

        Point origin = drawGrid(g, freeArea);

//        ----------------- yLabel ------------------
        int xCoordLabelY = 5 + yLabelDimensions.height;
        int yCoordLabelY = Math.round(origin.y / 2f + yLabelDimensions.width / 2f);

//        rotate
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform defaultAf = g2d.getTransform();
        AffineTransform verticalAf = new AffineTransform();
        verticalAf.rotate(-Math.PI / 2);
        g2d.setTransform(verticalAf);

//        draw
        g.drawString(chart.getyLabel(), -yCoordLabelY, xCoordLabelY);
        g2d.setTransform(defaultAf);

//        ----------------- xLabel ------------------
        int xLabelWidth = xLabelDimensions.width;
        int xCoordLabelX = Math.round(origin.x + (width - origin.x) / 2f - xLabelWidth / 2f);
        int yCoordLabelX = height - 5 - xLabelDimensions.height;
        g.drawString(chart.getxLabel(), xCoordLabelX, yCoordLabelX);

        return origin;
    }

    /**
     * @return Maximal x in chart's dataset.
     */
    private int getMaxX() {
        int maxX = 1;
        for (XYValue val : chart.getXyValueList()) {
            maxX = Math.max(val.x, maxX);
        }
        return maxX;
    }
}
