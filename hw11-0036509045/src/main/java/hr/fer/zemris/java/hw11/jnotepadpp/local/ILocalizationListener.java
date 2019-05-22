package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Localization listener interface. Every time localization is changed,
 * {@link ILocalizationProvider} will notify listener through its method.
 *
 * @author Jan Capek
 */
public interface ILocalizationListener {

    /**
     * Called by {@link ILocalizationProvider} every time localization language is changed.
     */
    void localizationChanged();
}
