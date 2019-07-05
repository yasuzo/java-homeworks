package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.*;

/**
 * Interface that models a listener for color changes.
 *
 * @author Jan Capek
 */
public interface ColorChangeListener {

    /**
     * Method called every time color changes.
     *
     * @param source Source of the event.
     * @param oldColor Old color; can be {@code null}.
     * @param newColor New color; can be {@code null}.
     * @throws NullPointerException If the source is {@code null} and implementation does not support nulls.
     */
    void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}