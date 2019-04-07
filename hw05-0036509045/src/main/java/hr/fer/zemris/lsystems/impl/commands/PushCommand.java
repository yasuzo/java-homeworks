package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

import java.util.Objects;

/**
 * Command for copying current state and pushing it to the context.
 *
 * @author Jan Capek
 */
public class PushCommand implements Command {

    /**
     * Copies current state from the context and pushes a copy on the context.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     * @throws CommandException     If an execution failed.
     * @throws NullPointerException If given context is {@code null};
     */
    @Override
    public void execute(Context ctx, Painter p) {
        Objects.requireNonNull(ctx);
        TurtleState state;
        try {
            state = ctx.getCurrentState();
        } catch (EmptyStackException e) {
            throw new CommandException("There is no current state.");
        }
        ctx.pushState(state.copy());
    }
}
