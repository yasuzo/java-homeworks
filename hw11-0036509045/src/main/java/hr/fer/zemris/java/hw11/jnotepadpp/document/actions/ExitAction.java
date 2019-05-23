package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action for program exiting.
 *
 * @author Jan Capek
 */
public class ExitAction extends EditorAction {

    /**
     * Constructs a new exit action with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public ExitAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean showSavePrompt = isEverythingSaved() == false;

//            some document have not been saved => warn user.
        if (showSavePrompt) {
            int response = uiBridge.showOptionDialog(
                    LocalizationProvider.getInstance().getString("unsavedWarning_title"),
                    LocalizationProvider.getInstance().getString("unsavedWarning_message"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null);
            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        }
//            dispose window
        uiBridge.getParent().dispose();
    }

    /**
     * Checks if all opened documents are saved.
     *
     * @return {@code true} if all documents are saved, {@code false} otherwise.
     */
    private boolean isEverythingSaved() {
        for (SingleDocumentModel doc : multipleDocumentModel) {
            if (doc.isModified()) {
                return false;
            }
        }
        return true;
    }
}
