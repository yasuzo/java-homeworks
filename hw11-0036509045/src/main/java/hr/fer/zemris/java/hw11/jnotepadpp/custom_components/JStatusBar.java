package hr.fer.zemris.java.hw11.jnotepadpp.custom_components;

import hr.fer.zemris.java.hw11.jnotepadpp.MyThreadPool;
import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

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
public class JStatusBar extends JPanel implements MultipleDocumentListener, CaretListener, ILocalizationListener {

    private JLabel lengthLabel;
    private JLabel cursorInfoLabel;
    private JLabel dateTimeLabel;
    private SingleDocumentModel currentDocument;

    private Status last;


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
        lengthLabel = new JLabel();
        cursorInfoLabel = new JLabel();
        setLabels();
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
        last = new Status();
        if (currentModel != null) {
            currentModel.getTextComponent().addCaretListener(this);
            last.documentLength = currentModel.getTextComponent().getText().length();
        }
        currentDocument = currentModel;
        setLabels();
    }

    @Override
    public void documentAdded(SingleDocumentModel model) {

    }

    @Override
    public void documentRemoved(SingleDocumentModel model) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {
        MyThreadPool.submit(() -> {
            LocalizationProvider lp = LocalizationProvider.getInstance();
            last = getStatus(e);
            SwingUtilities.invokeLater(() -> {
                setLabels();
            });
        });
    }

    /**
     * Returns status containing information about document length, and cursor position.
     *
     * @param e Caret event.
     * @return Status about document and caret.
     */
    private Status getStatus(CaretEvent e) {
        Status status = new Status();
        status.documentLength = currentDocument.getTextComponent().getText().length();
        int caretPosition = e.getDot();
        status.selected = Math.abs(caretPosition - e.getMark());
        char[] chars = currentDocument.getTextComponent().getText().toCharArray();
        status.cursorLine = 1;
        status.cursorColumn = 1;
        for (int i = 0; i < caretPosition; i++) {
            if (chars[i] == '\n') {
                status.cursorLine++;
                status.cursorColumn = 1;
            } else {
                status.cursorColumn++;
            }
        }
        return status;
    }

    @Override
    public void localizationChanged() {
        setLabels();
    }

    /**
     * Sets appropriate labels.
     */
    private void setLabels() {
        LocalizationProvider lp = LocalizationProvider.getInstance();
//        there is no status
        if(last == null) {
            lengthLabel.setText(lp.getString("statusLengthLabel_empty"));
            cursorInfoLabel.setText(lp.getString("statusCursorLabel_empty"));
            return;
        }
//        caret status update
        if (last.hasCaretStatus()) {
            cursorInfoLabel.setText(String.format(lp.getString("statusCursorLabel_format"), last.cursorLine, last.cursorColumn, last.selected));
        } else {
            cursorInfoLabel.setText(lp.getString("statusCursorLabel_empty"));
        }
//        document status update
        if (last.hasDocumentStatus()) {
            lengthLabel.setText(String.format(lp.getString("statusLengthLabel_format"), last.documentLength));
        } else {
            lengthLabel.setText(lp.getString("statusLengthLabel_empty"));
        }
    }

    /**
     * Status model. It holds document and caret status.
     */
    private static class Status {
        /**
         * Column position of the caret.
         */
        private int cursorColumn = -1;

        /**
         * Line position of the caret.
         */
        private int cursorLine = -1;

        /**
         * Number of characters selected.
         */
        private int selected = -1;

        /**
         * Current document length
         */
        private int documentLength = -1;

        /**
         * @return {@code true} if caret status is known, {@code false} otherwise.
         */
        private boolean hasCaretStatus() {
            return cursorColumn != -1;
        }

        /**
         * @return {@code true} if document status is known, {@code false} otherwise.
         */
        private boolean hasDocumentStatus() {
            return documentLength != -1;
        }
    }
}
