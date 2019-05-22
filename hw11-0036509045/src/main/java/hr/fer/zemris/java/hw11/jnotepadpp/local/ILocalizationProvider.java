package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface that provides localization.
 *
 * @author Jan Capek
 */
public interface ILocalizationProvider {

    /**
     * @return Current localization language.
     */
    String getCurrentLanguage();

    /**
     * Adds localization listener.
     *
     * @param l Listener that needs to be added.
     * @throws NullPointerException If given listener is {@code null}.
     */
    void addLocalizationListener(ILocalizationListener l);

    /**
     * Removes given listener.
     *
     * @param l Listener that needs to be removed.
     */
    void removeLocalizationListener(ILocalizationListener l);

    /**
     * Returns translation for given key.
     *
     * @param key Key under which translation is stored.
     * @return Translation for given key.
     * @throws NullPointerException               If given key is {@code null}.
     * @throws java.util.MissingResourceException If there is no translation for given key.
     */
    String getString(String key);
}
