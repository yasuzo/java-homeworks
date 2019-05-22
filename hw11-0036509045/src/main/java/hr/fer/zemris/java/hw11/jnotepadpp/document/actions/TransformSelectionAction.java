package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import com.sun.jdi.ObjectReference;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
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
        if(multipleDocumentModel.getCurrentDocument() != null) {
            multipleDocumentModel.getCurrentDocument().getTextComponent().addCaretListener(this);
        }
    }

    /**
     * Transforms text selection.
     *
     * @param transformer Function that will return transformed text.
     * @throws NullPointerException If given argument is {@code null}.
     */
    void transform(UnaryOperator<String> transformer) {
        Objects.requireNonNull(transformer);
        if (isEnabled() == false) {
            return;
        }
        Document document = multipleDocumentModel.getCurrentDocument().getTextComponent().getDocument();
        Caret caret = multipleDocumentModel.getCurrentDocument().getTextComponent().getCaret();

        int start = Math.min(caret.getDot(), caret.getMark());
        int length = Math.abs(caret.getDot() - caret.getMark());
        String selection;
        try {
            selection = document.getText(start, length);
            document.remove(start, length);
            document.insertString(start, transformer.apply(selection), null);
        } catch (BadLocationException ex) {
            return;
        }
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        super.currentDocumentChanged(previousModel, currentModel);
        if(previousModel != null) {
            previousModel.getTextComponent().removeCaretListener(this);
        }
        if(currentModel != null) {
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
}
