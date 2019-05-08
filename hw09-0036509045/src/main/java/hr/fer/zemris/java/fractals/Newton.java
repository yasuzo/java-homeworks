package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Demo program that draws Newton's fractal.
 *
 * @author Jan Capek.
 */
public class Newton {
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
        System.out.print("Root 1> ");

        int i = 0;
        List<Complex> roots = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        for(String s = sc.nextLine().trim(); s.equals("done") == false || roots.size() < 2; s = sc.nextLine().trim()) {
            if(s.equals("done")) {
                System.out.println("At least two roots are needed. Please enter them.");
                continue;
            }
            try {
                roots.add(parseComplex(s));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid number syntax.");
                continue;
            }
            i++;
            System.out.format("Root %d>", i + 1);
        }

        System.out.println("Image of fractal will appear shortly. Thank you.");

        Complex[] polynomialRoots = new Complex[roots.size()];
        roots.toArray(polynomialRoots);
        FractalViewer.show(new NewtonFractalProducer(new ComplexRootedPolynomial(Complex.ONE, polynomialRoots)));
    }

    private static Complex parseComplex(String expression) {
        Objects.requireNonNull(expression);

//        get rid of all spaces
        expression = expression.replaceAll("\\s+", "");

//        check if the string is empty or contains sequence that is not valid but could be interpreted as such e.g. "+-"
        if (expression.contains("+-") || expression.isEmpty()) {
            throw new IllegalArgumentException("Expression is not valid!");
        }

//        split by '+'
        String[] substrings = expression.split("\\+");

        double real = 0, imaginary = 0;
        for (String substring : substrings) {
//            split before each '-'
            String[] bits = substring.split("(?=-)");
            for (String s : bits) {
                if (s.isEmpty()) {
                    continue;
                }

                try {
                    if (s.matches("-?i.*")) {
                        s = s.replace("i", "");

//                    check for edge cases where imaginary part is given as 'i' or '-i'
                        if (s.isEmpty() || s.equals("-")) {
                            s += "1";
                        }

                        imaginary += Double.parseDouble(s);
                        continue;
                    }
                    real += Double.parseDouble(s);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Expression is not valid!");
                }
            }
        }

        return new Complex(real, imaginary);
    }

}
