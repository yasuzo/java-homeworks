package hr.fer.zemris.java.custom.scripting.exec.util;

/**
 * Class that contains methods for conversion of {@link Object} to {@link Integer} or {@link Double}.
 *
 * @author Jan Capek
 */
public class ObjectConverter {

    /**
     * Converts given object to {@link Integer} or {@link Double} if possible.
     * Rules are: <br>
     * <ol>
     * <li>If given object is a string, it will be parsed to integer or double if possible
     * otherwise a {@link RuntimeException} will be thrown.</li>
     * <li>If object is {@code null} it will be treated as {@code Integer.valueOf(0)}.</li>
     * <li>If object already is an Integer or Double it will simply be returned.</li>
     * <li>If given object is neither an Integer, Double or String a {@link RuntimeException} will be thrown.</li>
     * </ol>
     *
     * @param o Object that needs to be converted.
     * @return Object of type {@link Integer} or {@link Double} that is a result of conversion.
     * @throws RuntimeException If object is neither an Integer, Double or String or,
     *                          in case it is a String, it does not represent integer or double value.
     */
    public static Number convertToIntegerOrDouble(Object o) {
        o = o == null ? Integer.valueOf(0) : o;
//        exit early if o already is Double or Integer
        if (o instanceof Integer || o instanceof Double) {
            return (Number) o;
        }
//        if object is a string, try to parse it and return a result.
        if (o instanceof String) {
            return parseToIntegerOrDouble((String) o);
        }

//        given object isn't Integer, Double nor String -> throw
        throw new RuntimeException("Object is not Integer, Double or String.");
    }

    /**
     * Parses a string to {@link Integer} or {@link Double} if possible.
     *
     * @param s String that should be parsed.
     * @return Parsed Integer or Double.
     * @throws RuntimeException If given string does not represent an integer or double.
     */
    private static Number parseToIntegerOrDouble(String s) {
//        try to parse int from string, if it is possible that is the result
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
//            string does not represent an integer, maybe it is a double -> proceed
        }

//        try to parse double, if it is possible that is the final result
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
//            parsing double failed, clearly string does not represent a number
            throw new RuntimeException("String does not represent a number.");
        }
    }
}
