package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * Action for showing document statistics.
 *
 * @author Jan Capek
 */
public class StatisticsAction extends CurrentDocumentDependantAction {

    /**
     * Constructs a new action that displays statistics of current document with given attributes.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public StatisticsAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel) {
        super(uiBridge, multipleDocumentModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SingleDocumentModel current = multipleDocumentModel.getCurrentDocument();
        if(current == null) return;

        DocumentStatistic stat = getStatistic(current);
        uiBridge.showOptionDialog("Stats", stat.toString(), JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null);
    }

    /**
     * Returns document statistics.
     *
     * @param doc Document whose statistics need to be returned.
     * @return Document't statistics.
     * @throws NullPointerException If given document is {@code null}.
     */
    private DocumentStatistic getStatistic(SingleDocumentModel doc) {
        Objects.requireNonNull(doc);
        DocumentStatistic result = new DocumentStatistic();
        char[] chars = doc.getTextComponent().getText().toCharArray();
        for(char c : chars) {
            result.charNumber++;
            if(Character.isWhitespace(c) == false) {
                result.nonBlankCharNumber++;
            }
            if(c == '\n') {
                result.linesNumber++;
            }
        }
        result.linesNumber++;
        return result;
    }

    /**
     * Represents document statistics data structure.
     */
    private static class DocumentStatistic {
        private int charNumber;
        private int nonBlankCharNumber;
        private int linesNumber;

        @Override
        public String toString() {
            return String.format("Your document has %d characters, %d non-blank characters and %d lines.",
                    charNumber, nonBlankCharNumber, linesNumber);
        }
    }
}
