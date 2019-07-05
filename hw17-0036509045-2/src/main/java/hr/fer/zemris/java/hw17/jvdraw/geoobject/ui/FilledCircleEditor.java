package hr.fer.zemris.java.hw17.jvdraw.geoobject.ui;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JColorArea;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Editor for filled circle.
 *
 * @author Jan Capek
 */
public class FilledCircleEditor extends GeometricalObjectEditor {

    private FilledCircle circleToEdit;

    //    ui components
    private TextField centerX;
    private TextField centerY;
    private TextField radius;
    private JColorArea outlineColorArea;
    private JColorArea fillColorArea;


    //    new settings
    private Point newCenter;
    private int newRadius;
    private Color newOutlineColor;
    private Color newFillColor;

    /**
     * Flag that indicates if settings check was successful.
     */
    private boolean checkSucceeded;

    /**
     * Constructs a new  filled-circle editor JPanel.
     *
     * @param circleToEdit Circle that needs to be edited.
     * @throws NullPointerException If given circle is {@code null}.
     */
    public FilledCircleEditor(FilledCircle circleToEdit) {
        this.circleToEdit = Objects.requireNonNull(circleToEdit);
        initGUI();
    }

    /**
     * Initializes gui.
     */
    private void initGUI() {
        setLayout(new GridLayout(3, 1));

//        center panel
        JPanel centerPanel = new JPanel();
        Point center = circleToEdit.getCenter();
        centerX = new TextField(Integer.toString(center.x), 5);
        centerY = new TextField(Integer.toString(center.y), 5);
        centerPanel.add(new JLabel("Center:"));
        centerPanel.add(centerX);
        centerPanel.add(centerY);

//        radius panel
        JPanel radiusPanel = new JPanel();
        radius = new TextField(Integer.toString(circleToEdit.getRadius()), 5);
        radiusPanel.add(new JLabel("Radius:"));
        radiusPanel.add(radius);

//        color area
        JPanel colorPanel = new JPanel();
        colorPanel.add(new JLabel("Color:"));
        outlineColorArea = new JColorArea(circleToEdit.getOutlineColor());
        colorPanel.add(outlineColorArea);
        fillColorArea = new JColorArea(circleToEdit.getFillColor());
        colorPanel.add(fillColorArea);

//        add all to the panel
        add(centerPanel);
        add(radiusPanel);
        add(colorPanel);
    }

    @Override
    public void checkEditing() {
        newCenter = new Point(Integer.parseInt(centerX.getText()), Integer.parseInt(centerY.getText()));
        newRadius = Integer.parseInt(radius.getText());
        if (newRadius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative!");
        }
        newOutlineColor = Objects.requireNonNull(outlineColorArea.getCurrentColor());
        newFillColor = Objects.requireNonNull(fillColorArea.getCurrentColor());
        checkSucceeded = true;
    }

    @Override
    public void acceptEditing() {
        if (checkSucceeded == false) {
            throw new RuntimeException("Check unsuccessful or not executed.");
        }
        circleToEdit.setCenter(newCenter);
        circleToEdit.setRadius(newRadius);
        circleToEdit.setOutlineColor(newOutlineColor);
        circleToEdit.setFillColor(newFillColor);
        checkSucceeded = false;
    }
}
