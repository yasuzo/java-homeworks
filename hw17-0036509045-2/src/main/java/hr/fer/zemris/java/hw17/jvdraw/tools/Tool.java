package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Drawing tool interface used for drawing.
 *
 * @author Jan Capek
 */
public interface Tool {

    /**
     * Method called on mouse pressed event.
     *
     * @param e Event that triggered the method.
     * @throws NullPointerException If given event is {@code null} and implementation does not permit nulls.
     */
    void mousePressed(MouseEvent e);

    /**
     * Method called on mouse released event.
     *
     * @param e Event that triggered the method.
     * @throws NullPointerException If given event is {@code null} and implementation does not permit nulls.
     */
    void mouseReleased(MouseEvent e);

    /**
     * Method called on mouse click event.
     *
     * @param e Event that triggered the method.
     * @throws NullPointerException If given event is {@code null} and implementation does not permit nulls.
     */
    void mouseClicked(MouseEvent e);

    /**
     * Method called on mouse move event.
     *
     * @param e Event that triggered the method.
     * @throws NullPointerException If given event is {@code null} and implementation does not permit nulls.
     */
    void mouseMoved(MouseEvent e);

    /**
     * Method called on mouse drag event.
     *
     * @param e Event that triggered the method.
     * @throws NullPointerException If given event is {@code null} and implementation does not permit nulls.
     */
    void mouseDragged(MouseEvent e);

    /**
     * Paints using given graphics object.
     *
     * @param g2d Graphics object used for drawing.
     * @throws NullPointerException If given graphics is {@code null} and implementation does not permit nulls.
     */
    void paint(Graphics2D g2d);
}
