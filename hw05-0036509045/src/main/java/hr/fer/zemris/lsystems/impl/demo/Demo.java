package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

public class Demo {
    public static void main(String[] args) {
        LSystem system = new LSystemBuilderImpl()
                .registerCommand('F', "draw 1")
                .registerCommand('+', "rotate 60")
                .registerCommand('-', "rotate -60")
                .setOrigin(0.05, 0.4)
                .setAngle(0)
                .setUnitLength(0.9)
                .setUnitLengthDegreeScaler(1.0 / 3.0)
                .registerProduction('F', "F+F--F+F")
                .setAxiom("F")
                .build();

        LSystemViewer.showLSystem(system);
    }
}
