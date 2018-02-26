package se.oscarbergqvist.gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Oscar Bergqvist on 2018-02-24.
 */

public class View implements ActionListener{

    /* Controller object*/
    private Controller controller;

    /* State matrix*/
    private Matrix currentState;

    /* UI state variables and constants */
    private static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CONTROL_PANEL_HEIGHT = 100;
    private static final int WINDOW_HEIGHT = CONTROL_PANEL_HEIGHT + WORLD_HEIGHT;
    private static final int WINDOW_WIDTH =  WORLD_WIDTH;

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


    View(Controller controller, Matrix currentState){
        this.controller = controller;
        this.currentState = currentState;

        initUI();
        initActionListeners();
    }

    private void initUI() {
        frame = new JFrame("Game Of Life");

        frame.setTitle("Game Of Life");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        containerPanel = new JPanel(new BorderLayout());
        worldPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
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
        widthTextField = new JTextField(5);
        heightTextField = new JTextField(5);

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

        frame.pack();
        frame.setVisible(true);
        worldPanel.repaint();
    }

    private void initActionListeners(){
        stepButton.addActionListener(this);
        autoButton.addActionListener(this);
        newButton.addActionListener(this);
        openFileChooserButton.addActionListener(this);
    }

    private void paintWorld(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color white = new Color(255,255,255);
        Color black = new Color(0,0,0);
        int ROWS = currentState.getRows();
        int COLUMNS = currentState.getColumns();

        int cellWidth = (int) Math.floor(WORLD_WIDTH/ROWS);
        int cellHeight = (int) Math.floor(WORLD_HEIGHT/COLUMNS);

        for(int i = 0; i <= currentState.getColumns(); i++){
            for(int j = 0; j <= currentState.getRows(); j++){
                if(currentState.getValueAt(i, j))
                    g2d.setColor(white);
                else
                    g2d.setColor(black);
                g2d.fillRect(j*cellWidth,i*cellHeight,cellWidth,cellHeight);
            }
        }

        //System.out.println("In paintWorld");
    }

    public void showMessage(Enum TYPE, String message) {
        if(TYPE == Controller.LogType.INFO) {
            JOptionPane.showMessageDialog(frame, message);
        }
        else if (TYPE == Controller.LogType.ERROR) {
            JOptionPane.showMessageDialog(frame,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void updateWorld(Matrix newState){
        this.currentState = newState;
        worldPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == stepButton){
            int step = controller.getStep();
            try{
                step = Integer.parseInt(stepTextField.getText());
                if (step<=0)
                    throw new Exception();
            } catch(Exception ex){
                Controller.log(Controller.LogType.ERROR, "Please enter an positive integer");
            }
            this.controller.setStep(step);
            this.controller.update(step);
        }
        else if (e.getSource() == autoButton){

        }
        else if (e.getSource() == newButton){

        }
        else if (e.getSource() == openFileChooserButton){

        }

    }
}
