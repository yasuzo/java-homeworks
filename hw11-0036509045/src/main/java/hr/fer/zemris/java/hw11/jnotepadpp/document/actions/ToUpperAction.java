package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;

/**
 * Action that transforms selection to upper case.
 *
 * @author Jan Capek
 */
public class ToUpperAction extends TransformSelectionAction {

    /**
     * Constructs a new action that transforms text selection to uppercase.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public ToUpperAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        transform(String::toUpperCase);
    }
}
