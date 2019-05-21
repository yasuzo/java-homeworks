package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action for closing current document.
 *
 * @author Jan Capek
 */
public class CloseDocumentAction extends CurrentDocumentDependantAction {

    /**
     * Constructs a new action for closing current document with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public CloseDocumentAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SingleDocumentModel current = multipleDocumentModel.getCurrentDocument();
        if(current == null) return;
        if(current.isModified()) {
            int response = uiBridge.showOptionDialog(
                    "Unsaved document",
                    "Document is not saved.\nDo you want to continue?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null);
            if(response != JOptionPane.YES_OPTION) {
                return;
            }
        }
        multipleDocumentModel.closeDocument(current);
    }
}
