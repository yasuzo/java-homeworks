package hr.fer.zemris.java.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.UnaryOperator;

/**
 * Implementation of {@link CalcModel}.
 *
 * @author Jan Capek
 */
public class CalcModelImpl implements CalcModel {

    /**
     * Value listeners that will be notified on every change of value on display.
     */
    private List<CalcValueListener> valueListeners;

    /**
     * Stack on which values will be stored.
     */
    private Stack<Double> valueStack;

    /**
     * Flag that indicates if a new character can be inputted.
     */
    private boolean isEditable;

    /**
     * Current value on calculator's display.
     */
    private String input;

    /**
     * {@link CalcModelImpl#input} converted to double.
     */
    private double inputValue;

    /**
     * Operand waiting for operation to be executed on it.
     */
    private double activeOperand;

    /**
     * Flag that indicates if {@link CalcModelImpl#activeOperand} is set.
     */
    private boolean isActiveOperandSet;

    /**
     * Pending operation on {@link CalcModelImpl#activeOperand} and the {@link CalcModelImpl#inputValue}.
     */
    private DoubleBinaryOperator pendingOperation;

    /**
     * Constructs a new calculator model.
     */
    public CalcModelImpl() {
        valueListeners = new ArrayList<>();
        valueStack = new Stack<>();
        init();
    }

    /**
     * Initializes a calculator.
     */
    private void init() {
        valueStack.clear();
        isEditable = true;
        input = "";
        inputValue = 0;
        activeOperand = 0;
        isActiveOperandSet = false;
        pendingOperation = null;
        dispatchValueChange();
    }

    /**
     * Pushes current input on the stack.
     */
    public void push() {
        valueStack.push(inputValue);
    }

    /**
     * Pops top value from the stack and sets it to current input.
     *
     * @throws java.util.EmptyStackException If the stack is empty.
     */
    public void pop() {
        setValue(valueStack.pop());
    }

    /**
     * Notifies all CalcValueListeners of a value change.
     */
    private void dispatchValueChange() {
        for (CalcValueListener l : valueListeners) {
            l.valueChanged(this);
        }
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        valueListeners = new ArrayList<>(valueListeners);
        valueListeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        valueListeners = new ArrayList<>(valueListeners);
        valueListeners.remove(l);
    }

    @Override
    public String toString() {
        return input.matches("-?") ? input + "0" : input;
    }

    @Override
    public double getValue() {
        return inputValue;
    }

    @Override
    public void setValue(double value) {
        inputValue = value;
        input = Double.toString(value);
        isEditable = false;
        dispatchValueChange();
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void clear() {
        isEditable = true;
        alterInput(old -> "");
    }

    @Override
    public void clearAll() {
        clear();
        clearActiveOperand();
        pendingOperation = null;
    }

    /**
     * Edits current value.
     *
     * @param strategy Strategy which will return a new value that should be displayed.
     * @throws NullPointerException     If given strategy is {@code null}.
     * @throws CalculatorInputException If calculator is not editable or a new input is invalid.
     */
    private void alterInput(UnaryOperator<String> strategy) {
        Objects.requireNonNull(strategy);
        if (isEditable == false) {
            throw new CalculatorInputException("Calculator is not editable.");
        }

//        backup old data
        String oldInput = input;
        double oldInputValue = inputValue;

//        update input
        input = strategy.apply(input);
        try {
            inputValue = input.matches("-?") ? 0 : Double.parseDouble(input);
        } catch (NumberFormatException e) {
//            update failed -> roll back old data & throw
            input = oldInput;
            inputValue = oldInputValue;
            throw new CalculatorInputException("Input is not parsable to double.");
        }

//        overflow
        if (Double.isInfinite(inputValue) && Double.isInfinite(oldInputValue) == false) {
            input = oldInput;
            inputValue = oldInputValue;
            throw new CalculatorInputException("Number too large.");
        }
//        update succeeded -> dispatch change
        dispatchValueChange();
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        alterInput(old -> inputValue >= 0 ? "-" + old : old.substring(1));
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        alterInput(old -> {
            if(old.matches(".*\\d") == false) {
                throw new CalculatorInputException("Cannot add '.' to an empty input.");
            }
            return old + ".";
        });
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit has to be in range [1, 9].");
        }
        alterInput(old -> old.matches("-?0") ? old.replaceFirst("0", Integer.toString(digit)) : old + digit);
    }

    @Override
    public boolean isActiveOperandSet() {
        return isActiveOperandSet;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (isActiveOperandSet() == false) {
            throw new IllegalStateException("Active operand is not set.");
        }
        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        isActiveOperandSet = true;
    }

    @Override
    public void clearActiveOperand() {
        isActiveOperandSet = false;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingOperation = op;
    }
}
