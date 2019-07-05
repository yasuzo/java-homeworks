package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of tool supplier.
 *
 * @author Jan Capek
 */
public class ToolSupplier implements Supplier<Tool> {

    private Tool tool;

    @Override
    public Tool get() {
        return tool;
    }

    /**
     * Sets a new tool to supply.
     *
     * @param tool New tool.
     * @throws NullPointerException If given tool is {@code null}.
     */
    public void setTool(Tool tool) {
        this.tool = Objects.requireNonNull(tool);
    }
}
