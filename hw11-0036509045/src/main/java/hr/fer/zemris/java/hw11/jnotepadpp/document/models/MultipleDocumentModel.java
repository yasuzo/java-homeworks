package hr.fer.zemris.java.hw11.jnotepadpp.document.models;

import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;

import java.nio.file.Path;

/**
 * Model of an object consisting of multiple instances of {@link SingleDocumentModel}.
 * This interface allows manipulation of {@link SingleDocumentModel} objects from creating to saving and closing.
 *
 * @author Jan Capek
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

    /**
     * Creates a new {@link SingleDocumentModel} which represent a new document.
     *
     * @return New document.
     */
    SingleDocumentModel createNewDocument();

    /**
     * @return Currently edited document or {@code null} if no documents are opened.
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a document from path.
     *
     * @param path Path to a document.
     * @return Loaded document.
     * @throws IllegalArgumentException If given path does not lead to a readable file.
     * @throws NullPointerException     If given path is {@code null}.
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves a document under given path.
     * This will not update document's modification status.
     *
     * @param model   Document that needs to be saved.
     * @param newPath Save destination.
     * @throws NullPointerException If {@code newPath} is {@code null} and if {@code model.getFilePath() == null};
     *                              If given model is {@code null}.
     * @throws RuntimeException     If document could not be saved.
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes given document. This won't save a document before closing.
     *
     * @param model Document that needs to be closed.
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds a new listener.
     *
     * @param l Listener that needs to be added.
     * @throws NullPointerException If given listener is {@code null}.
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes a listener from internal collection.
     * That listener will not be aware of future changes in this object.
     *
     * @param l Listener that needs to be removed.
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * @return Number of currently held documents by {@code this}.
     */
    int getNumberOfDocuments();

    /**
     * Returns a document on given index.
     *
     * @param index Index of a document that needs to be returned.
     * @return Document on given index.
     * @throws IndexOutOfBoundsException If given index is invalid.
     */
    SingleDocumentModel getDocument(int index);
}
