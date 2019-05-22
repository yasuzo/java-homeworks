package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Abstract implementation of {@link ILocalizationProvider}.
 * This class only implements methods for notifying {@link ILocalizationListener} objects.
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    private Set<ILocalizationListener> listeners;

    /**
     * Default constructor.
     */
    public AbstractLocalizationProvider() {
        listeners = new CopyOnWriteArraySet<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener l) {
        Objects.requireNonNull(l);
        listeners.add(l);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all listeners of localization change.
     */
    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }

}
