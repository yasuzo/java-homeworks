package hr.fer.zemris.java.gui.calc.action_listeners;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Listener which evaluates calculator's expression and sets its value to evaluated value.
 *
 * @author Jan Capek
 */
public class EvaluationActionListener implements ActionListener {

    private CalcModel calcModel;

    /**
     * Constructs a new evaluation listener.
     *
     * @param calcModel Calculator model whose expressions will be evaluated.
     * @throws NullPointerException If given model is {@code null}.
     */
    public EvaluationActionListener(CalcModel calcModel) {
        this.calcModel = Objects.requireNonNull(calcModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(calcModel.isActiveOperandSet() && calcModel.getPendingBinaryOperation() != null) {
            double result = calcModel.getPendingBinaryOperation()
                    .applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
            calcModel.clearAll();
            calcModel.setValue(result);
        }
    }
}
