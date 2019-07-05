package hr.fer.zemris.java.hw17.jvdraw.uicomponents;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Color area button that triggers {@link JColorChooser} in order to choose color.
 *
 * @author Jan Capek
 */
public class JColorArea extends JComponent implements IColorProvider {

    private Set<ColorChangeListener> listeners = new CopyOnWriteArraySet<>();

    private Color selectedColor;

    /**
     * Mouse adapter.
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Color newColor = JColorChooser.showDialog(JColorArea.this, "Select Color", selectedColor, true);
            if (newColor == null) {
                return;
            }
            setColor(newColor);
        }
    };


    /**
     * Constructs a new color area.
     *
     * @param color Initial color.
     * @throws NullPointerException If given color is {@code null}.
     */
    public JColorArea(Color color) {
        addMouseListener(mouseAdapter);
        this.selectedColor = color;
    }

    /**
     * Sets a new color.
     *
     * @param c New color.
     * @throws NullPointerException If given color is {@code null}.
     */
    private void setColor(Color c) {
        Color oldColor = selectedColor;
        selectedColor = Objects.requireNonNull(c);
        notifyListeners(oldColor, c);
        repaint();
    }

    /**
     * Notifies listeners of color change.
     *
     * @param oldColor Old color.
     * @param newColor New color.
     */
    private void notifyListeners(Color oldColor, Color newColor) {
        listeners.forEach(l -> l.newColorSelected(this, oldColor, newColor));
    }

    @Override
    public Color getCurrentColor() {
        return selectedColor;
    }

    @Override
    public void addColorChangeListener(ColorChangeListener l) {
        listeners.add(Objects.requireNonNull(l));
    }

    @Override
    public void removeColorChangeListener(ColorChangeListener l) {
        listeners.remove(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(selectedColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(oldColor);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(15, 15);
    }
}
