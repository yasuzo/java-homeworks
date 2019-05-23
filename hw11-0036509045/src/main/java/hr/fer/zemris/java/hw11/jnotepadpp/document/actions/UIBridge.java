package hr.fer.zemris.java.hw11.jnotepadpp.document.actions;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Bridge between actions and UI. This object is used for communication with user.
 * Used for creating popup options, warnings, fileChoosing etc.
 *
 * @author Jan Capek
 */
public class UIBridge {

    /**
     * Popups will be based on this parent.
     */
    private JFrame parent;


    /**
     * Constructs a new UIBridge with given parent attribute. All popups will be centered in parent.
     *
     * @param parent Parent component of popups.
     */
    public UIBridge(JFrame parent) {
        Objects.requireNonNull(parent);
        this.parent = parent;
    }

    /**
     * @return Parent JFrame.
     */
    public JFrame getParent() {
        return parent;
    }

    /**
     * Creates a file chooser, displays it and returns selected file if option was approved.
     *
     * @param fileChooserTitle Title of a file chooser.
     * @return Path of a selected file or {@code null} if option was not approved.
     * @throws NullPointerException If given title is {@code null}.
     */
    public Path chooseFile(String fileChooserTitle) {
        Objects.requireNonNull(fileChooserTitle);
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(fileChooserTitle);
        if (jfc.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return jfc.getSelectedFile().toPath();
    }

    /**
     * Creates a file chooser, displays it and returns selected path if option was approved.
     *
     * @param fileChooserTitle Title of a file chooser.
     * @return Path of a selected file or {@code null} if option was not approved.
     * @throws NullPointerException If given title is {@code null}.
     */
    public Path chooseDestinationFile(String fileChooserTitle) {
        Objects.requireNonNull(fileChooserTitle);
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(fileChooserTitle);
        if (jfc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return jfc.getSelectedFile().toPath();
    }

    /**
     * Calls {@link JOptionPane#showOptionDialog(Component, Object, String, int, int, Icon, Object[], Object)} with given attributes.
     *
     * @param title       Dialog title.
     * @param message     Message to user.
     * @param optionType  JOptionPane's option type.
     * @param messageType JOptionPane's message type.
     * @param options     Options offered.
     * @return User response (JOptionPaneConstant).
     */
    public int showOptionDialog(String title, String message, int optionType, int messageType, Object[] options) {
        return JOptionPane.showOptionDialog(
                parent,
                message,
                title,
                optionType,
                messageType,
                null,
                null,
                null);
    }

    /**
     * Shows error dialog to user.
     *
     * @param title   Dialog title.
     * @param message Message to show.
     */
    public void showErrorMessage(String title, String message) {
        showOptionDialog(title, message, JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE, null);
    }

    /**
     * Disconnects from parent. This is necessary to stop memory leakage.
     */
    public void disconnectParent() {
        parent = null;
    }
}
