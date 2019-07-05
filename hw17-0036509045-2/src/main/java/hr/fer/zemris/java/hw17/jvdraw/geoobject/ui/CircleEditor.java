package hr.fer.zemris.java.hw17.jvdraw.geoobject.ui;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Circle;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JColorArea;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Circle editor panel.
 *
 * @author Jan Capek
 */
public class CircleEditor extends GeometricalObjectEditor {

    private Circle circleToEdit;

    //    ui components
    private TextField centerX;
    private TextField centerY;
    private TextField radius;
    private JColorArea colorArea;

    //    new settings
    private Point newCenter;
    private int newRadius;
    private Color newColor;

    /**
     * Flag that indicates if settings check was successful.
     */
    private boolean checkSucceeded;

    /**
     * Constructs a new circle editor JPanel.
     *
     * @param circleToEdit Circle that needs to be edited.
     * @throws NullPointerException If given circle is {@code null}.
     */
    public CircleEditor(Circle circleToEdit) {
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
        colorArea = new JColorArea(circleToEdit.getOutlineColor());
        colorPanel.add(colorArea);

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
        newColor = Objects.requireNonNull(colorArea.getCurrentColor());
        checkSucceeded = true;
    }

    @Override
    public void acceptEditing() {
        if (checkSucceeded == false) {
            throw new RuntimeException("Check unsuccessful or not executed.");
        }
        circleToEdit.setCenter(newCenter);
        circleToEdit.setRadius(newRadius);
        circleToEdit.setOutlineColor(newColor);
        checkSucceeded = false;
    }
}