package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import javax.swing.*;
import java.util.Objects;

/**
 * Abstract editor action class.
 *
 * @author Jan Capek
 */
abstract class EditorAction extends AbstractAction {

    /**
     * UI communication bridge.
     */
    UIBridge uiBridge;

    /**
     * Model holding multiple editable documents.
     */
    MultipleDocumentModel multipleDocumentModel;

    /**
     * Constructs a new EditorAction with given attributes.
     *
     * @param uiBridge Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public EditorAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        this.uiBridge = Objects.requireNonNull(uiBridge);
        this.multipleDocumentModel = Objects.requireNonNull(multipleDocumentModel);
    }
}
