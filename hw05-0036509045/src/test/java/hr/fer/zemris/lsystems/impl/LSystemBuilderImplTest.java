package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LSystemBuilderImplTest {

    private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
        String[] data = new String[] {
                "origin 0.05 0.4",
                "angle 0",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0 / 3.0",
                "",
                "command F draw 1",
                "command + rotate 60",
                "command - rotate -60",
                "",
                "axiom F",
                "",
                "production F F+F--F+F"
        };
        return provider.createLSystemBuilder().configureFromText(data).build();
    }

    @Test
    void generateTest() {
        LSystem system = createKochCurve2(LSystemBuilderImpl::new);

        assertEquals("F", system.generate(0));
        assertEquals("F+F--F+F", system.generate(1));
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", system.generate(2));
    }

}