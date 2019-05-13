package hr.fer.zemris.java.gui.layouts.demo;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;

/**
 * Demo of a {@link CalcLayout}.
 */
public class DemoFrame1 extends JFrame {
    public DemoFrame1() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DemoFrame1().setVisible(true);
        });
    }

    private void initGUI() {
        JPanel p = new JPanel();
        this.add(p);
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 30));
        p.setLayout(new CalcLayout(3));
        p.add(l("tekst 1"), new RCPosition(1, 1));
        p.add(l("tekst 2"), new RCPosition(2, 3));
        p.add(l("tekst stvarno najdulji"), new RCPosition(2, 7));
        p.add(l("tekst kraći"), new RCPosition(4, 2));
        p.add(l("tekst srednji"), new RCPosition(4, 5));
        p.add(l("tekst"), new RCPosition(4, 7));
    }

    private JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }
}