package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

import java.awt.event.ActionEvent;

/**
 * Action for saving current document.
 *
 * @author Jan Capek
 */
public class SaveAction extends SaveAsAction {

    /**
     * Constructs a new Save action with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public SaveAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SingleDocumentModel current = multipleDocumentModel.getCurrentDocument();
        if(current == null) return;
        if(current.getFilePath() == null) {
            super.actionPerformed(e);
            return;
        }

        try {
            multipleDocumentModel.saveDocument(current, null);
        } catch (Exception ex) {
            uiBridge.showErrorMessage("Error", String.format("Could not save %s file.", current.getFilePath()));
        }
        current.setModified(false);
    }
}
