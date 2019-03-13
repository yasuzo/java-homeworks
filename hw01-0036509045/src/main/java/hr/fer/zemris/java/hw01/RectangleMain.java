package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program for "Rectangle" task
 *
 * @author Jan Capek
 */
public class RectangleMain {

    public static void main(String[] args) {
        if (args.length != 2 && args.length != 0) {
            System.out.println("Nedovoljan broj argumenata! Zavrsavam.");
            return;
        }

        if (args.length == 2) {
            System.out.println(createRectangleFromArguments(args));
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println(requestInputAndCreateRectangle(sc));
        sc.close();
    }

    /**
     * Creates a rectangle from program arguments.
     * If arguments are not valid, this will exit the program.
     *
     * @param args Program arguments
     * @return Rectangle
     */
    private static Rectangle createRectangleFromArguments(String[] args) {
        Rectangle r = null;

        try {
            double width, height;
            width = Double.parseDouble(args[0]);
            height = Double.parseDouble(args[1]);
            r = new Rectangle(width, height);
        } catch (NumberFormatException e) {
            System.out.println("Visina i sirina moraju biti brojevi! Zavrsavam.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Visina i sirina moraju biti pozitivni brojevi! Zavrsavam.");
            System.exit(1);
        }

        return r;
    }

    /**
     * Request input of width and height from user and instantiates a Rectangle from it
     *
     * @param sc Scanner to read from standard input
     * @return Rectangle
     */
    private static Rectangle requestInputAndCreateRectangle(Scanner sc) {
        if (sc == null) {
            throw new NullPointerException("Scanner cannot be null!");
        }

        double width, height;
        do {
            System.out.print("Unesite širinu > ");
            String input = sc.next();

            if(isPositiveNumber(input)){
                width = Double.parseDouble(input);
                break;
            }

        } while (true);

        do {
            System.out.print("Unesite visinu > ");
            String input = sc.next();

            if(isPositiveNumber(input)){
                height = Double.parseDouble(input);
                break;
            }

        } while (true);

        return new Rectangle(width, height);
    }

    /**
     * Checks if given String is representing a positive number
     *
     * @param possiblyNumber String that should be checked
     * @return {@code true} if the string is representing a positive number, {@code false} otherwise
     */
    private static boolean isPositiveNumber(String possiblyNumber) {
        double num;
        try {
            num = Double.parseDouble(possiblyNumber);
        } catch (NumberFormatException e) {
            System.out.format("'%s' se ne može protumačiti kao broj.%n", possiblyNumber);
            return false;
        }

        if (num < 0) {
            System.out.println("Unijeli ste negativnu vrijednost.");
            return false;
        }

        return true;
    }
}
