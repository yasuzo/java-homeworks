package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import java.awt.event.ActionEvent;
import java.nio.file.Path;

/**
 * Action used for opening an existing document.
 *
 * @author Jan Capek
 */
public class OpenAction extends EditorAction {

    /**
     * Constructs a new open document action with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public OpenAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Path path = uiBridge.chooseFile(LocalizationProvider.getInstance().getString("openAction_name"));
        if (path == null) {
            return;
        }

        try {
            multipleDocumentModel.loadDocument(path);
        } catch (IllegalArgumentException ex) {
            String title = LocalizationProvider.getInstance().getString("error_title");
            String message_format = LocalizationProvider.getInstance().getString("unreadableFileError_format");
            uiBridge.showErrorMessage(title, String.format(message_format, path));
        }
    }
}
