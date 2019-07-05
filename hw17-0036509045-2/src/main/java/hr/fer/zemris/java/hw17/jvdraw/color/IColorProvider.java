package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.*;

/**
 * Color provider interface.
 *
 * @author Jan Capek
 */
public interface IColorProvider {

    /**
     * @return Current color.
     */
    Color getCurrentColor();

    /**
     * Registers a color change listener.
     *
     * @param l Listener that is to be registered.
     * @throws NullPointerException If given listener is {@code null}.
     */
    void addColorChangeListener(ColorChangeListener l);

    /**
     * Unregisters a listener.
     *
     * @param l Listener that is to be unregistered.
     */
    void removeColorChangeListener(ColorChangeListener l);
}