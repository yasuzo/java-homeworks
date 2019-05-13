package hr.fer.zemris.java.gui.layouts.demo;

import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;

public class DemoFrame2 extends JFrame {

    public DemoFrame2() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DemoFrame2().setVisible(true);
        });
    }

    private void initGUI() {
        JPanel p = new JPanel();
        this.add(p);
        p.setBorder(BorderFactory.createLineBorder(Color.RED, 30));
        p.setLayout(new CalcLayout(3));
        p.add(l("tekst 1"), "1,1");
        p.add(l("tekst 2"), "2,3");
        p.add(l("tekst stvarno najdulji"), "2,7");
        p.add(l("tekst kraći"), "4, 2");
        p.add(l("tekst srednji"), "4, 5");
        p.add(l("tekst"), " 4 , 7");
    }

    private JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }
}
