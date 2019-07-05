package hr.fer.zemris.java.hw17.jvdraw.geoobject.ui;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.Line;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JColorArea;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Line editor component.
 *
 * @author Jan Capek
 */
public class LineEditor extends GeometricalObjectEditor {

    private Line lineToEdit;

//    components
    private TextField startX;
    private TextField startY;
    private TextField endX;
    private TextField endY;
    private JColorArea colorArea;

//    new settings
    private Point newStart;
    private Point newEnd;
    private Color newColor;

    /**
     * Flag that indicates if check was successful.
     */
    private boolean checkSucceeded;

    /**
     * Constructs an editor for editing given line.
     *
     * @param lineToEdit Line that needs to be edited.
     * @throws NullPointerException If given line is {@code null}.
     */
    public LineEditor(Line lineToEdit) {
        this.lineToEdit = Objects.requireNonNull(lineToEdit);
        createGUI();
    }

    /**
     * Creates gui.
     */
    private void createGUI() {
        setLayout(new GridLayout(3, 1));

//        start panel
        JPanel startPanel = new JPanel();
        Point start = lineToEdit.getStart();
        startX = new TextField(Integer.toString(start.x), 5);
        startY = new TextField(Integer.toString(start.y), 5);
        startPanel.add(new JLabel("Start:"));
        startPanel.add(startX);
        startPanel.add(startY);

//        end panel
        JPanel endPanel = new JPanel();
        Point end = lineToEdit.getEnd();
        endX = new TextField(Integer.toString(end.x), 5);
        endY = new TextField(Integer.toString(end.y),5);
        endPanel.add(new JLabel("End:"));
        endPanel.add(endX);
        endPanel.add(endY);

//        color area
        JPanel colorPanel = new JPanel();
        colorPanel.add(new JLabel("Color:"));
        colorArea = new JColorArea(lineToEdit.getColor());
        colorPanel.add(colorArea);

//        add all to the panel
        add(startPanel);
        add(endPanel);
        add(colorPanel);
    }

    @Override
    public void checkEditing() {
        newStart = new Point(Integer.parseInt(startX.getText()), Integer.parseInt(startY.getText()));
        newEnd = new Point(Integer.parseInt(endX.getText()), Integer.parseInt(endY.getText()));
        newColor = Objects.requireNonNull(colorArea.getCurrentColor());
        checkSucceeded = true;
    }

    @Override
    public void acceptEditing() {
        if (checkSucceeded == false) {
            throw new RuntimeException("Check failed or was never executed.");
        }
        lineToEdit.setStart(newStart);
        lineToEdit.setEnd(newEnd);
        lineToEdit.setColor(newColor);
        checkSucceeded = false;
    }
}
