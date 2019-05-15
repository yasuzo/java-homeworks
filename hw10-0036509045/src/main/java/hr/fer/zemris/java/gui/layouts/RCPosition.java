package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a position in a table.
 *
 * @author Jan Capek
 */
public class RCPosition {

    /**
     * Row number.
     */
    public final int row;

    /**
     * Column number.
     */
    public final int column;

    /**
     * Constructs a new row-column position with given attributes.
     *
     * @param row    Row number.
     * @param column Column number.
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Parses an {@link RCPosition} from string.
     * String has to be in "row,column" format.
     *
     * @param s String representation of {@link RCPosition}.
     * @return Parsed position.
     * @throws NullPointerException     If given string is {@code null}.
     * @throws IllegalArgumentException If given string is invalid or row/column number is too high for an integer.
     */
    public static RCPosition fromString(String s) {
        Objects.requireNonNull(s);
        Pattern p = Pattern.compile("\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*");
        Matcher m = p.matcher(s);
        if (m.matches() == false) {
            throw new IllegalArgumentException("String is not valid.");
        }

        int row, column;
        try {
            row = Integer.parseInt(m.group(1));
            column = Integer.parseInt(m.group(2));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Numbers are too high.");
        }
        return new RCPosition(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RCPosition position = (RCPosition) o;
        return row == position.row &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
