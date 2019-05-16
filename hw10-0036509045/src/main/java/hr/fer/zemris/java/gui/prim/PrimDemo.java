package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * Demo program of a {@link PrimListModel}.
 */
public class PrimDemo extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
    }

    /**
     * Constructs a new demo window.
     */
    public PrimDemo() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        initUI();
    }

    /**
     * Initializes UI components.
     */
    private void initUI() {
        PrimListModel listModel = new PrimListModel();

        JList<Integer> list1 = new JList<>(listModel);
        JScrollPane scrollPane1 = new JScrollPane(list1);

        JList<Integer> list2 = new JList<>(listModel);
        JScrollPane scrollPane2 = new JScrollPane(list2);

        JPanel listPanel = new JPanel(new GridLayout(1, 2));
        listPanel.add(scrollPane1);
        listPanel.add(scrollPane2);

        this.add(listPanel, BorderLayout.CENTER);

        JButton next = new JButton("sljedeci");
        next.addActionListener(e -> listModel.next());
        this.add(next, BorderLayout.SOUTH);
    }
}
