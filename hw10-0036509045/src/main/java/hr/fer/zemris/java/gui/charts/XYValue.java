package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model of a plot value.
 *
 * @author Jan Capek
 */
public class XYValue {

    /**
     * x coordinate.
     */
    public final int x;

    /**
     * y coordinate.
     */
    public final int y;

    /**
     * Constructs a new XYValue with given attributes.
     *
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Parses all values in array.
     *
     * @param values Values that need to be parsed.
     * @return List of parsed values.
     * @throws NullPointerException If given array is or any of its elements is {@code null}.
     * @throws IllegalArgumentException If any of the values could not be parsed.
     */
    public static List<XYValue> parseAll(String[] values) {
        Objects.requireNonNull(values);
        List<XYValue> result = new ArrayList<>(values.length);

        Pattern p = Pattern.compile("(\\d+),(\\d+)");
        for(String s : values) {
            Matcher m = p.matcher(s);
            if(m.matches() == false) {
                throw new IllegalArgumentException("Value could not be parsed.");
            }
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            result.add(new XYValue(x, y));
        }
        return result;
    }
}
