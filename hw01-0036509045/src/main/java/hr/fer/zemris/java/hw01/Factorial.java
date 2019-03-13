package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program for calculating factorials
 *
 * @author Jan Capek
 */
public class Factorial {

    /**
     * Calculates factorial from positive integer given as a parameter
     *
     * @throws IllegalArgumentException if given integer is out of allowed range
     * @param num Integer in range of [3, 20]
     * @return Factorial of a given integer
     */
    public static long calculateFactorial(int num) {
        if (num < 3 || num > 20) {
            throw new IllegalArgumentException("Number is out of range [3, 20]!");
        }

        long fact = 1;
        for (int i = num; i >= 2; i--) {
            fact *= i;
        }

        return fact;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Unesite broj > ");
        String input = sc.next();
        while (input.equals("kraj") == false) {
            try {
                int inputNumber = Integer.valueOf(input);
                System.out.format("%d! = %d%n", inputNumber, calculateFactorial(inputNumber));
            } catch (NumberFormatException e) {
                System.out.format("'%s' nije cijeli broj!%n", input);
            } catch (IllegalArgumentException e) {
                System.out.format("'%s' nije u dozvoljenom rasponu!%n", input);
            }

            System.out.print("Unesite broj > ");
            input = sc.next();
        }

        sc.close();

        System.out.println("Doviđenja!");
    }
}
