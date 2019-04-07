package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.awt.*;
import java.util.EmptyStackException;
import java.util.Objects;

/**
 * Command for changing drawing color of the current state in the context.
 *
 * @author Jan Capek
 */
public class ColorCommand implements Command {

    private Color color;

    /**
     * Creates a new color command with given color.
     *
     * @param color New drawing color.
     * @throws NullPointerException If {@code color} is {@code null}.
     */
    public ColorCommand(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    /**
     * Changes drawing color of the current state in given context.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     * @throws NullPointerException If {@code ctx} is {@code null}.
     * @throws CommandException     If the execution failed.
     */
    @Override
    public void execute(Context ctx, Painter p) {
        Objects.requireNonNull(ctx);
        try {
            ctx.getCurrentState().setDrawingColor(color);
        } catch (EmptyStackException e) {
            throw new CommandException("Current state is missing.");
        }
    }
}
