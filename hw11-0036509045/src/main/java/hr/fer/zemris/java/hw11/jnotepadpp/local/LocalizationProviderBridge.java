package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

/**
 * Bridge to localization provider.
 *
 * @author Jan Capek
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider implements ILocalizationListener {

    /**
     * Flag that indicates if the bridge is connected.
     */
    private boolean isConnected;

    /**
     * Currently used language.
     */
    private String language;

    /**
     * Localization provider.
     */
    private ILocalizationProvider localizationProvider;

    /**
     * Constructs a new bridge to given localization provider.
     *
     * @param localizationProvider Localization provider this bridge will connect to.
     */
    public LocalizationProviderBridge(ILocalizationProvider localizationProvider) {
        this.localizationProvider = Objects.requireNonNull(localizationProvider);
    }

    /**
     * Connects itself to the localization provider provided in the constructor.
     */
    public void connect() {
        if (isConnected) {
            return;
        }
        localizationProvider.addLocalizationListener(this);
        if (localizationProvider.getCurrentLanguage().equals(language) == false) {
            localizationChanged();
        }
        isConnected = true;
    }

    /**
     * Disconnects itself from localization provided provided in the constructor.
     */
    public void disconnect() {
        if (isConnected) {
            localizationProvider.removeLocalizationListener(this);
            isConnected = false;
        }
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }

    @Override
    public String getString(String key) {
        return localizationProvider.getString(key);
    }

    @Override
    public void localizationChanged() {
        language = localizationProvider.getCurrentLanguage();
        fire();
    }
}
