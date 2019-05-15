package hr.fer.zemris.java.gui.calc.components;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Calculator button whose command can be inverted.
 *
 * @author Jan Capek
 */
public class InvertibleOpButton extends CalcButton {

    private String normalText;
    private String invertedText;

    private ActionListener normalAction;
    private ActionListener invertedAction;

    private boolean isInverted;

    /**
     * Constructs a new button whose behavior can be inverted.
     *
     * @param normalText     Normal button text.
     * @param invertedText   Inverted button text.
     * @param normalAction   Action performed in normal mode.
     * @param invertedAction Action performed in inverted mode.
     * @throws NullPointerException If {@code normalAction} or {@code invertedAction} is null.
     */
    public InvertibleOpButton(String normalText, String invertedText,
                              ActionListener normalAction, ActionListener invertedAction) {
        super(normalText);
        this.invertedText = invertedText;
        this.normalText = normalText;
        this.normalAction = Objects.requireNonNull(normalAction);
        this.invertedAction = Objects.requireNonNull(invertedAction);
        this.addActionListener(actionEvent -> {
            if (isInverted) {
                invertedAction.actionPerformed(actionEvent);
            } else {
                normalAction.actionPerformed(actionEvent);
            }
        });
    }

    /**
     * Inverts button behaviour.
     */
    public void invert() {
        isInverted = !isInverted;
        String text = isInverted ? invertedText : normalText;
        SwingUtilities.invokeLater(() -> this.setText(text));
    }
}
