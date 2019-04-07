package hr.fer.zemris.lsystems.impl;


import hr.fer.zemris.lsystems.Painter;

/**
 * Command interface for command execution.
 *
 * @author Jan Capek
 */
public interface Command {

    /**
     * Executes a command on the given context and/or painter.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     */
    void execute(Context ctx, Painter p);
}
