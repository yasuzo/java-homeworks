package hr.fer.zemris.java.gui.calc.action_listeners;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Performs unary operation on current calculator value.
 *
 * @author Jan Capek
 */
public class UnaryOperationActionListener implements ActionListener {

    private CalcModel calcModel;
    private UnaryOperator<Double> unaryOperator;

    /**
     * Constructs a listener that performs given operation on current calculator input if there is any.
     *
     * @param calcModel Calculator model used.
     * @param unaryOperator Operation to be performed on {@link ActionListener#actionPerformed(ActionEvent)}.
     */
    public UnaryOperationActionListener(CalcModel calcModel, UnaryOperator<Double> unaryOperator) {
        this.calcModel = Objects.requireNonNull(calcModel);
        this.unaryOperator = Objects.requireNonNull(unaryOperator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        calcModel.setValue(unaryOperator.apply(calcModel.getValue()));
    }
}
