package hr.fer.zemris.java.hw11.jnotepadpp;

import hr.fer.zemris.java.hw11.jnotepadpp.custom_components.JStatusBar;
import hr.fer.zemris.java.hw11.jnotepadpp.custom_components.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.document.actions.ActionFactory;
import hr.fer.zemris.java.hw11.jnotepadpp.document.actions.UIBridge;
import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.listeners.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.document.models.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * My Notepad++ program.
 *
 * @author Jan Capek
 */
public class JNotepadPP extends JFrame {

    /**
     * Action factory used by components.
     */
    private ActionFactory actionFactory;

    /**
     * Bridge to this JFrame.
     */
    private UIBridge uiBridge;

    /**
     * Multiple document model used by actions.
     */
    private DefaultMultipleDocumentModel multipleDocumentModel;

    /**
     * Localization bridge.
     */
    private LocalizationProviderBridge localizationProviderBridge;

    /**
     * Listens for window close event.
     */
    private WindowListener localWindowListener = new WindowAdapter() {

        @Override
        public void windowOpened(WindowEvent e) {
            localizationProviderBridge.connect();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            MyThreadPool.shutdown();
            localizationProviderBridge.disconnect();
            uiBridge.disconnectParent();
        }

        @Override
        public void windowClosing(WindowEvent e) {
            actionFactory.getExitAction().actionPerformed(null);
        }
    };

    /**
     * Listener that changes title of this JFrame according to currently edited document.
     */
    private DocumentListener titleDocumentListener = new DocumentListener();

    /**
     * Constructs a new main window.
     */
    public JNotepadPP() {
        uiBridge = new UIBridge(this);
        multipleDocumentModel = new DefaultMultipleDocumentModel();
        localizationProviderBridge = new LocalizationProviderBridge(LocalizationProvider.getInstance());
        actionFactory = new ActionFactory(localizationProviderBridge, uiBridge, multipleDocumentModel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(900, 700);
        setTitle("JNotepad++");
        addWindowListener(localWindowListener);
        multipleDocumentModel.addMultipleDocumentListener(titleDocumentListener);
        initGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }

    /**
     * GUI initialization.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        this.add(multipleDocumentModel, BorderLayout.CENTER);

//        menu bar
        this.setJMenuBar(createMenuBar());

//        tool bar
        this.add(createToolBar(), BorderLayout.NORTH);

//        status bar
        JStatusBar statusBar = new JStatusBar();
        localizationProviderBridge.addLocalizationListener(statusBar);
        multipleDocumentModel.addMultipleDocumentListener(statusBar);
        this.add(statusBar, BorderLayout.SOUTH);
    }


    /**
     * Creates {@link JMenuBar} for this frame.
     *
     * @return Menu bar.
     */
    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();

        LJMenu file = new LJMenu("fileMenu");
        LJMenu edit = new LJMenu("editMenu");
        LJMenu info = new LJMenu("infoMenu");
        LJMenu languages = new LJMenu("languageMenu");

        localizationProviderBridge.addLocalizationListener(file);
        localizationProviderBridge.addLocalizationListener(edit);
        localizationProviderBridge.addLocalizationListener(info);
        localizationProviderBridge.addLocalizationListener(languages);

        file.add(actionFactory.getCreateNewDocumentAction());
        file.add(actionFactory.getOpenExistingDocumentAction());
        file.addSeparator();
        file.add(actionFactory.getSaveAction());
        file.add(actionFactory.getSaveAsAction());
        file.addSeparator();
        file.add(actionFactory.getCloseAction());
        file.add(actionFactory.getExitAction());

        edit.add(actionFactory.getCutAction());
        edit.add(actionFactory.getCopyAction());
        edit.add(actionFactory.getPasteAction());

        info.add(actionFactory.getDocumentStatisticsAction());

        languages.add(actionFactory.getLanguageDeAction());
        languages.add(actionFactory.getLanguageEnAction());
        languages.add(actionFactory.getLanguageHrAction());

        mb.add(file);
        mb.add(edit);
        mb.add(info);
        mb.add(languages);
        return mb;
    }

    /**
     * Creates a toolbar and returns it.
     * @return Toolbar.
     */
    private JToolBar createToolBar() {
        JToolBar tb = new JToolBar();

        tb.add(actionFactory.getCreateNewDocumentAction());
        tb.add(actionFactory.getOpenExistingDocumentAction());
        tb.add(actionFactory.getSaveAction());
        tb.add(actionFactory.getSaveAsAction());
        tb.add(actionFactory.getCloseAction());
        tb.addSeparator();
        tb.add(actionFactory.getCutAction());
        tb.add(actionFactory.getCopyAction());
        tb.add(actionFactory.getPasteAction());
        tb.addSeparator();
        tb.add(actionFactory.getDocumentStatisticsAction());
        tb.addSeparator();
        tb.add(actionFactory.getExitAction());

        return tb;
    }

    /**
     * Listener that changes window's title according to the currently edited document.
     */
    private class DocumentListener implements MultipleDocumentListener, SingleDocumentListener {

        /**
         * Updates title according to given document.
         *
         * @param current Currently edited document.
         */
        private void updateTitle(SingleDocumentModel current) {
            String title = "JNotepad++";
            if (current != null) {
                String file = current.getFilePath() == null ? "(unnamed)" : current.getFilePath().toString();
                title = String.format("%s - %s", file, title);
            }
            setTitle(title);
        }

        @Override
        public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
            updateTitle(currentModel);
        }

        @Override
        public void documentAdded(SingleDocumentModel model) {
            model.addSingleDocumentListener(this);
        }

        @Override
        public void documentRemoved(SingleDocumentModel model) {
            model.removeSingleDocumentListener(this);
        }

        @Override
        public void documentModifyStatusUpdated(SingleDocumentModel model) {

        }

        @Override
        public void documentFilePathUpdated(SingleDocumentModel model) {
            updateTitle(model);
        }
    }
}
