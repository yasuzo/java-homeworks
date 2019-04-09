package hr.fer.zemris.lsystems.impl.util;

import hr.fer.zemris.lsystems.LSystemBuilder;

import java.util.Objects;
import java.util.Scanner;

/**
 * This class will be used to parse commands and settings for {@link hr.fer.zemris.lsystems.LSystemBuilder}.
 *
 * @author Jan Capek
 */
public class SettingsParser {

    private LSystemBuilder builder;

    /**
     * Constructs a new SettingsParser for LSystemBuilder given as argument.
     *
     * @param builder LSystemBuilder whose settings should be parsed.
     * @throws NullPointerException If {@code builder} is {@code null}.
     */
    public SettingsParser(LSystemBuilder builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    /**
     * Parses settings and updates LSystemBuilder accordingly.
     *
     * @param settings Settings that should be parsed.
     * @throws NullPointerException If given array or any of the settings are {@code null}.
     * @throws ParserException      If any of the setting is invalid.
     */
    public void parse(String[] settings) {
        Objects.requireNonNull(settings);

        for (String setting : settings) {
            Objects.requireNonNull(setting);
            if (setting.isEmpty()) {
                continue;
            }

            try (Scanner sc = new Scanner(setting)) {
                parseSetting(sc);
            } catch (Exception e) {
                throw new ParserException("Invalid setting.");
            }
        }
    }

    /**
     * Parses a specific setting.
     *
     * @param sc Scanner to read a setting with.
     */
    private void parseSetting(Scanner sc) {
        Objects.requireNonNull(sc);

        String setting = sc.next();

        switch (setting) {
            case "origin":
                parseOrigin(sc);
                break;
            case "angle":
                builder.setAngle(parseLastDouble(sc));
                break;
            case "unitLength":
                builder.setUnitLength(parseLastDouble(sc));
                break;
            case "unitLengthDegreeScaler":
                parseUnitLengthDegreeScaler(sc);
                break;
            case "command":
                parseCommand(sc);
                break;
            case "axiom":
                parseAxiom(sc);
                break;
            case "production":
                parseProduction(sc);
                break;
            default:
                throw new ParserException("Unrecognized setting.");
        }
    }

    /**
     * Parses origin setting.
     *
     * @param sc Scanner for reading the setting.
     * @throws ParserException If a setting was invalid.
     */
    private void parseOrigin(Scanner sc) {
        double x, y;
        try {
            x = Double.parseDouble(sc.next());
            y = Double.parseDouble(sc.next());
        } catch (NumberFormatException e) {
            throw new ParserException("Invalid origin setting.");
        }
        if (sc.hasNext()) {
            throw new ParserException("Invalid origin setting.");
        }
        builder.setOrigin(x, y);
    }

    /**
     * Parses a number which has to be only/last argument.
     *
     * @param sc Scanner for reading the number.
     * @return Number.
     * @throws ParserException If the number isn't the last argument or the setting is invalid.
     */
    private double parseLastDouble(Scanner sc) {
        double x;
        try {
            x = Double.parseDouble(sc.next());
        } catch (NumberFormatException e) {
            throw new ParserException("Invalid setting.");
        }
        if (sc.hasNext()) {
            throw new ParserException("Invalid setting.");
        }
        return x;
    }

    /**
     * Parses unit length degree scaler.
     *
     * @param sc Scanner for reading the setting.
     * @throws ParserException If a setting is invalid.
     */
    private void parseUnitLengthDegreeScaler(Scanner sc) {
        String expression = sc.nextLine().replaceAll("\\s+", "");

        String[] args = expression.split("/");

        if (args.length == 1) {
            builder.setUnitLengthDegreeScaler(Double.parseDouble(args[0]));
        } else if (args.length == 2) {
            builder.setUnitLengthDegreeScaler(Double.parseDouble(args[0]) / Double.parseDouble(args[1]));
        } else {
            throw new ParserException("Invalid degree scalar setting.");
        }
    }

    /**
     * Parses a command setting.
     *
     * @param sc Scanner for reading the setting.
     * @throws ParserException If a setting is invalid.
     */
    private void parseCommand(Scanner sc) {
        char[] chars = sc.next().toCharArray();
        if (chars.length != 1) {
            throw new ParserException("Invalid command setting.");
        }
        char c = chars[0];
        String command = sc.nextLine().trim();
        builder.registerCommand(c, command);
    }

    /**
     * Parses an axiom.
     *
     * @param sc Scanner for reading the setting.
     * @throws ParserException If the setting is invalid.
     */
    private void parseAxiom(Scanner sc) {
        String axiom = sc.next();

        if (sc.hasNext()) {
            throw new ParserException("Invalid axiom setting.");
        }

        builder.setAxiom(axiom);
    }

    /**
     * Parses a production sequence.
     *
     * @param sc Scanner for reading the setting.
     * @throws ParserException If the setting is invalid.
     */
    private void parseProduction(Scanner sc) {
        char[] chars = sc.next().toCharArray();
        String production = sc.next();
        if (sc.hasNext()) {
            throw new ParserException("Invalid production setting.");
        }
        if (chars.length != 1) {
            throw new ParserException("Invalid production setting.");
        }
        char c = chars[0];
        builder.registerProduction(c, production);
    }
}
