package hr.fer.zemris.java.hw11.jnotepadpp.document.listeners;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

/**
 * Model of a listener for changes of {@link MultipleDocumentModel}.
 *
 * @author Jan Capek
 */
public interface MultipleDocumentListener {

    /**
     * Called when current documents are switched.
     *
     * @param previousModel Previous document.
     * @param currentModel Current document.
     * @throws NullPointerException If both arguments are {@code null}.
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Called when a document was added (opened or created).
     * @param model Newly added document.
     * @throws NullPointerException If given model is {@code null}.
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Called when a document was removed (closed).
     *
     * @param model Document that was removed.
     * @throws NullPointerException If given model is {@code null}.
     */
    void documentRemoved(SingleDocumentModel model);
}
