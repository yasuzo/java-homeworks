package hr.fer.zemris.java.hw11.jnotepadpp.document.models;

import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.SingleDocumentListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOError;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Default implementation of {@link SingleDocumentModel}.
 *
 * @author Jan Capek
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private Set<SingleDocumentListener> listeners;

    private Path documentPath;
    private String content;
    private boolean isModified;
    private JTextArea documentEditor;

    /**
     * Constructs a new {@link SingleDocumentModel} with given attributes.
     *
     * @param documentPath Document's path; can be {@code null} in case it is a new document.
     * @param content      Document contents.
     * @throws NullPointerException If given content is {@code null}.
     * @throws IllegalArgumentException If document path is not {@code null} and not accessible.
     */
    public DefaultSingleDocumentModel(Path documentPath, String content) {
        try {
            this.documentPath = documentPath == null ? null : documentPath.toAbsolutePath().normalize();
        } catch (IOError | SecurityException e) {
            throw new IllegalArgumentException("Given path is not accessible.");
        }
        this.content = Objects.requireNonNull(content);
        this.isModified = documentPath == null; // documentPath is null => new document is created
        this.documentEditor = new JTextArea(content);
        this.documentEditor.getDocument().addDocumentListener(new LocalDocumentListener());
    }

    @Override
    public JTextArea getTextComponent() {
        return documentEditor;
    }

    @Override
    public Path getFilePath() {
        return documentPath;
    }

    @Override
    public void setFilePath(Path path) {
        this.documentPath = Objects.requireNonNull(path);
        notifyListeners(l -> l.documentFilePathUpdated(this));
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void setModified(boolean modified) {
        boolean shouldUpdate = isModified != modified;
        if (shouldUpdate) {
            isModified = modified;
            notifyListeners(l -> l.documentModifyStatusUpdated(this));
        }
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        Objects.requireNonNull(l);
        if (listeners == null) {
            listeners = new HashSet<>();
        }
        if (listeners.contains(l) == false) {
            listeners = new HashSet<>(listeners);
        }
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        if (listeners == null || listeners.contains(l) == false) {
            return;
        }
        listeners = new HashSet<>(listeners);
        listeners.remove(l);
    }

    /**
     * Notifies all listeners by given strategy.
     *
     * @param strategy What to notify listeners about.
     * @throws NullPointerException If given strategy is {@code null}.
     */
    private void notifyListeners(Consumer<SingleDocumentListener> strategy) {
        Objects.requireNonNull(strategy);
        if (listeners == null) return;
        for (SingleDocumentListener l : listeners) {
            strategy.accept(l);
        }
    }

    /**
     * Compares {@code this} with given object.
     * Two {@link DefaultSingleDocumentModel} are equal if they have the same path.
     *
     * @param o Object that needs to be compared to {@code this}.
     * @return {@code true} if equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultSingleDocumentModel that = (DefaultSingleDocumentModel) o;
        if(this.documentPath == null) {
            return this == that;
        }
//        todo: Paths should not be compared like this.
        return Objects.equals(documentPath, that.documentPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentPath, documentPath);
    }

    /**
     * DocumentListener for this document.
     */
    private class LocalDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            documentUpdate(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            documentUpdate(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
//            do nothing
        }

        private void documentUpdate(DocumentEvent e) {
            DefaultSingleDocumentModel.this.setModified(true);
            DefaultSingleDocumentModel.this.content = e.getDocument().toString();
        }
    }
}
