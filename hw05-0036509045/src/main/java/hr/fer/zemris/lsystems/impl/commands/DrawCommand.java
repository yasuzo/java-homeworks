package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

import java.util.Objects;

/**
 * Command for drawing a line on the canvas.
 *
 * @author Jan Capek
 */
public class DrawCommand implements Command {

    /**
     * Scalar of turtle's step.
     */
    private double step;

    /**
     * Constructs a new draw command that will draw turtle's move which is scaled by given {@code step}.
     *
     * @param step Scalar that scales turtle's move.
     * @throws IllegalArgumentException If {@code step} is is less than 0.
     */
    public DrawCommand(double step) {
        if (step < 0) {
            throw new IllegalArgumentException("Step must be greater than 0.");
        }
        this.step = step;
    }

    /**
     * Draws turtles next move.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     * @throws NullPointerException If either of the arguments are {@code null}.
     * @throws CommandException     If an execution failed.
     */
    @Override
    public void execute(Context ctx, Painter p) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(p);

        TurtleState state;
        try {
            state = ctx.getCurrentState();
        } catch (EmptyStackException e) {
            throw new CommandException("Current state is missing.");
        }

        Vector2D startPosition = state.getPosition();
        Vector2D newPosition = state.move(step);

        p.drawLine(
                startPosition.getX(),
                startPosition.getY(),
                newPosition.getX(),
                newPosition.getY(),
                state.getDrawingColor(),
                1f
        );
    }
}
