package hr.fer.zemris.java.hw17.jvdraw.geoobject;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for {@link GeometricalObject}.
 *
 * @author Jan Capek
 */
public class GeoObjectParser {

    /**
     * Parses a string representation of {@link GeometricalObject} and return parsed object.
     *
     * @param s String representation of {@link GeometricalObject}.
     * @return Parsed geometric object.
     * @throws IllegalArgumentException If object could not be parsed.
     * @throws NullPointerException     If given string is {@code null}.
     */
    public static GeometricalObject fromString(String s) {
        Matcher m = Pattern.compile("LINE (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)").matcher(s);
        if (m.matches()) {
            Point start = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            Point end = new Point(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
            Color color = new Color(Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)));
            return new Line(start, end, color);
        }

        m = Pattern.compile("CIRCLE (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)").matcher(s);
        if (m.matches()) {
            Point center = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            int radius = Integer.parseInt(m.group(3));
            Color color = new Color(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
            return new Circle(center, radius, color);
        }

        m = Pattern.compile("FCIRCLE (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)").matcher(s);
        if (m.matches()) {
            Point center = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            int radius = Integer.parseInt(m.group(3));
            Color outlineColor = new Color(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
            Color fillColor = new Color(Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8)), Integer.parseInt(m.group(9)));
            return new FilledCircle(center, radius, outlineColor, fillColor);
        }

        throw new IllegalArgumentException("Object could not be parsed.");
    }

    /**
     * Parses a list of string in which every string represents a {@link GeometricalObject}
     * and returns a list of parsed objects.
     *
     * @param iterable Iterable object containing string representations of {@link GeometricalObject}.
     * @throws NullPointerException     If given parameter or any of its elements is {@code null}.
     * @throws IllegalArgumentException If any of the objects could not be parsed.
     */
    public static List<GeometricalObject> fromString(Iterable<String> iterable) {
        List<GeometricalObject> result = new ArrayList<>();
        for (String s : iterable) {
            result.add(fromString(s));
        }
        return result;
    }
}
