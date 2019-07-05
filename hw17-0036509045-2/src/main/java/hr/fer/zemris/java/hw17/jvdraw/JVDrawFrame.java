package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.geoobject.GeoObjectParser;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.models.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geoobject.ui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geoobjectvisitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.geoobjectvisitors.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.ToolSupplier;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.uicomponents.JDrawingCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Main frame of JVDraw application.
 *
 * @author Jan Capek
 */
public class JVDrawFrame extends JFrame {

    private SavableDrawingModel drawingModel;
    private JDrawingCanvas drawingCanvas;
    private JColorArea outlineColorProvider;
    private JColorArea fillColorProvider;
    private ToolSupplier toolSupplier;

    /**
     * Object holding actions used by this.
     */
    private ActionContainer actionContainer = new ActionContainer();

    /**
     * Listener for window close event.
     */
    private WindowListener localWindowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            actionContainer.exitAction.actionPerformed(null);
        }
    };

    /**
     * Constructs a new frame.
     *
     * @throws HeadlessException If {@link GraphicsEnvironment#isHeadless()} returns {@code true}.
     */
    public JVDrawFrame() throws HeadlessException {
        super("JVDraw");
        setSize(1000, 800);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(localWindowListener);
        init();
        initGUI();
    }

    /**
     * Initializes member variables.
     */
    private void init() {
        drawingModel = new SavableDrawingModel();
        outlineColorProvider = new JColorArea(Color.BLACK);
        fillColorProvider = new JColorArea(Color.WHITE);
        toolSupplier = new ToolSupplier();
        drawingCanvas = new JDrawingCanvas(drawingModel, toolSupplier);
        toolSupplier.setTool(new LineTool(outlineColorProvider, fillColorProvider, drawingModel, drawingCanvas));
    }

    /**
     * Initializes GUI components.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        drawingCanvas.setMinimumSize(new Dimension((int) (getWidth() * 0.75), 500));
        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, drawingCanvas, createListPane()), BorderLayout.CENTER);
        add(createToolBar(), BorderLayout.NORTH);
        setJMenuBar(createMenuBar());
    }

    /**
     * Creates a tool bar.
     *
     * @return Tool bar.
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

//        add color areas
        outlineColorProvider.setMaximumSize(new Dimension(30, 30));
        toolBar.add(outlineColorProvider);
        toolBar.addSeparator(new Dimension(2, 1));
        fillColorProvider.setMaximumSize(new Dimension(30, 30));
        toolBar.add(fillColorProvider);

//        create radio buttons
        JRadioButton lineRadioButton = new JRadioButton("Line", true);
        lineRadioButton.addActionListener(e -> {
            toolSupplier.setTool(new LineTool(outlineColorProvider, fillColorProvider, drawingModel, drawingCanvas));
            drawingCanvas.repaint();
        });
        lineRadioButton.setBackground(new Color(0, 0, 0, 0));
        JRadioButton circleRadioButton = new JRadioButton("Circle", false);
        circleRadioButton.addActionListener(e -> {
            toolSupplier.setTool(new CircleTool(outlineColorProvider, fillColorProvider, drawingModel, drawingCanvas));
            drawingCanvas.repaint();
        });
        circleRadioButton.setBackground(new Color(0, 0, 0, 0));
        JRadioButton filledCircleRadioButton = new JRadioButton("Filled circle", false);
        filledCircleRadioButton.addActionListener(e -> {
            toolSupplier.setTool(new FilledCircleTool(outlineColorProvider, fillColorProvider, drawingModel, drawingCanvas));
            drawingCanvas.repaint();
        });
        filledCircleRadioButton.setBackground(new Color(0, 0, 0, 0));

//        add radio buttons to button group
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(lineRadioButton);
        buttonGroup.add(circleRadioButton);
        buttonGroup.add(filledCircleRadioButton);

//        add radio buttons to the tool bar
        toolBar.add(lineRadioButton);
        toolBar.add(circleRadioButton);
        toolBar.add(filledCircleRadioButton);
        return toolBar;
    }

    /**
     * Creates a menu bar.
     *
     * @return Menu bar.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(actionContainer.openAction);
        fileMenu.add(actionContainer.saveAction);
        fileMenu.add(actionContainer.saveAsAction);
        fileMenu.addSeparator();
        fileMenu.add(actionContainer.exportAction);
        fileMenu.addSeparator();
        fileMenu.add(actionContainer.exitAction);
        menuBar.add(fileMenu);

        return menuBar;
    }

    /**
     * Create a scroll pane containing a JList.
     *
     * @return Scroll pane with JList.
     */
    private ScrollPane createListPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setMinimumSize(new Dimension(200, 100));
        JList<GeometricalObject> jList = new JList<>(new DrawingObjectListModel(drawingModel));
        jList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GeometricalObject selected = jList.getSelectedValue();
                if (selected == null) {
                    return;
                }
                int selectedIndex = jList.getSelectedIndex();
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
//                    delete
                    drawingModel.remove(selected);
                } else if (e.getKeyChar() == '-') {
//                    minus
                    if (selectedIndex - 1 < 0) {
                        return;
                    }
                    drawingModel.changeOrder(selected, -1);
                    jList.setSelectedIndex(selectedIndex - 1);
                } else if (e.getKeyChar() == '+') {
//                    plus
                    if (selectedIndex + 1 >= drawingModel.getSize()) {
                        return;
                    }
                    drawingModel.changeOrder(selected, 1);
                    jList.setSelectedIndex(selectedIndex + 1);
                }
            }
        });

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                GeometricalObject selected = jList.getSelectedValue();
                if (selected == null) {
                    return;
                }
                GeometricalObjectEditor editor = selected.createGeometricalObjectEditor();
                int response = JOptionPane.showConfirmDialog(
                        JVDrawFrame.this,
                        editor,
                        selected.toString(),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if (response == JOptionPane.OK_OPTION) {
                    try {
                        editor.checkEditing();
                        editor.acceptEditing();
                    } catch (Exception ex) {
                        JOptionPane.showOptionDialog(
                                JVDrawFrame.this,
                                "Invalid settings!",
                                "Error",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                null,
                                null
                        );
                    }
                }
            }
        });
        scrollPane.add(jList);
        return scrollPane;
    }


    /**
     * Container for all actions of this frame.
     */
    private class ActionContainer {

        /**
         * Save as action.
         */
        private Action saveAsAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileFilter(new FileNameExtensionFilter("*.jvd", "jvd"));
                if (jfc.showSaveDialog(JVDrawFrame.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }

//                destination path
                Path destination = jfc.getSelectedFile().toPath();

//                invalid file format
                if (destination.toString().matches(".*[^/]\\.jvd") == false) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "Invalid file format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                file already exists
                if (Files.exists(destination)) {
                    int response = JOptionPane.showConfirmDialog(
                            JVDrawFrame.this,
                            "File already exists, do you want to overwrite it?",
                            "File already exists",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (response != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

//                save
                try {
                    Files.writeString(destination, drawingModel.toString(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "Drawing could not be saved", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                if drawing does not have a save path yet set it to chosen destination.
                if (drawingModel.getSavePath() == null) {
                    drawingModel.setSavePath(destination);
                    drawingModel.clearModifiedFlag();
                }
                JOptionPane.showMessageDialog(JVDrawFrame.this, "Drawing successfully saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        /**
         * Save action.
         */
        private Action saveAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawingModel.isModified() == false) {
                    return;
                }

                if (drawingModel.getSavePath() == null) {
                    saveAsAction.actionPerformed(e);
                    return;
                }
                try {
                    Files.writeString(drawingModel.getSavePath(), drawingModel.toString(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "Drawing could not be saved", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                drawingModel.clearModifiedFlag();
            }
        };
        /**
         * Exit action.
         */
        private Action exitAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawingModel.isModified()) {
                    if (offerSave() == false) {
                        return;
                    }
                }
                JVDrawFrame.this.dispose();
            }
        };
        /**
         * Open action.
         */
        private Action openAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawingModel.isModified()) {
                    if (offerSave() == false) {
                        return;
                    }
                }
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Open");
                jfc.setFileFilter(new FileNameExtensionFilter("*.jvd", "jvd"));
                if (jfc.showOpenDialog(JVDrawFrame.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                Path file = jfc.getSelectedFile().toPath();

//                invalid format
                if (file.toString().matches(".*[^/]\\.jvd") == false) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "Invalid file format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                read file
                List<String> lines;
                try {
                    lines = Files.readAllLines(file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "File could not be opened!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                parse objects
                List<GeometricalObject> objects;
                try {
                    objects = GeoObjectParser.fromString(lines);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "File is corrupted!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                draw objects
                drawingModel.clear();
                for (GeometricalObject o : objects) {
                    drawingModel.add(o);
                }
                drawingModel.setSavePath(file);
                drawingModel.clearModifiedFlag();
            }
        };
        /**
         * Export action.
         */
        private Action exportAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileFilter(new FileNameExtensionFilter("*.png, *.jpg, *.gif", "png", "jpg", "gif"));
                if (jfc.showSaveDialog(JVDrawFrame.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }

//                destination path
                Path destination = jfc.getSelectedFile().toPath();
                if (destination.toString().matches(".*[^/]\\.(?:png|jpg|gif)") == false) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "Invalid export extension!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                export
                try {
                    export(destination, destination.toString().replaceFirst(".*\\.", ""));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(JVDrawFrame.this, "Drawing could not be exported!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

//                show confirmation
                JOptionPane.showMessageDialog(JVDrawFrame.this, "Drawing successfully exported!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            /**
             * Exports drawing as image.
             *
             * @param destination Destination path.
             * @param extension Extension.
             * @throws IOException If data could not be written to disk.
             * @throws NullPointerException If any of the parameters is {@code null}.
             */
            private void export(Path destination, String extension) throws IOException {
//                calculate bounding box
                GeometricalObjectBBCalculator bbCalculator = new GeometricalObjectBBCalculator();
                for (GeometricalObject o : drawingModel) {
                    o.accept(bbCalculator);
                }
                Rectangle box = bbCalculator.getBoundingBox();

//                create image
                BufferedImage image = new BufferedImage(
                        box.width == 0 ? 20 : box.width,
                        box.height == 0 ? 20 : box.height,
                        BufferedImage.TYPE_3BYTE_BGR
                );

//                draw on image
                Graphics2D g = image.createGraphics();
                g.translate(-box.x, -box.y);
                GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
                for (GeometricalObject o : drawingModel) {
                    o.accept(painter);
                }
                g.dispose();

//                write image to disk
                ImageIO.write(image, extension, destination.toFile());
            }
        };

        /**
         * Constructs a new action container.
         */
        public ActionContainer() {
            exitAction.putValue(Action.NAME, "Exit");
            exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));

            openAction.putValue(Action.NAME, "Open");
            openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));

            saveAction.putValue(Action.NAME, "Save");
            saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));

            saveAsAction.putValue(Action.NAME, "Save As");
            saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));

            exportAction.putValue(Action.NAME, "Export");
        }

        /**
         * Offers a user to save current drawing model.
         *
         * @return {@code true} if action should continue, {@code false} otherwise.
         */
        private boolean offerSave() {
            int result = JOptionPane.showOptionDialog(
                    JVDrawFrame.this,
                    "Current drawing is not yet saved, would you like to save it?",
                    "Unsaved drawing",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    null,
                    null
            );

            if (result == JOptionPane.YES_OPTION) {
                saveAction.actionPerformed(null);
                return true;
            }
            if (result == JOptionPane.NO_OPTION) {
                return true;
            }
            return false;
        }
    }
}
