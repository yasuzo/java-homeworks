package hr.fer.zemris.java.hw17.jvdraw.geoobject.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Class used for editing of {@link hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject}.
 *
 * @author Jan Capek
 */
public abstract class GeometricalObjectEditor extends JPanel {

    /**
     * Checks if every edited setting is valid.
     *
     * @throws RuntimeException If any of the settings in invalid.
     */
    public abstract void checkEditing();

    /**
     * Accepts edited settings.
     *
     * @throws RuntimeException If {@link GeometricalObjectEditor#checkEditing()} failed or was never executed.
     */
    public abstract void acceptEditing();
}