package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CalcLayoutTest {

    @Test
    void addLayoutComponentOnExistingButIllegal() {
        LayoutManager2 manager = new CalcLayout();
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "1,2"));
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "1,5"));
    }

    @Test
    void addLayoutComponentOnNonexistentPosition() {
        LayoutManager2 manager = new CalcLayout();
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "-1,2"));
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "0,5"));
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "2,-5"));
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "2,0"));
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "6,5"));
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "1,8"));
    }

    @Test
    void addLayoutComponentOnTakenPosition() {
        LayoutManager2 manager = new CalcLayout();
        manager.addLayoutComponent(new JLabel("lll"), "1,1");
        assertThrows(CalcLayoutException.class, () -> manager.addLayoutComponent(new JLabel("dafa"), "1,1"));
    }

    @Test
    void preferredLayoutSize1() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();
        assertEquals(new Dimension(152, 158), dim);
    }

    @Test
    void preferredLayoutSize2() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();
        assertEquals(new Dimension(152, 158), dim);
    }
}