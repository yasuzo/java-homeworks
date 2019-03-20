package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Demo of {@code ObjectStack}.
 * This demo program accepts simple postfix expression as argument and calculates result of the expression.
 *
 * @author Jan Capek
 */
public class StackDemo {

    public static void main(String[] args) {

//        check if number of arguments is valid
        if (args.length != 1) {
            System.out.println("Invalid number of arguments! Exiting...");
            return;
        }

//        split expression by spaces
        String[] elements = args[0].split("\\s+");
        ObjectStack stack = new ObjectStack();

        for (String el : elements) {
            try {
//                try to parse integer
                Integer i = Integer.parseInt(el);
                stack.push(i);
            } catch (NumberFormatException e) {
                try {
//                    since element is not an integer, maybe it is an operation sign
                    int b = (Integer) stack.pop();
                    int a = (Integer) stack.pop();
//                    try to calculate
                    stack.push(calculate(a, b, el));
                } catch (EmptyStackException | IllegalArgumentException ex) {
//                    if stack is empty or some operation is not recognized, exit
                    System.out.println("Invalid expression!");
                    return;
                } catch (ArithmeticException ex) {
//                    division with 0 happened
                    System.out.println("Division with 0 is not permitted. Exiting...");
                    return;
                }
            }
        }

        Integer finalResult;
        try {
            finalResult = (Integer) stack.pop();
        } catch (EmptyStackException | ClassCastException ex) {
            System.out.println("Invalid expression!");
            return;
        }

        System.out.format("Result of the expression is %d.%n", finalResult);
    }

    /**
     * Calculates arithmetic operation on given operands.
     *
     * @param a         First operand
     * @param b         Second operand
     * @param operation Mathematical operation
     * @return Result of the operation on two operands
     * @throws ArithmeticException      If division with 0 happened
     * @throws IllegalArgumentException If operation is not recognized
     */
    private static int calculate(int a, int b, String operation) {
        switch (operation) {
            case "/":
                if (b == 0) {
                    throw new ArithmeticException("Division with zero!");
                }
                return a / b;
            case "*":
                return a * b;
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "%":
                if (b == 0) {
                    throw new ArithmeticException("Division with zero!");
                }
                return a % b;
            default:
//                string is not recognized as mathematical operation
                throw new IllegalArgumentException("Invalid operation!");
        }

    }
}
