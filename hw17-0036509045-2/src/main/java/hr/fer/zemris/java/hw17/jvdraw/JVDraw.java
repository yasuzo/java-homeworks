package hr.fer.zemris.java.hw17.jvdraw;

import javax.swing.*;

/**
 * Starting point for JVDraw paint application.
 *
 * @author Jan Capek
 */
public class JVDraw {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new JVDrawFrame().setVisible(true);
        });
    }
}
