package se.oscarbergqvist.gameoflife;

import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.IllegalFormatConversionException;

/**
 * Created by Oscar Bergqvist on 2018-02-24.
 */

public class View implements ActionListener
{

    /* Controller object*/
    private Controller controller;

    /* State matrix*/
    private Matrix currentState;

    /* Timer for auto play */
    private Timer timer;
    private boolean autoPlayEnabled;

    /* File chooser */
    JFileChooser fileChooser = new JFileChooser();

    /* UI state variables and constants */
    private static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CONTROL_PANEL_HEIGHT = 100;
    private static final int WINDOW_HEIGHT = CONTROL_PANEL_HEIGHT + WORLD_HEIGHT;
    private static final int WINDOW_WIDTH = WORLD_WIDTH;

    /* Frame */
    private JFrame frame;

    /* Panels */
    private JPanel containerPanel;
    private JPanel worldPanel;
    private JPanel controlPanel;
    private JPanel controlLeftPanel;
    private JPanel controlRightPanel;
    private JPanel stepControlPanel;
    private JPanel sizeControlPanel;
    private JPanel fileControlPanel;

    /* Buttons */
    private JButton stepButton;
    private JButton autoButton;
    private JButton newButton;
    private JButton openFileChooserButton;

    /* TextFields */
    private JTextField stepTextField;
    private JTextField widthTextField;
    private JTextField heightTextField;

    View(Controller controller, Matrix currentState)
    {
        Controller.log(Controller.LogType.LOG, "in View constructor");
        this.controller = controller;
        this.currentState = currentState;
        this.timer = new Timer(500, e -> controller.update(controller.getStep()));
        timer.setInitialDelay(500);
        this.autoPlayEnabled = false;
        initUI();
        initActionListeners();
    }

    private void initUI()
    {
        Controller.log(Controller.LogType.LOG, "initializing UI");
        frame = new JFrame("Game Of Life");

        frame.setTitle("Game Of Life");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        containerPanel = new JPanel(new BorderLayout());
        worldPanel = new JPanel()
        {
            @Override
            public void paintComponent(Graphics g)
            {
                //System.out.println("I paintComponent()");
                super.paintComponent(g);
                paintWorld(g);
            }
        };
        controlPanel = new JPanel(new BorderLayout());
        controlLeftPanel = new JPanel(new BorderLayout());
        controlRightPanel = new JPanel(new BorderLayout());
        stepControlPanel = new JPanel();
        sizeControlPanel = new JPanel();
        fileControlPanel = new JPanel();

        stepButton = new JButton("Step");
        autoButton = new JButton("Auto Play");
        newButton = new JButton("New");
        openFileChooserButton = new JButton("Get state from file");

        stepTextField = new JTextField(5);
        stepTextField.setText("1");
        widthTextField = new JTextField(5);
        widthTextField.setText("20");
        heightTextField = new JTextField(5);
        heightTextField.setText("20");
        frame.setContentPane(containerPanel);
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

        containerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        worldPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WORLD_HEIGHT));
        //worldPanel.setBackground(new Color(0,0,0));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        fileChooser.setFileFilter(filter);


        frame.pack();
        frame.setVisible(true);
        worldPanel.repaint();
    }

    private void initActionListeners()
    {
        Controller.log(Controller.LogType.LOG, "initializing ActionListeners");
        worldPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int cellWidth = (int) Math.floor(WORLD_WIDTH / currentState.getColumns());
                int cellHeight = (int) Math.floor(WORLD_HEIGHT / currentState.getRows());
                int i = (int) Math.floor(e.getX() / cellWidth);
                int j = (int) Math.floor(e.getY() / cellHeight);
                controller.change(i, j);
            }
        });
        stepButton.addActionListener(this);
        autoButton.addActionListener(this);
        newButton.addActionListener(this);
        openFileChooserButton.addActionListener(this);
    }

    private void paintWorld(Graphics g)
    {
        Controller.log(Controller.LogType.LOG, "in View.paintWorld(Graphics g)");
        //System.out.println("I paintWorld()");
        Graphics2D g2d = (Graphics2D) g;
        Color cellColor = new Color(255, 255, 255);
        Color bgColor = new Color(0, 0, 0);
        Color gridColor = new Color(30, 30, 30);

        int ROWS = currentState.getRows();
        int COLUMNS = currentState.getColumns();

        int cellWidth = (int) Math.floor(WORLD_WIDTH / COLUMNS);
        int cellHeight = (int) Math.floor(WORLD_HEIGHT / ROWS);

        for (int i = 0; i < COLUMNS; i++)
        {
            for (int j = 0; j < ROWS; j++)
            {
                if (currentState.getValueAt(i, j))
                    g2d.setColor(cellColor);
                else
                    g2d.setColor(bgColor);
                g2d.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
            }
        }

        g2d.setColor(gridColor);

        for (int j = 0; j < ROWS; j++)
            g2d.drawLine(0, j * cellHeight, cellWidth * COLUMNS, j * cellHeight);

        for (int i = 0; i < COLUMNS; i++)
            g2d.drawLine(i * cellWidth, 0, i * cellWidth, cellHeight * ROWS);

        //System.out.println("In paintWorld");
    }

    void updateWorld(Matrix newState)
    {
        Controller.log(Controller.LogType.LOG, "in View.updateWorld(Matrix newState)");
        //System.out.println("I updateWorld()");
        this.currentState = newState;
        worldPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Controller.log(Controller.LogType.LOG, "in actionPerformed");
        if (e.getSource() == stepButton)
        {
            try
            {
                int step = Integer.parseInt(stepTextField.getText());
                if (step < 1)
                {
                    throw new NumberFormatException();
                }
                controller.setStep(step);
                controller.update(step);
            } catch (NumberFormatException ex)
            {
                Controller.log(Controller.LogType.ERROR, "Please enter an positive integer");
            }
        } else if (e.getSource() == autoButton)
        {
            if (autoPlayEnabled)
            {
                autoPlayEnabled = false;
                timer.stop();
                stepTextField.setEnabled(true);
                stepButton.setEnabled(true);
            } else
            {
                //COMMENT!
                try
                {
                    int step = Integer.parseInt(stepTextField.getText());
                    if (step < 1)
                    {
                        throw new NumberFormatException();
                    }
                    autoPlayEnabled = true;
                    stepButton.setEnabled(false);
                    stepTextField.setEnabled(false);
                    timer.start();
                } catch (NumberFormatException ex)
                {
                    Controller.log(Controller.LogType.ERROR, "Please enter an positive integer number of steps");
                }
            }
        } else if (e.getSource() == newButton)
        {
            try
            {
                int rows = Integer.parseInt(widthTextField.getText());
                int columns = Integer.parseInt(heightTextField.getText());
                if (rows < 1 || columns < 1)
                {
                    throw new Exception();
                }
                if (autoPlayEnabled)
                {
                    autoPlayEnabled = false;
                    timer.stop();
                    stepTextField.setEnabled(true);
                    stepButton.setEnabled(true);
                }
                controller.newInstance(rows, columns);
            } catch (Exception ex)
            {
                Controller.log(Controller.LogType.ERROR, "please enter positive " +
                        "integer values of the number of rows and columns");
            }
        } else if (e.getSource() == openFileChooserButton)
        {
            int returnValue = fileChooser.showOpenDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION)
            {
                File file = fileChooser.getSelectedFile();
                controller.readFile(file);
            } else
            {
                Controller.log(Controller.LogType.INFO, "File selection aborted");
            }
        }
    }

}
