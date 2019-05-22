package hr.fer.zemris.java.hw11.jnotepadpp.custom_components;

import hr.fer.zemris.java.hw11.jnotepadpp.MyThreadPool;
import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Status bar used to display document length, cursor position and date-time.
 *
 * @author Jan Capek
 */
public class JStatusBar extends JPanel implements MultipleDocumentListener, CaretListener {

    private JLabel lengthLabel;
    private JLabel cursorInfoLabel;
    private JLabel dateTimeLabel;
    private SingleDocumentModel currentDocument;


    /**
     * Constructs a new status bar.
     */
    public JStatusBar() {
        setOpaque(true);
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridLayout(1, 3));
        setPreferredSize(new Dimension(getWidth(), 20));
        initGUI();
        startTimeCounterTask();
    }

    /**
     * Starts a task that will update date-time label every half second.
     */
    private void startTimeCounterTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
//                    nothing
                }
                SwingUtilities.invokeLater(() -> dateTimeLabel.setText(getCurrentDateTime()));
                MyThreadPool.submit(this);
            }
        };
        MyThreadPool.submit(task);
    }

    /**
     * GUI initialization.
     */
    private void initGUI() {
        lengthLabel = new JLabel("length: ");
        cursorInfoLabel = new JLabel("Ln:   Col:   Sel:   ");
        dateTimeLabel = new JLabel(getCurrentDateTime());
        dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        this.add(lengthLabel);
        this.add(cursorInfoLabel);
        this.add(dateTimeLabel);
    }

    /**
     * Constructs a string representation of current date.
     *
     * @return Current date.
     */
    private String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if (previousModel == null && currentModel == null) {
            throw new NullPointerException("Both args are null.");
        }
        if (previousModel != null) {
            previousModel.getTextComponent().removeCaretListener(this);
        }
        if (currentModel != null) {
            currentModel.getTextComponent().addCaretListener(this);
            lengthLabel.setText(String.format("length:%d", currentModel.getTextComponent().getText().length()));
        }
        currentDocument = currentModel;
    }

    @Override
    public void documentAdded(SingleDocumentModel model) {

    }

    @Override
    public void documentRemoved(SingleDocumentModel model) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int documentLength = currentDocument.getTextComponent().getText().length();
        lengthLabel.setText(String.format("length:%-4d", documentLength));

        MyThreadPool.submit(() -> {
            int caretPosition = e.getDot();
            int selected = Math.abs(caretPosition - e.getMark());
            char[] chars = currentDocument.getTextComponent().getText().toCharArray();
            int lineNumber = 1;
            int column = 1;
            for (int i = 0; i < caretPosition; i++) {
                if (chars[i] == '\n') {
                    lineNumber++;
                    column = 1;
                } else {
                    column++;
                }
            }
            Integer lNum = lineNumber;
            Integer cNum = column;
            SwingUtilities.invokeLater(() -> {
                cursorInfoLabel.setText(String.format("Ln:%-4d Col:%-4d Sel:%-4d", lNum, cNum, selected));
            });
        });
    }
}
