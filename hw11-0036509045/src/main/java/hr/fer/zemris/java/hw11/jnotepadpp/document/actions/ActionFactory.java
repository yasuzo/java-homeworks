package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Singleton action factory used to create {@link javax.swing.Action} objects.
 *
 * @author Jan Capek
 */
public class ActionFactory implements ILocalizationListener {

    /**
     * Instance of {@code this} that will be returned every time
     * {@link ActionFactory#getActionFactory(LocalizationProviderBridge, UIBridge, MultipleDocumentModel)} is called.
     */
    public static ActionFactory actionFactory;

    /**
     * Document model used for document manipulation.
     */
    private MultipleDocumentModel model;

    /**
     * Bridge between actions and UI.
     */
    private UIBridge uiBridge;

    /**
     * Creates a new document.
     */
    private final Action createNewDocumentAction;

    /**
     * Opens a new document.
     */
    private final Action openExistingDocumentAction;

    /**
     * SaveAs action.
     */
    private final Action saveAsAction;

    /**
     * Save action.
     */
    private final Action saveAction;

    /**
     * Closes currently opened document.
     */
    private final Action closeAction;

    /**
     * Exits a program.
     */
    private final Action exitAction;

    /**
     * Cut action.
     */
    private final Action cutAction;

    /**
     * Copy action.
     */
    private final Action copyAction;

    /**
     * Paste action.
     */
    private final Action pasteAction;

    /**
     * Shows stats about the document.
     */
    private final Action documentStatisticsAction;

    /**
     * Changes language to german.
     */
    private final Action languageDeAction;

    /**
     * Changes language to english.
     */
    private final Action languageEnAction;

    /**
     * Changes language to croatian.
     */
    private final Action languageHrAction;

    /**
     * Action that inverts case of selected text.
     */
    private final Action invertCaseAction;

    /**
     * Action that transforms selected text to upper case.
     */
    private final Action toUpperCaseAction;

    /**
     * Action that transforms selected text to lower case.
     */
    private final Action toLowerCaseAction;

    /**
     * Constructs an action factory.
     *
     * @param localizationBridge Bridge to localization provider.
     * @param uiBridge Bridge to the parent frame.
     * @param model     Document model used for document manipulation.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public ActionFactory(LocalizationProviderBridge localizationBridge, UIBridge uiBridge, MultipleDocumentModel model) {
        this.uiBridge = Objects.requireNonNull(uiBridge);
        Objects.requireNonNull(localizationBridge).addLocalizationListener(this);

        createNewDocumentAction = new CreateAction(uiBridge, model);
        openExistingDocumentAction = new OpenAction(uiBridge, model);
        saveAction = new SaveAction(uiBridge, model);
        saveAsAction = new SaveAsAction(uiBridge, model);
        exitAction = new ExitAction(uiBridge, model);
        closeAction = new CloseDocumentAction(uiBridge, model);
        cutAction = new DefaultEditorKit.CutAction();
        copyAction = new DefaultEditorKit.CopyAction();
        pasteAction = new DefaultEditorKit.PasteAction();
        documentStatisticsAction = new StatisticsAction(uiBridge, model);
        languageDeAction = new ChangeLanguageAction("de");
        languageEnAction = new ChangeLanguageAction("en");
        languageHrAction = new ChangeLanguageAction("hr");
        invertCaseAction = new InvertCaseAction(uiBridge, model);
        toUpperCaseAction = new ToUpperAction(uiBridge, model);
        toLowerCaseAction = new ToLowerAction(uiBridge, model);

        initActions();
    }

    /**
     * Initializes actions.
     */
    private void initActions() {
        initAction(openExistingDocumentAction, "openAction_name", KeyStroke.getKeyStroke("control O"), "openAction_desc");
        initAction(createNewDocumentAction, "newAction_name", KeyStroke.getKeyStroke("control N"), "newAction_desc");
        initAction(saveAction, "saveAction_name", KeyStroke.getKeyStroke("control S"),
                "saveAction_desc", false);
        initAction(saveAsAction, "saveAsAction_name", KeyStroke.getKeyStroke("control shift S"),
                "saveAsAction_desc", false);
        initAction(closeAction, "closeAction_name", KeyStroke.getKeyStroke("control shift C"),
                "closeAction_desc", false);
        initAction(exitAction, "exitAction_name", KeyStroke.getKeyStroke("control Q"), "exitAction_desc");

//        todo: make cut, copy and paste unavailable when there is no current document.
        initAction(cutAction, "cutAction_name", KeyStroke.getKeyStroke("control X"), "cutAction_desc");
        initAction(copyAction, "copyAction_name", KeyStroke.getKeyStroke("control C"), "copyAction_desc");
        initAction(pasteAction, "pasteAction_name", KeyStroke.getKeyStroke("control V"), "pasteAction_desc");
        initAction(documentStatisticsAction, "statsAction_name", KeyStroke.getKeyStroke("control I"),
                "statsAction_desc", false);

        initAction(languageDeAction, "langDeAction_name", null, "langDeAction_desc");
        initAction(languageEnAction, "langEnAction_name", null, "langEnAction_desc");
        initAction(languageHrAction, "langHrAction_name", null, "langHrAction_desc");

        initAction(toLowerCaseAction, "toLowerAction_name", null, "toLowerAction_desc", false);
        initAction(toUpperCaseAction, "toUpperAction_name", null, "toUpperAction_desc", false);
        initAction(invertCaseAction, "invertCaseAction_name", null, "invertCaseAction_desc", false);
    }

