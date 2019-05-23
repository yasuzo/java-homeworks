package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Action used to transform selected text.
 *
 * @author Jan Capek
 */
public abstract class TransformSelectionAction extends CurrentDocumentDependantAction implements CaretListener {

    /**
     * Constructs a new action dependant on text selection.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public TransformSelectionAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
        if (multipleDocumentModel.getCurrentDocument() != null) {
            multipleDocumentModel.getCurrentDocument().getTextComponent().addCaretListener(this);
        }
    }

    /**
     * Transforms text selection.
     *
     * @param transformer Function that will return transformed text.
     * @throws NullPointerException If given argument is {@code null}.
     */
    void transformSelected(UnaryOperator<String> transformer) {
        Objects.requireNonNull(transformer);
        if (isEnabled() == false) {
            return;
        }
        Caret caret = multipleDocumentModel.getCurrentDocument().getTextComponent().getCaret();

        int start = Math.min(caret.getDot(), caret.getMark());
        int length = Math.abs(caret.getDot() - caret.getMark());
        try {
            transform(transformer, start, length);
        } catch (IllegalArgumentException ex) {
            return;
        }
    }

    /**
     * Transforms text at at given position for given length.
     *
     * @param transformer Function which will return transformed text.
     * @param start       Start selection index.
     * @param length      Length of selection.
     * @throws NullPointerException     If given transformer is {@code null};
     * @throws IllegalArgumentException If given start index or length is invalid.
     */
    void transform(UnaryOperator<String> transformer, int start, int length) {
        Objects.requireNonNull(transformer);

        Document document = multipleDocumentModel.getCurrentDocument().getTextComponent().getDocument();
        try {
            String selection = document.getText(start, length);
            document.remove(start, length);
            document.insertString(start, transformer.apply(selection), null);
        } catch (BadLocationException ex) {
            throw new IllegalArgumentException("Illegal start and/or length.");
        }
    }

    /**
     * Determines which lines are selected and returns a result.
     *
     * @return Result object containing selected lines and other information.
     */
    LineResult getSelectedLines() {
        Document document = multipleDocumentModel.getCurrentDocument().getTextComponent().getDocument();
        Caret caret = multipleDocumentModel.getCurrentDocument().getTextComponent().getCaret();
        Element root = document.getDefaultRootElement();

        int selectionStart = Math.min(caret.getDot(), caret.getMark());
        int selectionEnd = Math.max(caret.getDot(), caret.getMark());

//        first line start and last line end
        int linesStart = root.getElement(root.getElementIndex(selectionStart)).getStartOffset();
        int linesEnd = Math.min(root.getElement(root.getElementIndex(selectionEnd)).getEndOffset(), document.getLength());
        int linesLength = linesEnd - linesStart;

//        get text
        String text;
        try {
            text = document.getText(linesStart, linesLength);
        } catch (BadLocationException ex) {
            throw new RuntimeException("Selection is invalid.");
        }
//        check if last line ends with '\n'
        boolean endsWithNewLine = false;
        if (text.endsWith("\n")) {
            endsWithNewLine = true;
            text = text.substring(0, Math.max(0, text.length() - 1));
        }
//        create lines and return result
        List<String> lines = Arrays.asList(text.split("\n"));
        return new LineResult(lines, linesStart, linesLength, endsWithNewLine);
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        super.currentDocumentChanged(previousModel, currentModel);
        if (previousModel != null) {
            previousModel.getTextComponent().removeCaretListener(this);
        }
        if (currentModel != null) {
            currentModel.getTextComponent().addCaretListener(this);
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (e.getMark() - e.getDot() == 0) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }

    /**
     * Class that models a result of {@link TransformSelectionAction#getSelectedLines()}.
     */
    static class LineResult {

        /**
         * List of lines.
         */
        private List<String> lines;

        /**
         * Offset of the start of the first line.
         */
        private int startIndex;

        /**
         * Length of all lines, last '\n' included.
         */
        private int length;

        /**
         * Flag that indicates if last line ends with '\n'
         */
        private boolean endsWithNewLine;

        /**
         * Constructs a new result object.
         *
         * @param lines           List of selected lines.
         * @param startIndex      Starting index.
         * @param length          Length of all selected lines.
         * @param endsWithNewLine Flag that indicates if last line ends with '\n'.
         * @throws NullPointerException If given list is {@code null}.
         */
        public LineResult(List<String> lines, int startIndex, int length, boolean endsWithNewLine) {
            this.lines = Objects.requireNonNull(lines);
            this.startIndex = startIndex;
            this.endsWithNewLine = endsWithNewLine;
            this.length = length;
        }

        /**
         * @return List with selected lines.
         */
        public List<String> getLines() {
            return lines;
        }

        /**
         * @return Starting offset of the first returned line.
         */
        public int getStartIndex() {
            return startIndex;
        }

        /**
         * @return Length of all selected lines.
         */
        public int getLength() {
            return length;
        }

        /**
         * @return {@code true} if last line ends with '\n', {@code false} otherwise.
         */
        public boolean endsWithNewLine() {
            return endsWithNewLine;
        }
    }
}
