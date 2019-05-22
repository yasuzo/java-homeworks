package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

/**
 * Action dependant on current document. If current document is not selected, this action will be disabled.
 *
 * @author Jan Capek
 */
public abstract class CurrentDocumentDependantAction extends EditorAction implements MultipleDocumentListener {
    /**
     * Constructs a new action dependant on current document with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public CurrentDocumentDependantAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
        multipleDocumentModel.addMultipleDocumentListener(this);
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        boolean enable = currentModel != null;
        setEnabled(enable);
    }

    @Override
    public void documentAdded(SingleDocumentModel model) {
    }

    @Override
    public void documentRemoved(SingleDocumentModel model) {
    }
}