    /**
     * Sets action name and description gotten from localization provider based on given keys.
     *
     * @param action Action whose attributes need to be set.
     * @param nameKey Key under which action's name is stored.
     * @param descriptionKey Key under which action's description is stored.
     * @throws NullPointerException If any of the parameters are {@code null}.
     * @throws java.util.MissingResourceException If any of the keys could not be found.
     */
    private void setActionNameAndDescription(Action action, String nameKey, String descriptionKey) {
        Objects.requireNonNull(action);
        action.putValue(Action.NAME, LocalizationProvider.getInstance().getString(nameKey));
        action.putValue(Action.SHORT_DESCRIPTION, LocalizationProvider.getInstance().getString(descriptionKey));
    }

    /**
     * Initializes given action with given attributes. Action will be enabled by default.
     *
     * @param action Action that needs to be initialized.
     * @param name Action's name.
     * @param shortcut Keyboard shortcut.
     * @param description Action's description.
     * @throws NullPointerException If given action is {@code null}.
     */
    private void initAction(Action action, String name, KeyStroke shortcut, String description) {
        initAction(action, name, shortcut, description, true);
    }

    /**
     * Initializes given action with given attributes.
     *
     * @param action Action that needs to be initialized.
     * @param name Action's name.
     * @param shortcut Keyboard shortcut.
     * @param description Action's description.
     * @param enabled Enabled flag.
     * @throws NullPointerException If given action is {@code null}.
     */
    private void initAction(Action action, String name, KeyStroke shortcut, String description, boolean enabled) {
        Objects.requireNonNull(action);
        setActionNameAndDescription(action, name, description);
        action.putValue(Action.ACCELERATOR_KEY, shortcut);
        action.setEnabled(enabled);
    }

    /**
     * @return Create new document action.
     */
    public Action getCreateNewDocumentAction() {
        return createNewDocumentAction;
    }

    /**
     * @return Open existing document action.
     */
    public Action getOpenExistingDocumentAction() {
        return openExistingDocumentAction;
    }

    /**
     * @return Document save action.
     */
    public Action getSaveAction() {
        return saveAction;
    }

    /**
     * @return SaveAs action.
     */
    public Action getSaveAsAction() {
        return saveAsAction;
    }

    /**
     * @return Action that closes current document.
     */
    public Action getCloseAction() {
        return closeAction;
    }

    /**
     * @return Exit program action.
     */
    public Action getExitAction() {
        return exitAction;
    }

    /**
     * @return Cut action.
     */
    public Action getCutAction() {
        return cutAction;
    }

    /**
     * @return Copy action.
     */
    public Action getCopyAction() {
        return copyAction;
    }

    /**
     * @return Paste action.
     */
    public Action getPasteAction() {
        return pasteAction;
    }

    /**
     * @return Action that shows current document stats.
     */
    public Action getDocumentStatisticsAction() {
        return documentStatisticsAction;
    }

    /**
     * @return German language action.
     */
    public Action getLanguageDeAction() {
        return languageDeAction;
    }

    /**
     * @return English language action.
     */
    public Action getLanguageEnAction() {
        return languageEnAction;
    }

    /**
     * @return Croatian language action.
     */
    public Action getLanguageHrAction() {
        return languageHrAction;
    }

    /**
     * @return Invert case action.
     */
    public Action getInvertCaseAction() {
        return invertCaseAction;
    }

    /**
     * @return Convert to upper case action.
     */
    public Action getToUpperCaseAction() {
        return toUpperCaseAction;
    }

    /**
     * @return Convert to lower case action.
     */
    public Action getToLowerCaseAction() {
        return toLowerCaseAction;
    }

    @Override
    public void localizationChanged() {
        setActionNameAndDescription(openExistingDocumentAction, "openAction_name", "openAction_desc");
        setActionNameAndDescription(createNewDocumentAction, "newAction_name", "newAction_desc");
        setActionNameAndDescription(saveAction, "saveAction_name", "saveAction_desc");
        setActionNameAndDescription(saveAsAction, "saveAsAction_name", "saveAsAction_desc");
        setActionNameAndDescription(closeAction, "closeAction_name", "closeAction_desc");
        setActionNameAndDescription(exitAction, "exitAction_name", "exitAction_desc");
        setActionNameAndDescription(cutAction, "cutAction_name", "cutAction_desc");
        setActionNameAndDescription(copyAction, "copyAction_name", "copyAction_desc");
        setActionNameAndDescription(pasteAction, "pasteAction_name", "pasteAction_desc");
        setActionNameAndDescription(documentStatisticsAction, "statsAction_name", "statsAction_desc");
        setActionNameAndDescription(languageDeAction, "langDeAction_name", "langDeAction_desc");
        setActionNameAndDescription(languageEnAction, "langEnAction_name", "langEnAction_desc");
        setActionNameAndDescription(languageHrAction, "langHrAction_name", "langHrAction_desc");
        setActionNameAndDescription(toLowerCaseAction, "toLowerAction_name", "toLowerAction_desc");
        setActionNameAndDescription(toUpperCaseAction, "toUpperAction_name", "toUpperAction_desc");
        setActionNameAndDescription(invertCaseAction, "invertCaseAction_name", "invertCaseAction_desc");
    }
}
