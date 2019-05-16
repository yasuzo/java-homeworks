package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Model of a bar chart.
 *
 * @author Jan Capek
 */
public class BarChart  {

    private List<XYValue> xyValueList;
    private String xLabel;
    private String yLabel;
    private int minY;
    private int maxY;
    private int yDiff;

    /**
     * Constructs a new bar chart with given arguments.
     *
     * @param xyValueList List of values to plot; each {@code value.y} must be >= minY.
     * @param xLabel Label for x axis.
     * @param yLabel Label for y axis.
     * @param minY Minimal y; cannot be negative or >= maxY.
     * @param maxY Maximal y.
     * @param yDiff Difference between two ys; cannot be less than 1.
     * @throws NullPointerException If any of the given objects is {@code null}.
     * @throws IllegalArgumentException If given values are invalid.
     */
    public BarChart(List<XYValue> xyValueList, String xLabel, String yLabel, int minY, int maxY, int yDiff) {
        this.xLabel = Objects.requireNonNull(xLabel);
        this.yLabel = Objects.requireNonNull(yLabel);
        setYDiff(yDiff);
        setMinMaxY(minY, maxY);
        addAllPoints(xyValueList);
    }

    /**
     * Adds all points to the internal list.
     *
     * @param xyValueList List of points that need to be added.
     * @throws NullPointerException If given list is {@code null}.
     * @throws IllegalArgumentException If any of the values have {@code value.y < this.minY}.
     */
    private void addAllPoints(List<XYValue> xyValueList) {
        Objects.requireNonNull(xyValueList);
        if(this.xyValueList == null) {
            this.xyValueList = new ArrayList<>();
        }
        xyValueList.forEach(value -> {
            if(value.y < minY){
                throw new IllegalArgumentException("Some y values hava y < minY.");
            }
            this.xyValueList.add(value);
        });
        this.xyValueList = Collections.unmodifiableList(this.xyValueList);
    }

    /**
     * Sets y boundaries.
     *
     * @param minY Minimal y that needs to be plotted.
     * @param maxY Maximal y that needs to be plotted.
     * @throws IllegalArgumentException If {@code minY < 0 || minY >= maxY}.
     */
    private void setMinMaxY(int minY, int maxY) {
        if(minY < 0 || minY >= maxY) {
            throw new IllegalArgumentException("Invalid y boundaries.");
        }
        this.minY = minY;
        int remainder = (maxY - minY) % yDiff; // shows if with given yDiff maxY could be plotted
        this.maxY = remainder == 0 ? maxY : maxY + yDiff - remainder;
    }

    /**
     * Sets difference between two ys on the plot.
     *
     * @param yDiff Difference.
     * @throws IllegalArgumentException If given difference is less than 1.
     */
    private void setYDiff(int yDiff) {
        if(yDiff < 1) {
            throw new IllegalArgumentException("Difference between two Ys cannot be less than 1.");
        }
        this.yDiff = yDiff;
    }

    /**
     * @return List of values on the chart.
     */
    public List<XYValue> getXyValueList() {
        return xyValueList;
    }

    /**
     * @return Label for x axis.
     */
    public String getxLabel() {
        return xLabel;
    }

    /**
     * @return Label for y axis.
     */
    public String getyLabel() {
        return yLabel;
    }

    /**
     * @return Minimal y.
     */
    public int getMinY() {
        return minY;
    }

    /**
     * @return Maximal y.
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * @return Difference between two ys.
     */
    public int getyDiff() {
        return yDiff;
    }
}
