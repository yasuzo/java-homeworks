package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import java.awt.event.ActionEvent;

/**
 * Action for transforming selected text to lower case.
 *
 * @author Jan Capek
 */
public class ToLowerAction extends TransformSelectionAction {

    /**
     * Constructs a new action that transforms selected text to lower case.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public ToLowerAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        transformSelected(String::toLowerCase);
    }
}
