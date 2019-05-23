package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Action used for saving a document on selected destination.
 *
 * @author Jan Capek
 */
class SaveAsAction extends CurrentDocumentDependantAction {

    /**
     * Constructs a new SaveAs action with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public SaveAsAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SingleDocumentModel doc = multipleDocumentModel.getCurrentDocument();
        if (doc == null) return;

        Path destination = uiBridge.chooseDestinationFile(LocalizationProvider.getInstance().getString("saveAction_name"));
        if (destination == null) return;

//        check if file already exists
        if (Files.exists(destination)) {
            int response = uiBridge.showOptionDialog(
                    LocalizationProvider.getInstance().getString("overwriteWarning_title"),
                    LocalizationProvider.getInstance().getString("overwriteWarning_message"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null);
            if (response != JOptionPane.YES_OPTION) {
                return;
            }
        }

//        save
        try {
            multipleDocumentModel.saveDocument(doc, destination);
        } catch (RuntimeException ex) {
            uiBridge.showErrorMessage("Error", String.format("File could not be saved to %s", destination));
            return;
        }
//            set path if not already set
        if (doc.getFilePath() == null) {
            doc.setFilePath(destination);
            doc.setModified(false);
        }
    }
}
