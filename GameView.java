import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out the actual game and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Weiyun, University of Ottawa
 */

public class GameView extends JFrame {

    int gameSize;
    int iconSize;

    protected GameModel theModel;
    protected DotButton[][] board;
    protected DotButton[] buttonSelector;

    protected JButton quitButton, resetButton;
    protected JLabel stepCount;

    protected JButton undoButton, redoButton, settingsButton;

    protected JRadioButton torusButton, planeButton;
    protected JRadioButton orthogonalButton, diagonalButton;

    protected Object[] settingsBox;

    /**
     * Constructor used for initializing the Frame
     * 
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel model, GameController gameController) {

        super("FloodIt - ITI1121 Edition");

        theModel = model;
        gameSize = theModel.getSize();

        if (gameSize <= 25) {
            iconSize = 1;
        } else {
            iconSize = 0;
        }

        setSize(500, 500);

        JPanel boardPanel = new JPanel();

        boardPanel.setBackground(Color.WHITE);
        boardPanel.setLayout(new GridLayout(gameSize, gameSize));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        board = new DotButton[gameSize][gameSize];

        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                int xPos = theModel.model[x][y].getX();
                int yPos = theModel.model[x][y].getY();
                int dotColor = theModel.model[x][y].getColor();
                DotButton dot = new DotButton(xPos, yPos, dotColor, iconSize);
                board[x][y] = dot;
                boardPanel.add(dot);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        stepCount = new JLabel("Select initial dot.");
        resetButton = new JButton("Reset");
        quitButton = new JButton("Quit");

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        controlPanel.add(stepCount);
        controlPanel.add(resetButton);
        controlPanel.add(quitButton);

        add(controlPanel, BorderLayout.SOUTH);

        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        settingsButton = new JButton("Settings");

        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        topPanel.add(undoButton);
        topPanel.add(redoButton);
        topPanel.add(settingsButton);

        add(topPanel, BorderLayout.NORTH);

        torusButton = new JRadioButton("Torus");
        planeButton = new JRadioButton("Plane");
        orthogonalButton = new JRadioButton("Orthogonal");
        diagonalButton = new JRadioButton("Diagonal");

        ButtonGroup group1 = new ButtonGroup();
        ButtonGroup group2 = new ButtonGroup();

        group1.add(torusButton);
        group1.add(planeButton);

        group2.add(orthogonalButton);
        group2.add(diagonalButton);

        JLabel planeQuest = new JLabel("Play on plane or torus?");
        JLabel moveQuest = new JLabel("Diagonal moves?");

        settingsBox = new Object[6];
        settingsBox[0] = planeQuest;
        settingsBox[1] = planeButton;
        settingsBox[2] = torusButton;
        settingsBox[3] = moveQuest;
        settingsBox[4] = orthogonalButton;
        settingsBox[5] = diagonalButton;

        planeButton.setSelected(true);
        orthogonalButton.setSelected(true);

        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                board[x][y].addActionListener(gameController);
            }
        }

        resetButton.addActionListener(gameController);
        quitButton.addActionListener(gameController);
        undoButton.addActionListener(gameController);
        redoButton.addActionListener(gameController);
        settingsButton.addActionListener(gameController);

        planeButton.addActionListener(gameController);
        torusButton.addActionListener(gameController);
        orthogonalButton.addActionListener(gameController);
        diagonalButton.addActionListener(gameController);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * update the status of the board's DotButton instances based on the current game model
     */

    public void update(){

        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                if (theModel.model[x][y].isCaptured()) {
                    board[x][y].setColor(theModel.getCurrentSelectedColor());
                } else {
                    board[x][y].setColor(theModel.model[x][y].getColor());
                }
            }
        }

        if (theModel.diagonalMode) {
            diagonalButton.setSelected(true);
        } else {
            orthogonalButton.setSelected(true);
        }

        if (theModel.torusMode) {
            torusButton.setSelected(true);
        } else {
            planeButton.setSelected(true);
        }

        int numSteps = theModel.getNumberOfSteps();
        String stepsCurrent = new String();

        if (numSteps >= 0) {
            stepsCurrent = "Number of steps: " + Integer.toString(numSteps);
        } else {
            stepsCurrent = "Select initial dot.";
        }
    
        stepCount.setText(stepsCurrent);
    }

}