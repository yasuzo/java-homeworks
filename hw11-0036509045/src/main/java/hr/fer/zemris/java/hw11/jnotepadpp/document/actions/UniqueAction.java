package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Action that removes all selected duplicate lines.
 *
 * @author Jan Capek
 */
public class UniqueAction extends TransformSelectionAction {

    /**
     * Constructs a new action for removing duplicate lines from selection.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public UniqueAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("unique");
        if(isEnabled() == false) {
            return;
        }
        LineResult lineResult = getSelectedLines();
        transform(s -> {
            Set<String> set = new LinkedHashSet<>(lineResult.getLines());
//            create text
            String text = String.join("\n", set);
            if (lineResult.endsWithNewLine()) {
                text += "\n";
            }
            return text;
        }, lineResult.getStartIndex(), lineResult.getLength());
    }
}
