package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * Node representing a single for-loop construct.
 *
 * @author Jan Capek
 */
public class ForLoopNode extends Node {

    private ElementVariable variable;
    private Element startExpression;
    private Element endExpression;
    private Element stepExpression;

    /**
     * Constructs a new {@code ForLoopNode} with given properties.
     *
     * @param variable        Variable used in a for-loop. Cannot be {@code null}.
     * @param startExpression Starting expression. Cannot be {@code null}.
     * @param endExpression   Ending expression. Cannot be {@code null}.
     * @param stepExpression  Step expression.
     * @throws NullPointerException If either {@code variable}, {@code startExpression} or {@code endExpression} are null.
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = Objects.requireNonNull(variable);
        this.startExpression = Objects.requireNonNull(startExpression);
        this.endExpression = Objects.requireNonNull(endExpression);
        this.stepExpression = stepExpression;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }

    /**
     * @return Variable used in the for-loop.
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * @return Start expression of the for-loop.
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * @return End expression of the for-loop.
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * @return Step expression of the for-loop.
     */
    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ForLoopNode that = (ForLoopNode) o;
        return Objects.equals(variable, that.variable) &&
                Objects.equals(startExpression, that.startExpression) &&
                Objects.equals(endExpression, that.endExpression) &&
                Objects.equals(stepExpression, that.stepExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), variable, startExpression, endExpression, stepExpression);
    }
}
