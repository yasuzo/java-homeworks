package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents an integer constant.
 *
 * @author Jan Capek
 */
public class ElementConstantInteger extends Element {
    private int value;

    public ElementConstantInteger(int value) {
        this.value = value;
    }

    @Override
    public String asText() {
        return Integer.toString(value);
    }

    /**
     * @return Constant's value.
     */
    public int getValue() {
        return value;
    }
}
