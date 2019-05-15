package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

import javax.swing.*;
import java.awt.*;

/**
 * Display component used to display results on a calculator.
 *
 * @author Jan Capek
 */
public class CalcDisplay extends JLabel implements CalcValueListener {

    /**
     * Constructs a new calculator display component.
     */
    public CalcDisplay() {
        this.setHorizontalAlignment(RIGHT);
        this.setOpaque(true);
        this.setBackground(Color.YELLOW);
        this.setBorder(BorderFactory.createLineBorder(new Color(70, 100, 100), 1));
        this.setFont(this.getFont().deriveFont(30f));
        this.setSize(500, 100);
    }

    @Override
    public void valueChanged(CalcModel model) {
        SwingUtilities.invokeLater(() -> this.setText(model.toString()));
    }
}
