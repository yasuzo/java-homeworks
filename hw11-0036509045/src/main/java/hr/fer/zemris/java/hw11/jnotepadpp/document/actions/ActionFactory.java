package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import hr.fer.zemris.java.hw11.jnotepadpp.document.models.MultipleDocumentModel;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * Singleton action factory used to create {@link javax.swing.Action} objects.
 *
 * @author Jan Capek
 */
public class ActionFactory {

    /**
     * Instance of {@code this} that will be returned every time
     * {@link ActionFactory#getActionFactory(JFrame, MultipleDocumentModel)} is called.
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
     * Constructs an action factory.
     *
     * @param mainFrame Main window where actions will be used.
     * @param model     Document model used for document manipulation.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    private ActionFactory(JFrame mainFrame, MultipleDocumentModel model) {
        this.model = Objects.requireNonNull(model);
        this.uiBridge = new UIBridge(mainFrame);

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

        initActions();
    }

    /**
     * Creates a new action factory if it has not been already created, if it has, existing factory is returned.
     *
     * @param mainFrame Main window where actions will be used.
     * @param model     Document model used for document manipulation.
     * @return Instance of {@link ActionFactory}.
     * @throws NullPointerException If any of the parameters is {@code null}.
     */
    public static ActionFactory getActionFactory(JFrame mainFrame, MultipleDocumentModel model) {
        return actionFactory == null ? new ActionFactory(mainFrame, model) : actionFactory;
    }

    /**
     * Initializes actions.
     */
    private void initActions() {

        openExistingDocumentAction.putValue(Action.NAME, "Open");
        openExistingDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openExistingDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openExistingDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Open document from disk.");

        createNewDocumentAction.putValue(Action.NAME, "New");
        createNewDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        createNewDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        createNewDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Create new document.");

        saveAction.putValue(Action.NAME, "Save");
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveAction.putValue(Action.SHORT_DESCRIPTION, "Saves a document under documents default path.");
        saveAction.setEnabled(false);

        saveAsAction.putValue(Action.NAME, "Save as...");
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        saveAsAction.putValue(Action.SHORT_DESCRIPTION, "Saves a document under chosen path.");
        saveAsAction.setEnabled(false);

        closeAction.putValue(Action.NAME, "Close");
        closeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift C"));
        closeAction.putValue(Action.SHORT_DESCRIPTION, "Closes currently edited document.");
        closeAction.setEnabled(false);

        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit program.");

//        todo: make cut, copy and paste unavailable when there is no current document.
        cutAction.putValue(Action.NAME, "Cut");
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        cutAction.putValue(Action.SHORT_DESCRIPTION, "Cuts portion of the text.");

        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        copyAction.putValue(Action.SHORT_DESCRIPTION, "Copies portion of the text.");

        pasteAction.putValue(Action.NAME, "Paste");
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
        pasteAction.putValue(Action.SHORT_DESCRIPTION, "Pastes text in clipboard.");

        documentStatisticsAction.putValue(Action.NAME, "Stats");
        documentStatisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        documentStatisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
        documentStatisticsAction.putValue(Action.SHORT_DESCRIPTION, "Shows document stats.");
        documentStatisticsAction.setEnabled(false);
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
}
