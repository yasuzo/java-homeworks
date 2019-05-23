package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Action that sorts selected lines.
 *
 * @author Jan Capek
 */
public class SortAction extends TransformSelectionAction {

    /**
     * Comparator used for sorting.
     */
    private Comparator<String> comparator;

    /**
     * Constructs a new action for sorting selected lines.
     *
     * @param uiBridge              Bridge between UI and actions.
     * @param multipleDocumentModel Multiple document model holding documents.
     * @param comparator            Comparator used for sorting.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public SortAction(UIBridge uiBridge, MultipleDocumentModel multipleDocumentModel, Comparator<String> comparator) {
        super(uiBridge, multipleDocumentModel);
        setComparator(comparator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isEnabled() == false) {
            return;
        }

        LineResult lineResult = getSelectedLines();
        transform(s -> {
            List<String> sorted = lineResult.getLines().stream().sorted(comparator).collect(Collectors.toList());
//            create text
            String text = String.join("\n", sorted);
            if (lineResult.endsWithNewLine()) {
                text += "\n";
            }
            return text;
        }, lineResult.getStartIndex(), lineResult.getLength());
    }

    /**
     * Sets new comparator.
     *
     * @param comparator Comparator used for sorting.
     * @throws NullPointerException If given comparator is {@code null}.
     */
    public void setComparator(Comparator<String> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }
}
