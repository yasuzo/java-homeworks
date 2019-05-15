package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.action_listeners.BinaryOperationActionListener;
import hr.fer.zemris.java.gui.calc.action_listeners.EvaluationActionListener;
import hr.fer.zemris.java.gui.calc.action_listeners.UnaryOperationActionListener;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Calculator frame.
 *
 * @author Jan Capek
 */
public class CalcFrame extends JFrame {

    private List<InvertibleOpButton> invertableButtons = new ArrayList<>();
    private CalcModelImpl calcModel;

    /**
     * Constructs a new calculator window.
     *
     * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
     * @see JFrame#JFrame()
     */
    public CalcFrame(CalcModelImpl calcModel) throws HeadlessException {
        super("Calculator");
        Objects.requireNonNull(calcModel);
        this.calcModel = calcModel;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(500, 350);
        this.setSize(700, 400);
        this.setLayout(new CalcLayout(8));
        initGUI();
    }

    /**
     * Adds all needed components to the frame.
     */
    private void initGUI() {
//        create calculator's display
        CalcDisplay display = new CalcDisplay();
        calcModel.addCalcValueListener(display);
        this.add(display, "1,1");

//        add buttons
        addInvertibleButtons();
        addNonInvertibleButtons();
        addInputButtons();

//        add inverting checkbox
        JCheckBox invertCheck = new JCheckBox("Inv");
        invertCheck.addActionListener(event -> invertableButtons.forEach(InvertibleOpButton::invert));
        this.add(invertCheck, "5,7");

    }

    /**
     * Adds input buttons on to the frame.
     */
    private void addInputButtons() {
//        7
        CalcButton button = new CalcButton("7");
        button.addActionListener(event -> calcModel.insertDigit(7));
        this.add(button, "2,3");

//        8
        button = new CalcButton("8");
        button.addActionListener(event -> calcModel.insertDigit(8));
        this.add(button, "2,4");

//        9
        button = new CalcButton("9");
        button.addActionListener(event -> calcModel.insertDigit(9));
        this.add(button, "2,5");

//        4
        button = new CalcButton("4");
        button.addActionListener(event -> calcModel.insertDigit(4));
        this.add(button, "3,3");

//        5
        button = new CalcButton("5");
        button.addActionListener(event -> calcModel.insertDigit(5));
        this.add(button, "3,4");

//        6
        button = new CalcButton("6");
        button.addActionListener(event -> calcModel.insertDigit(6));
        this.add(button, "3,5");

//        1
        button = new CalcButton("1");
        button.addActionListener(event -> calcModel.insertDigit(1));
        this.add(button, "4,3");

//        2
        button = new CalcButton("2");
        button.addActionListener(event -> calcModel.insertDigit(2));
        this.add(button, "4,4");

//        3
        button = new CalcButton("3");
        button.addActionListener(event -> calcModel.insertDigit(3));
        this.add(button, "4,5");

//        0
        button = new CalcButton("0");
        button.addActionListener(event -> calcModel.insertDigit(0));
        this.add(button, "5,3");

//        +/-
        button = new CalcButton("+/-");
        button.addActionListener(event -> calcModel.swapSign());
        this.add(button, "5,4");

//        .
        button = new CalcButton(".");
        button.addActionListener(event -> calcModel.insertDecimalPoint());
        this.add(button, "5,5");
    }

    /**
     * Adds non invertible buttons to the frame.
     */
    private void addNonInvertibleButtons() {
//        1/x
        CalcButton button = new CalcButton("1/x");
        button.addActionListener(new UnaryOperationActionListener(calcModel, x -> 1 / x));
        this.add(button, "2,1");

//        /
        button = new CalcButton("/");
        button.addActionListener(new BinaryOperationActionListener(calcModel, (x, y) -> x / y));
        this.add(button, "2,6");

//        *
        button = new CalcButton("*");
        button.addActionListener(new BinaryOperationActionListener(calcModel, (x, y) -> x * y));
        this.add(button, "3,6");

//        -
        button = new CalcButton("-");
        button.addActionListener(new BinaryOperationActionListener(calcModel, (x, y) -> x - y));
        this.add(button, "4,6");

//        +
        button = new CalcButton("+");
        button.addActionListener(new BinaryOperationActionListener(calcModel, (x, y) -> x + y));
        this.add(button, "5,6");

//        =
        button = new CalcButton("=");
        button.addActionListener(new EvaluationActionListener(calcModel));
        this.add(button, "1,6");

//        clr
        button = new CalcButton("clr");
        button.addActionListener(event -> calcModel.clear());
        this.add(button, "1,7");

//        reset
        button = new CalcButton("reset");
        button.addActionListener(event -> calcModel.clearAll());
        this.add(button, "2,7");

//        push
        button = new CalcButton("push");
        button.addActionListener(event -> calcModel.push());
        this.add(button, "3,7");

//        pop
        button = new CalcButton("pop");
        button.addActionListener(event -> calcModel.pop());
        this.add(button, "4,7");
    }

    /**
     * Adds invertible buttons to frame.
     */
    private void addInvertibleButtons() {
        InvertibleOpButton button = new InvertibleOpButton("log", "10^x",
                new UnaryOperationActionListener(calcModel, Math::log10),
                new UnaryOperationActionListener(calcModel, x -> Math.pow(10, x)));
        invertableButtons.add(button);
        this.add(button, "3,1");

        button = new InvertibleOpButton("ln", "e^x",
                new UnaryOperationActionListener(calcModel, Math::log),
                new UnaryOperationActionListener(calcModel, Math::exp));
        invertableButtons.add(button);
        this.add(button, "4,1");

        button = new InvertibleOpButton("x^n", "x^(1/n)",
                new BinaryOperationActionListener(calcModel, Math::pow),
                new BinaryOperationActionListener(calcModel, (x, n) -> Math.pow(x, 1/n)));
        invertableButtons.add(button);
        this.add(button, "5,1");

        button = new InvertibleOpButton("sin", "arcsin",
                new UnaryOperationActionListener(calcModel, Math::sin),
                new UnaryOperationActionListener(calcModel, Math::asin));
        invertableButtons.add(button);
        this.add(button, "2,2");

        button = new InvertibleOpButton("cos", "arccos",
                new UnaryOperationActionListener(calcModel, Math::cos),
                new UnaryOperationActionListener(calcModel, Math::acos));
        invertableButtons.add(button);
        this.add(button, "3,2");

        button = new InvertibleOpButton("tan", "arctan",
                new UnaryOperationActionListener(calcModel, Math::tan),
                new UnaryOperationActionListener(calcModel, Math::atan));
        invertableButtons.add(button);
        this.add(button, "4,2");

        button = new InvertibleOpButton("ctg", "arcctg",
                new UnaryOperationActionListener(calcModel, x -> 1 / Math.tan(x)),
                new UnaryOperationActionListener(calcModel, x -> Math.atan(1 / x)));
        invertableButtons.add(button);
        this.add(button, "5,2");
    }


}
