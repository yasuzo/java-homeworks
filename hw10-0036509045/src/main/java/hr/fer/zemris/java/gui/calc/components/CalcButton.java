package hr.fer.zemris.java.gui.calc.components;

import javax.swing.*;
import java.awt.*;

/**
 * Button for a calculator.
 *
 * @author Jan Capek
 */
public class CalcButton extends JButton {

    /**
     * Constructs a new calculator button with given text.
     *
     * @param text Button text.
     */
    public CalcButton(String text) {
        super(text);
        this.setFont(this.getFont().deriveFont(20f));
        this.setBorder(BorderFactory.createLineBorder(new Color(70, 100, 100), 1));
        this.setBackground(new Color(100, 150, 170));
        this.setSize(100, 100);
    }
}
