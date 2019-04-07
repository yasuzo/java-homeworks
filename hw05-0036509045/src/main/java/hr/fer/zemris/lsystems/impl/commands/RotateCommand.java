package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.util.Objects;

/**
 * Rotation command. This will be used for rotation of the turtle
 * whose context will be given in {@link RotateCommand#execute(Context, Painter)}.
 *
 * @author Jan Capek
 */
public class RotateCommand implements Command {

    private double angle;

    /**
     * Constructs a new rotate command that will rotate a turtle for given angle.
     *
     * @param angle Angle for which a turtle will need to be rotated.
     */
    public RotateCommand(double angle) {
        this.angle = angle;
    }

    /**
     * Rotates a turtle by an angle given in constructor.
     *
     * @param ctx Context of the turtle.
     * @param p   Painter used for drawing.
     * @throws NullPointerException If given context is {@code null};
     * @throws CommandException     If the execution failed.
     */
    @Override
    public void execute(Context ctx, Painter p) {
        Objects.requireNonNull(ctx);
        try {
            ctx.getCurrentState().rotate(angle);
        } catch (EmptyStackException e) {
            throw new CommandException("Rotation isn't possible.");
        }
    }
}
