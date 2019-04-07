package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.util.Objects;

/**
 * Skip command for moving turtle without drawing on canvas.
 *
 * @author Jan Capek
 */
public class SkipCommand implements Command {

    private double step;

    /**
     * Constructs a new skip command that will move turtle forward without drawing.
     *
     * @param step Scalar that will scale turtle's move.
     */
    public SkipCommand(double step) {
        if (step < 0) {
            throw new IllegalArgumentException("Step cannot be less than 0.");
        }
        this.step = step;
    }

    /**
     * Moves a turtle forward without drawing.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     * @throws NullPointerException If {@code ctx} is {@code null}.
     * @throws CommandException     If an execution failed.
     */
    @Override
    public void execute(Context ctx, Painter p) {
        Objects.requireNonNull(ctx);
        try {
            ctx.getCurrentState().move(step);
        } catch (EmptyStackException e) {
            throw new CommandException("Current state is missing.");
        }
    }
}
