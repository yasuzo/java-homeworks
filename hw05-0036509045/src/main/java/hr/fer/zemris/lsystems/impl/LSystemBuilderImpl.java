package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;
import hr.fer.zemris.lsystems.impl.util.SettingsParser;
import hr.fer.zemris.math.Vector2D;

import java.awt.*;
import java.util.Objects;

/**
 * Instances of this class are meant to build LSystem from settings given to the instances.
 */
public class LSystemBuilderImpl implements LSystemBuilder {

    /**
     * Productions for characters in axiom.
     */
    private Dictionary<Character, String> productions;

    /**
     * Actions for characters in axiom.
     */
    private Dictionary<Character, Command> actions;

    /**
     * Turtle's unit length.
     */
    private double unitLength;

    /**
     * Turtle's origin point.
     */
    private Vector2D origin;
    /**
     * Turtle's starting angle.
     */
    private double angle;

    /**
     * Starting axiom.
     */
    private String axiom;

    /**
     * Degree scalar.
     * This will scale turtle's unit length for each generation.<br>
     * Formula: <br> {@code unitLength = unitLength * unitLengthScalar^generation}.
     */
    private double unitLengthDegreeScalar;

    public LSystemBuilderImpl() {
        productions = new Dictionary<>();
        actions = new Dictionary<>();
    }

    /**
     * Sets turtle's unit length.
     *
     * @param v Length
     * @return {@code this} with updated members.
     * @throws IllegalArgumentException If {@code v < 0 || v > 1}.
     */
    @Override
    public LSystemBuilder setUnitLength(double v) {
        if (unitLength < 0 || unitLength > 1) {
            throw new IllegalArgumentException("Unit length cannot be less than 0 or greater than 1.");
        }
        unitLength = v;
        return this;
    }

    /**
     * Sets turtle's origin.
     *
     * @param v  x coordinate.
     * @param v1 y coordinate.
     * @return {@code this} with updated members.
     */
    @Override
    public LSystemBuilder setOrigin(double v, double v1) {
        origin = new Vector2D(v, v1);
        return this;
    }

    /**
     * Sets turtle's starting angle. (0 means right)
     *
     * @param v Angle in degrees.
     * @return {@code this} with updated members.
     */
    @Override
    public LSystemBuilder setAngle(double v) {
        angle = v / 360 * 2 * Math.PI;
        return this;
    }

    /**
     * Sets an axiom.
     *
     * @param s Axiom.
     * @return {@code this} with updated members.
     * @throws NullPointerException If given axiom is {@code null}.
     */
    @Override
    public LSystemBuilder setAxiom(String s) {
        axiom = s;
        return this;
    }

    /**
     * Sets degree scalar which will scale unit length for each degree of the fractal.
     *
     * @param v Scalar.
     * @return {@code this} with updated members.
     * @throws IllegalArgumentException If given scalar is less or equal to 0.
     */
    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double v) {
        if (v <= 0) {
            throw new IllegalArgumentException("Scalar has to be greater than 0.");
        }
        unitLengthDegreeScalar = v;
        return this;
    }

    /**
     * Registers a production for a character.
     *
     * @param c Character.
     * @param s Production for character.
     * @return {@code this} with updated members.
     * @throws NullPointerException If given production is {@code null}.
     */
    @Override
    public LSystemBuilder registerProduction(char c, String s) {
        Objects.requireNonNull(s);
        productions.put(c, s);
        return this;
    }

    /**
     * Registers a command for a character.
     *
     * @param c Character.
     * @param s Turtle's command for given character.
     * @return {@code this} with updated members.
     * @throws NullPointerException If command is {@code null}.
     */
    @Override
    public LSystemBuilder registerCommand(char c, String s) {
        Objects.requireNonNull(s);
        String[] args = s.trim().split("\\s+");

        if (args.length > 2) {
            throw new IllegalArgumentException("Illegal command.");
        }

//        TODO: Break into a separate method and catch NumberFormatException.
        if (args.length == 1) {
            switch (args[0]) {
                case "push":
                    actions.put(c, new PushCommand());
                    break;
                case "pop":
                    actions.put(c, new PopCommand());
                    break;
                default:
                    throw new IllegalArgumentException("Illegal command.");
            }
            return this;
        }

        Command command;
        switch (args[0]) {
            case "color":
                command = new ColorCommand(new Color(Integer.parseInt(args[1], 16)));
                break;
            case "draw":
                command = new DrawCommand(Double.parseDouble(args[1]));
                break;
            case "skip":
                command = new SkipCommand(Double.parseDouble(args[1]));
                break;
            case "scale":
                command = new ScaleCommand(Double.parseDouble(args[1]));
                break;
            case "rotate":
                double angle = Double.parseDouble(args[1]) / 360 * 2 * Math.PI;
                command = new RotateCommand(angle);
                break;
            default:
                throw new IllegalArgumentException("Illegal command.");
        }
        actions.put(c, command);
        return this;
    }

    /**
     * Configures LSystemBuilder from text.
     *
     * @param strings Textual settings.
     * @return {@code this} with updated config.
     * @throws NullPointerException If settings are {@code null}.
     */
    @Override
    public LSystemBuilder configureFromText(String[] strings) {
        Objects.requireNonNull(strings);
        SettingsParser parser = new SettingsParser(this);
        parser.parse(strings);
        return this;
    }

    /**
     * Builds an {@link LSystem} from settings.
     *
     * @return New LSystem.
     */
    @Override
    public LSystem build() {
        return new LocalLSystem();
    }

    /**
     * Class representing LSystem. This enables generation and drawing of axioms.
     *
     * @author Jan Capek
     */
    public class LocalLSystem implements LSystem {

        /**
         * Generates a new axiom of {@code i}th generation.
         *
         * @param n Generation number.
         * @return New axiom.
         * @throws IllegalArgumentException If {@code n} is less than 0.
         */
        @Override
        public String generate(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Generation number cannot be less than 0.");
            }
            String currentAxiom = LSystemBuilderImpl.this.axiom;
            for (int i = 0; i < n; i++) {

                char[] seq = currentAxiom.toCharArray();
                StringBuilder sb = new StringBuilder();

                for (char c : seq) {
                    String temp = productions.get(c);
                    sb.append(temp == null ? c : temp);
                }

                currentAxiom = sb.toString();
            }

            return currentAxiom;
        }

        /**
         * Draws {@code i}th generation axiom with given painter.
         *
         * @param i       Generation number.
         * @param painter Painter to draw with.
         * @throws NullPointerException If {@code painter} is {@code null}.
         */
        @Override
        public void draw(int i, Painter painter) {
            Objects.requireNonNull(painter);

            Context context = new Context();
            TurtleState state =
                    new TurtleState(origin, angle, new Color(Integer.parseInt("000000", 16)), calculateUnitLength(i));
            context.pushState(state);

            String axiom = generate(i);
            char[] seq = axiom.toCharArray();
            for (char c : seq) {
                Command command = actions.get(c);
                if (command != null) {
                    command.execute(context, painter);
                }
            }

        }

        /**
         * Calculates appropriate unit length for axiom generation.
         *
         * @param i Generation number.
         * @return Calculated unit length.
         */
        private double calculateUnitLength(int i) {
            return LSystemBuilderImpl.this.unitLength * Math.pow(unitLengthDegreeScalar, i);
        }
    }
}
