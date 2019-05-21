package hr.fer.zemris.java.hw11.jnotepadpp.document.listeners;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

/**
 * Listener model of a {@link SingleDocumentModel}.
 *
 * @author Jan Capek
 */
public interface SingleDocumentListener {

    /**
     * Called when a modification status is updated.
     *
     * @param model Document that was updated.
     * @throws NullPointerException If given model is {@code null}.
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Called when document's file is updated.
     *
     * @param model Document that was updated.
     * @throws NullPointerException If given model is {@code null}.
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
