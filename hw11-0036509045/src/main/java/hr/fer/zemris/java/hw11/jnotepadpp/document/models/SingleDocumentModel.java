package hr.fer.zemris.java.hw11.jnotepadpp.document.models;

import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.SingleDocumentListener;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Model of a single document. It offers information about a document.
 *
 * @author Jan Capek
 */
public interface SingleDocumentModel {

    /**
     * @return Text area used for editing this document.
     */
    JTextArea getTextComponent();

    /**
     * @return Path to a document or {@code null} if document does not have a path.
     */
    Path getFilePath();

    /**
     * Sets document's path.
     *
     * @param path New document path.
     * @throws NullPointerException If given path is {@code null}.
     */
    void setFilePath(Path path);

    /**
     * Returns document's modification status.
     *
     * @return {@code true} if document was modified, {@code false} otherwise.
     */
    boolean isModified();

    /**
     * Sets document's modification status.
     *
     * @param modified New status.
     */
    void setModified(boolean modified);

    /**
     * Registers a new listener.
     *
     * @param l New listener.
     * @throws NullPointerException If given listener is {@code null}.
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes given listener from internal collection.
     * That listener will not be aware of future changes of this.
     *
     * @param l Listener that needs to be removed.
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}
