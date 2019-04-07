package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.util.Objects;

/**
 * Pop command for removing current state from the context.
 *
 * @author Jan Capek
 */
public class PopCommand implements Command {

    /**
     * Removes current state from the context.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     * @throws NullPointerException If the context is null.
     * @throws CommandException     If there are no states in the context.
     */
    @Override
    public void execute(Context ctx, Painter p) {
        Objects.requireNonNull(ctx);
        try {
            ctx.popState();
        } catch (EmptyStackException e) {
            throw new CommandException("There are no states to pop.");
        }
    }
}
