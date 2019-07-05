package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class JVDraw {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new JVDrawFrame().setVisible(true);
        });
    }
}
