package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import java.awt.event.ActionEvent;

/**
 * Action that inverts selected text.
 *
 * @author Jan Capek
 */
public class InvertCaseAction extends TransformSelectionAction {

    /**
     * Constructs a new action dependant on text selection.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public InvertCaseAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        transform(selection -> {
            char[] chars = selection.toCharArray();
            for(int i = 0; i < chars.length; i++) {
                if(Character.isUpperCase(chars[i])) {
                    chars[i] = Character.toLowerCase(chars[i]);
                }else if (Character.isLowerCase(chars[i])) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
            }
            return new String(chars);
        });
    }
}
