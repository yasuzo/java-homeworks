package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * Demo of bar char drawing.
 *
 * @author Jan Capek
 */
public class BarChartDemo extends JFrame {

    /**
     * Constructs a new demo window.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless() returns true.
     */
    public BarChartDemo() throws HeadlessException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("BarChartDemo");
        setSize(1000, 600);
        setLayout(new BorderLayout());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments!");
            return;
        }
        Path p = Path.of(args[0]);

        List<String> fileData;
        try {
            fileData = Files.readAllLines(p);
        } catch (IOException e) {
            System.out.println("Could not read a file.");
            return;
        }

        BarChart chart;
        try {
            chart = parseChart(fileData);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid data.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new BarChartDemo();
            frame.add(new BarChartComponent(chart), BorderLayout.CENTER);
            JLabel label = new JLabel(p.toString());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(label, BorderLayout.NORTH);
            frame.setVisible(true);
        });
    }

    /**
     * Parses chart data in string representation.
     *
     * @param chartData Chart data; each item represent one row.
     * @return Parsed chart.
     * @throws NullPointerException     If given list is {@code null}.
     * @throws IllegalArgumentException If given data is invalid.
     */
    private static BarChart parseChart(List<String> chartData) {
        if (chartData.size() < 6) {
            throw new IllegalArgumentException("Data is invalid.");
        }
        Iterator<String> it = chartData.iterator();
        String xLabel = it.next().trim();
        String yLabel = it.next().trim();
        List<XYValue> values = XYValue.parseAll(it.next().split("\\s+"));
        int minY = Integer.parseInt(it.next().trim());
        int maxY = Integer.parseInt(it.next().trim());
        int yDiff = Integer.parseInt(it.next().trim());
        return new BarChart(values, xLabel, yLabel, minY, maxY, yDiff);
    }
}
