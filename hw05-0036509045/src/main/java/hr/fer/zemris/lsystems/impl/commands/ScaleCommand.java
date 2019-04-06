package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Command for scaling turtle's unit distance by factor.
 *
 * @author Jan Capek
 */
public class ScaleCommand implements Command {

    private double factor;

    /**
     * Creates a new scale command that scales turtles moves by given factor.
     *
     * @param factor Factor that will scale turtles move.
     * @throws IllegalArgumentException If {@code factor} is less than 0.
     */
    public ScaleCommand(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Scale factor has to be greater than 0.");
        }
        this.factor = factor;
    }

    /**
     * Scales turtle's unit distance.
     *
     * @param ctx Context of the turtle.
     * @param p Painter used for drawing.
     * @throws CommandException If the execution failed.
     */
    @Override
    public void execute(Context ctx, Painter p) {
        try {
            ctx.getCurrentState().scaleTranslationDistance(factor);
        } catch (EmptyStackException e) {
            throw new CommandException("Current state missing.");
        }
    }
}
