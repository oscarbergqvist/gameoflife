/*
 * Author : Oscar Bergqvist
 * Date: 2018-02-24
 */

package se.oscarbergqvist.gameoflife;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


/**
 * View creates the UI, using Swing graphics. Implements ActionListener.
 */
public class View implements ActionListener {

    /* Controller & Matrix*/
    private Controller controller;
    private Matrix currentState;

    /* UI functionality */
    private Timer timer;
    private boolean autoPlayEnabled;
    private JFileChooser fileChooser = new JFileChooser();

    /* UI state variables and constants */
    private static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CONTROL_PANEL_HEIGHT = 100;
    private static final int WINDOW_HEIGHT = CONTROL_PANEL_HEIGHT + WORLD_HEIGHT;
    private static final int WINDOW_WIDTH = WORLD_WIDTH;

    /* UI: Frame */
    private final JFrame frame = new JFrame("Game of Life");

    /* UI: Panels */
    private final JPanel containerPanel = new JPanel(new BorderLayout());
    private final JPanel worldPanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            //System.out.println("I paintComponent()");
            super.paintComponent(g);
            paintWorld(g);
        }
    };
    private final JPanel controlPanel = new JPanel(new BorderLayout());
    private final JPanel controlLeftPanel = new JPanel(new BorderLayout());
    private final JPanel controlRightPanel = new JPanel(new BorderLayout());
    private final JPanel stepControlPanel = new JPanel();
    private final JPanel sizeControlPanel = new JPanel();
    private final JPanel fileControlPanel = new JPanel();

    /* UI: Buttons */
    private final JButton stepButton = new JButton("Next generation");
    private final JButton autoButton = new JButton("Play");
    private final JButton newButton = new JButton("New world");
    private final JButton openFileChooserButton = new JButton("World from file");

    /* UI: TextFields */
    private final JTextField stepTextField = new JTextField(5);
    private final JTextField widthTextField = new JTextField(5);
    private final JTextField heightTextField = new JTextField(5);

    /**
     * Constructor
     * <p>
     * Instantiates fields and calls {@link #initUI()} and {@link #initActionListeners()}.
     *
     * @param controller   Controller object
     * @param currentState Matrix with current state.
     */
    View(Controller controller, Matrix currentState) {
        this.controller = controller;
        this.currentState = currentState;

        // Setup default values
        timer = new Timer(500, e -> controller.update(controller.getStep()));
        timer.setInitialDelay(500);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        autoPlayEnabled = false;

        // Init UI and Action Listeners
        initUI();
        initActionListeners();

        Controller.log(Controller.LogType.LOG, "View created");
    }

    /**
     * The UI is created in this method.
     */
    private void initUI() {
        // Setup frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(containerPanel);

        // Add components
        containerPanel.add(worldPanel, BorderLayout.PAGE_START);
        containerPanel.add(controlPanel, BorderLayout.PAGE_END);
        controlPanel.add(controlLeftPanel, BorderLayout.LINE_START);
        controlPanel.add(controlRightPanel, BorderLayout.LINE_END);
        controlLeftPanel.add(stepControlPanel, BorderLayout.PAGE_START);
        controlLeftPanel.add(sizeControlPanel, BorderLayout.PAGE_END);
        controlRightPanel.add(fileControlPanel, BorderLayout.CENTER);
        stepControlPanel.add(stepTextField);
        stepControlPanel.add(stepButton);
        stepControlPanel.add(autoButton);
        sizeControlPanel.add(widthTextField);
        sizeControlPanel.add(heightTextField);
        sizeControlPanel.add(newButton);
        fileControlPanel.add(openFileChooserButton);

        // Set default texts and sizes
        stepTextField.setText("1");
        widthTextField.setText("30");
        heightTextField.setText("30");
        containerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        worldPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WORLD_HEIGHT));

        // Finish UI-setup
        frame.pack();
        frame.setVisible(true);
        worldPanel.repaint();
        Controller.log(Controller.LogType.LOG, "UI initialization complete");
    }

    /**
     * Adds Action Listeners to JComponents.
     */
    private void initActionListeners() {
        worldPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellWidth = (int) Math.floor(WORLD_WIDTH / currentState.getColumns());
                int cellHeight = (int) Math.floor(WORLD_HEIGHT / currentState.getRows());
                int i = (int) Math.floor(e.getX() / cellWidth);
                int j = (int) Math.floor(e.getY() / cellHeight);
                controller.toggle(i, j);
            }
        });
        stepButton.addActionListener(this);
        autoButton.addActionListener(this);
        newButton.addActionListener(this);
        openFileChooserButton.addActionListener(this);

        Controller.log(Controller.LogType.LOG, "Action listeners initialized");
    }

    /**
     * This method updates the current state using newState Matrix. Calls repaint().
     *
     * @param newState Matrix with new state.
     */
    void updateWorld(Matrix newState) {
        Controller.log(Controller.LogType.LOG, "View.updateWorld(Matrix newState) called");
        this.currentState = newState;
        worldPanel.repaint();
    }

    /**
     * Is called by paintComponent() of java.swing. Redraws world according to the current state matrix.
     *
     * @param g Graphics object to update.
     */
    private void paintWorld(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color cellColor = new Color(255, 255, 255);
        Color bgColor = new Color(0, 0, 0);
        Color gridColor = new Color(30, 30, 30);

        int ROWS = currentState.getRows();
        int COLUMNS = currentState.getColumns();
        int cellWidth = (int) Math.floor(WORLD_WIDTH / COLUMNS);
        int cellHeight = (int) Math.floor(WORLD_HEIGHT / ROWS);

        // Paint living and empty cells
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (currentState.getValueAt(i, j))
                    g2d.setColor(cellColor);
                else
                    g2d.setColor(bgColor);
                g2d.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
            }
        }

        // Paint grid
        g2d.setColor(gridColor);
        for (int j = 0; j < ROWS; j++)
            g2d.drawLine(0, j * cellHeight, cellWidth * COLUMNS, j * cellHeight);

        for (int i = 0; i < COLUMNS; i++)
            g2d.drawLine(i * cellWidth, 0, i * cellWidth, cellHeight * ROWS);

        Controller.log(Controller.LogType.LOG, "World repainted");
    }

    /**
     * Overrides actionPreformed in java.awt.event
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Controller.log(Controller.LogType.LOG, "View.ActionPerformed(ActionEvent e) called");

        if (e.getSource() == stepButton) {
            try {
                int step = Integer.parseInt(stepTextField.getText());
                if (step < 1)
                    throw new NumberFormatException();
                controller.setStep(step);
                controller.update(step);
            } catch (NumberFormatException ex) {
                Controller.log(Controller.LogType.ERROR, "Please enter an positive integer");
            }
        } else if (e.getSource() == autoButton) {
            if (autoPlayEnabled) {
                autoPlayEnabled = false;
                timer.stop();
                stepTextField.setEnabled(true);
                stepButton.setEnabled(true);
                autoButton.setText("Play");
            } else {
                try {
                    int step = Integer.parseInt(stepTextField.getText());
                    if (step < 1)
                        throw new NumberFormatException();
                    autoPlayEnabled = true;
                    stepButton.setEnabled(false);
                    stepTextField.setEnabled(false);
                    timer.start();
                    autoButton.setText("Stop");
                } catch (NumberFormatException ex) {
                    Controller.log(Controller.LogType.ERROR, "Please enter an positive integer number of steps");
                }
            }
        } else if (e.getSource() == newButton) {
            try {
                int rows = Integer.parseInt(widthTextField.getText());
                int columns = Integer.parseInt(heightTextField.getText());
                if (rows < 1 || columns < 1)
                    throw new Exception();
                if (autoPlayEnabled) {
                    autoPlayEnabled = false;
                    timer.stop();
                    stepTextField.setEnabled(true);
                    stepButton.setEnabled(true);
                    autoButton.setText("Play");
                }
                controller.newInstance(rows, columns);
            } catch (Exception ex) {
                Controller.log(Controller.LogType.ERROR, "please enter positive integer values of the number of rows " +
                        "" + "" + "and columns");
            }
        } else if (e.getSource() == openFileChooserButton) {
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                controller.readFile(file);
            } else
                Controller.log(Controller.LogType.INFO, "File selection aborted");
        }
    }

}
