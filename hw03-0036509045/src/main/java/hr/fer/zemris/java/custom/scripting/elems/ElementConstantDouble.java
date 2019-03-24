package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents a double constant.
 *
 * @author Jan Capek
 */
public class ElementConstantDouble extends Element {

    private double value;

    public ElementConstantDouble(double value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return Double.toString(value);
    }

    /**
     * @return Constant's value.
     */
    public double getValue() {
        return value;
    }
}
