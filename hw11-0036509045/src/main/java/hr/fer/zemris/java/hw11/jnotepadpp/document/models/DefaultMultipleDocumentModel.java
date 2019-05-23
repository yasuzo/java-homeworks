package hr.fer.zemris.java.hw11.jnotepadpp.document.models;

import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.SingleDocumentListener;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Consumer;

/**
 * {@link JTabbedPane} that implement {@link MultipleDocumentModel}.
 *
 * @author Jan Capek
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    /**
     * Used by {@link DefaultMultipleDocumentModel#setCurrentDocument(int)} to indicate
     * that current document should be set to {@code null}.
     */
    private static final int NULL_DOCUMENT_INDEX = -1;

    /**
     * "Saved" icon.
     */
    private static final Icon ICON_SAVED = getIcon(false);

    /**
     * "Modified" icon.
     */
    private static final Icon ICON_MODIFIED = getIcon(true);

    /**
     * Listeners that need to be notified of changes in this.
     */
    private Set<MultipleDocumentListener> listeners;

    /**
     * List of opened documents.
     */
    private List<SingleDocumentModel> openedDocuments;

    /**
     * Currently edited document.
     */
    private SingleDocumentModel current;

    /**
     * Document listener used for tab update.
     */
    private SingleDocumentListener documentListener;

    /**
     * Constructs a new multiple document model.
     */
    public DefaultMultipleDocumentModel() {
        openedDocuments = new ArrayList<>();
        addChangeListener(e -> setCurrentDocument(this.getSelectedIndex()));
        documentListener = new LocalSingleDocumentListener();
    }

    /**
     * Notifies all listeners of new/removed documents.
     *
     * @param strategy What to dispatch.
     * @throws NullPointerException If given consumer is {@code null}.
     */
    private void dispatchAddedRemoved(Consumer<MultipleDocumentListener> strategy) {
        Objects.requireNonNull(strategy);
        if (listeners == null) return;
        listeners.forEach(strategy);
    }

    /**
     * Notifies all listeners of current document change.
     *
     * @param old Old current document; can be {@code null} if {@link DefaultMultipleDocumentModel#current} is not.
     * @throws NullPointerException If both given parameter and {@link DefaultMultipleDocumentModel#current} are {@code null}.
     */
    private void dispatchCurrentDocumentChanged(SingleDocumentModel old) {
        if (listeners == null) return;
        listeners.forEach(l -> l.currentDocumentChanged(old, current));
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel doc = new DefaultSingleDocumentModel(null, "");
        addDocument(doc);
        dispatchAddedRemoved(l -> l.documentAdded(doc));
        setCurrentDocument(openedDocuments.size() - 1);
        return doc;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return current;
    }

    /**
     * Sets current document to a document at given index.
     *
     * @param index Index of a new current document or {@link DefaultMultipleDocumentModel#NULL_DOCUMENT_INDEX}
     *              to set current document to {@code null}.
     * @throws IndexOutOfBoundsException If given index is not {@link DefaultMultipleDocumentModel#NULL_DOCUMENT_INDEX} and is illegal.
     * @throws NullPointerException      If old current document is {@code null} and new current document is also {@code null}.
     */
    private void setCurrentDocument(int index) {
        SingleDocumentModel old = current;
        current = index == NULL_DOCUMENT_INDEX ? null : openedDocuments.get(index);
        setSelectedIndex(index);
        dispatchCurrentDocumentChanged(old);
    }

    /**
     * Adds given document to a list of opened documents and also adds a tab for that document to the JTabbedPane.
     *
     * @param doc Document that needs to be added.
     * @throws NullPointerException If given document is {@code null}.
     */
    private void addDocument(SingleDocumentModel doc) {
        Objects.requireNonNull(doc);
        openedDocuments.add(doc);
        doc.addSingleDocumentListener(documentListener);

        Path docPath = doc.getFilePath();
        String tabName = docPath == null ? "(unnamed)" : docPath.getFileName().toString();
        String toolTipText = docPath == null ? "(unnamed)" : docPath.toString();
        addTab(tabName, doc.isModified() ? ICON_MODIFIED : ICON_SAVED, new JScrollPane(doc.getTextComponent()), toolTipText);
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        Objects.requireNonNull(path);

//        read contents and create document object
        String content;
        try {
            content = Files.readString(path);
        } catch (IOException | SecurityException e) {
            throw new IllegalArgumentException("Given file could not be read.");
        }
        SingleDocumentModel doc = new DefaultSingleDocumentModel(path, content);

//        check if loaded document already exists
        int index = openedDocuments.indexOf(doc);
        if (index == -1) {
//            document does not exist => add it
            addDocument(doc);
            dispatchAddedRemoved(l -> l.documentAdded(doc));
            index = openedDocuments.size() - 1;
        }

//        update current document
        setCurrentDocument(index);
        return doc;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        Objects.requireNonNull(model);
        Path destination = newPath == null ? Objects.requireNonNull(model.getFilePath()) : newPath;
        String content = model.getTextComponent().getText();
        try {
            Files.writeString(destination, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Document could not be saved.");
        }
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = openedDocuments.indexOf(model);
        if (index == -1) {
            return;
        }
        openedDocuments.remove(index);
        removeTabAt(index);
        model.removeSingleDocumentListener(documentListener);
        dispatchAddedRemoved(l -> l.documentRemoved(model));

        // if closed document was current then update current
        if (model.equals(current)) {
            setCurrentDocument(openedDocuments.size() > 0 ? 0 : NULL_DOCUMENT_INDEX);
        }
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        Objects.requireNonNull(l);
        if (listeners == null) {
            listeners = new HashSet<>();
        }
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        if (listeners == null) return;
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return openedDocuments.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return openedDocuments.get(index);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return openedDocuments.iterator();
    }

    /**
     * Returns icon for given modification status if completed successfully.
     *
     * @param modified If {@code true} "modified" icon will be returned, otherwise "saved" icon will be returned.
     * @return Icon or {@code null} if an error occurred.
     */
    private static Icon getIcon(boolean modified) {
        String path = modified ? "/icons/modified.png" : "/icons/saved.png";
        InputStream is = DefaultMultipleDocumentModel.class.getResourceAsStream(path);
        if (is == null) {
            return null;
        }
        byte[] bytes;
        try {
            bytes = is.readAllBytes();
            is.close();
        } catch (IOException e) {
            return null;
        }
        return new ImageIcon(bytes);
    }

    /**
     * Local single document listener.
     */
    private class LocalSingleDocumentListener implements SingleDocumentListener {

        @Override
        public void documentModifyStatusUpdated(SingleDocumentModel model) {
            int index = openedDocuments.indexOf(model);
            if (index != -1) {
                DefaultMultipleDocumentModel.this.setIconAt(index, model.isModified() ? ICON_MODIFIED : ICON_SAVED);
            }
        }

        @Override
        public void documentFilePathUpdated(SingleDocumentModel model) {
            int index = -1;
            for (SingleDocumentModel doc : openedDocuments) {
                index++;
                if (model == doc) {
                    setTitleAt(index, doc.getFilePath().getFileName().toString());
                    setToolTipTextAt(index, doc.getFilePath().toString());
                    break;
                }
            }
        }
    }
}
