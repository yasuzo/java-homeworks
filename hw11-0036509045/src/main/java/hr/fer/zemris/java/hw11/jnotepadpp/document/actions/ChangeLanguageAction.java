package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import com.sun.jdi.ObjectReference;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * Action that changes localization language.
 *
 * @author Jan Capek
 */
public class ChangeLanguageAction extends AbstractAction {

    private String languageTag;

    /**
     * Constructs a new action.
     *
     * @param languageTag Language this action changes localization to.
     * @throws NullPointerException If given tag is {@code null}.
     */
    public ChangeLanguageAction(String languageTag) {
        this.languageTag = Objects.requireNonNull(languageTag);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LocalizationProvider.getInstance().setLanguage(languageTag);
    }
}
