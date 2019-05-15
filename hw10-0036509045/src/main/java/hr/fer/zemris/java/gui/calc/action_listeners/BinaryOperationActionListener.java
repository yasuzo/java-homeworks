package hr.fer.zemris.java.gui.calc.action_listeners;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * Action listener that sets a pending operation of a calculator model on {@link ActionListener#actionPerformed(ActionEvent)}.
 *
 * @author Jan Capek.
 */
public class BinaryOperationActionListener implements ActionListener {

    private CalcModel calcModel;
    private DoubleBinaryOperator operator;

    /**
     * Constructs a listener that uses calculator model given as a parameter and sets calculator's operation to given operation.
     *
     * @param calcModel Calculator on which this listener will operate.
     * @param operator  Operator that will be sent to {@link CalcModel#setPendingBinaryOperation(DoubleBinaryOperator)}
     *                  on {@link ActionListener#actionPerformed(ActionEvent)}.
     * @throws NullPointerException If any of the arguments is {@code null}.
     */
    public BinaryOperationActionListener(CalcModel calcModel, DoubleBinaryOperator operator) {
        this.calcModel = Objects.requireNonNull(calcModel);
        this.operator = Objects.requireNonNull(operator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (calcModel.isActiveOperandSet() && calcModel.getPendingBinaryOperation() != null) {
            calcModel.setActiveOperand(
                    calcModel.getPendingBinaryOperation()
                            .applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue())
            );
        }
        if (calcModel.isActiveOperandSet() == false) {
            calcModel.setActiveOperand(calcModel.getValue());
        }
        calcModel.setPendingBinaryOperation(operator);
        calcModel.clear();
    }
}
