package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.components.CalcFrame;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;

import javax.swing.*;

/**
 * Simple calculator program.
 *
 * @author Jan Capek
 */
public class Calculator {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalcFrame(new CalcModelImpl()).setVisible(true);
        });
    }
}
