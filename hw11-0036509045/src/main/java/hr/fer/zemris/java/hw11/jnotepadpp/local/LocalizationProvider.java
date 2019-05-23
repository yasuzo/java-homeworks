package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Objects of this class provide language localization.
 *
 * @author Jan Capek
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Created instance of localization provider. Every time {@link LocalizationProvider#getInstance()}
     * is called this instance will be returned (singleton).
     */
    private static LocalizationProvider instance;

    /**
     * Current language.
     */
    private String language;

    /**
     * Translation bundle.
     */
    private ResourceBundle bundle;

    /**
     * Constructs new LocalizationProvider.
     */
    private LocalizationProvider() {
        setLanguage("en");
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * Sets language to the given language.
     *
     * @param languageTag Tag of the new language.
     * @throws NullPointerException               If given language is {@code null}.
     * @throws java.util.MissingResourceException If given tag is not found.
     */
    public void setLanguage(String languageTag) {
        Objects.requireNonNull(languageTag);
        if (languageTag.equals(this.language)) {
            return;
        }
        Locale locale = Locale.forLanguageTag(languageTag);
        bundle = ResourceBundle.getBundle("locale.language", locale);
        language = languageTag;
        fire();
    }

    /**
     * @return Instance of {@link LocalizationProvider}.
     */
    public static LocalizationProvider getInstance() {
        if (instance == null) {
            instance = new LocalizationProvider();
        }
        return instance;
    }
}
