package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Class that represents an operator element.
 *
 * @author Jan Capek
 */
public class ElementOperator extends Element {
    private String symbol;

    /**
     * Constructs a new operator element.
     *
     * @param symbol String representation of an operator.
     * @throws NullPointerException If {@code symbol} is {@code null}.
     */
    public ElementOperator(String symbol) {
        this.symbol = Objects.requireNonNull(symbol);
    }

    @Override
    public String asText() {
        return symbol;
    }

    /**
     * @return Symbol of the operator.
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementOperator that = (ElementOperator) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
