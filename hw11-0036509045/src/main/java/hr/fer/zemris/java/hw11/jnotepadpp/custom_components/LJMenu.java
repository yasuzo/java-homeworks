package hr.fer.zemris.java.hw11.jnotepadpp.custom_components;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;

/**
 * Localized {@link JMenu} component.
 *
 * @author Jan Capek
 */
public class LJMenu extends JMenu implements ILocalizationListener {

    String nameKey;

    /**
     * Constructs new LJMenu with given name key.
     *
     * @param nameKey Key under which menu name is stored in {@link LocalizationProvider}.
     * @throws NullPointerException If given string is {@code null}.
     * @throws java.util.MissingResourceException If given key does not exist.
     */
    public LJMenu(String nameKey) {
        super(LocalizationProvider.getInstance().getString(nameKey));
        this.nameKey = nameKey;
    }

    @Override
    public void localizationChanged() {
        this.setText(LocalizationProvider.getInstance().getString(nameKey));
    }
}
