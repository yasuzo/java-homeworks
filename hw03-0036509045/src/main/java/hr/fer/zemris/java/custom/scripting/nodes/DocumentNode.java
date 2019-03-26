package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node representing an entire document.
 *
 * @author Jan Capek
 */
public class DocumentNode extends Node {

    @Override
    public String toString() {
        int children = numberOfChildren();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < children; i++){
            sb.append(getChild(i).toString());
        }
        return sb.toString();
    }
}
