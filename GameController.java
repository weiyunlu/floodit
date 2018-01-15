import java.awt.event.*;
import javax.swing.JOptionPane;
import java.io.*;

/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computes the next step of the game, and updates model and view.
 *
 * @author Weiyun Lu, University of Ottawa
 */


public class GameController implements ActionListener {

    protected GameModel theModel;
    protected GameView theView;
    protected int gameSize;

    protected GenericLinkedStack<GameModel> previousStates;
    protected GenericLinkedStack<GameModel> nextStates;

    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {

        gameSize = size;

        File savedGame = new File("savedGame.ser");

        if (savedGame.exists()) {
            try {
                FileInputStream f_in = new FileInputStream("savedGame.ser");
                ObjectInputStream o_in = new ObjectInputStream(f_in);
                Object saved = o_in.readObject();
                f_in.close();
                GameModel savedModel = (GameModel) saved;
                if (savedModel.getSize() == gameSize) {
                    System.out.println("Restoring old game.");
                    theModel = savedModel;
                    savedGame.delete();
                } else {
                    theModel = new GameModel(gameSize);
                }
            }
            catch (Exception e) {
                System.out.println("Error restoring previously saved game... starting a new game instead.");
                theModel = new GameModel(gameSize);
            }
        } else {
            theModel = new GameModel(gameSize);
        }

        theView = new GameView(theModel, this);
        theView.update();

        previousStates = new GenericLinkedStack<GameModel>();
        nextStates = new GenericLinkedStack<GameModel>();
    }

    /**
     * resets the game
     */
    public void reset(){

        clearPreviousStates();
        clearNextStates();
        theModel.reset();
        theView.update();
    }

    /**
     * saves the current state of the game before an action, so that it can be undone
     */
    public void saveState() {
        try{
            GameModel stateCopy = theModel.clone();
            previousStates.push(stateCopy);
            if (!theView.undoButton.isEnabled()) {
                theView.undoButton.setEnabled(true);
            }
        }
        catch (CloneNotSupportedException e) {
            System.out.println("Error: Failed to clone game state.");
        }
    }

    /**
     * clears the previous states that can be undone, when the game is reset
     */
    public void clearPreviousStates() {
        while (!previousStates.isEmpty()) {
            previousStates.pop();
        }
        theView.undoButton.setEnabled(false);
    }

    /**
     * clears the states that can be redone whenever the user makes a new action
     */
    public void clearNextStates() {
        while (!nextStates.isEmpty()) {
            nextStates.pop();
        }
        theView.redoButton.setEnabled(false);
    }

    /**
     * restores the game to the most recent previous state, and puts this state into the stack of redoable ones
     */
    public void restoreState() {
        GameModel previousState = previousStates.pop();
        nextStates.push(theModel);
        theModel = previousState;
        theView.theModel = previousState;
        theView.update();
        if (!theView.redoButton.isEnabled()) {
            theView.redoButton.setEnabled(true);
        }
        if (previousStates.isEmpty()) {
            theView.undoButton.setEnabled(false);
        }
    }

    /**
     * redoes an undone move, when available
     */
    public void redoState() {
        saveState();
        GameModel nextState = nextStates.pop();
        theModel = nextState;
        theView.theModel = nextState;
        theView.update();
        if (nextStates.isEmpty()) {
            theView.redoButton.setEnabled(false);
        }
    }

    /**
     * serializes the game state for restoring
     */
    public void saveGame() {
        try {  
            FileOutputStream f_out = new FileOutputStream("savedGame.ser");
            ObjectOutputStream o_out = new ObjectOutputStream(f_out);
            o_out.writeObject(theModel);
            o_out.close();
            System.out.println("Game saved.");
        } catch (IOException e) {
            System.out.println("Error saving the game.");
        }
    }

    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e
     *            the ActionEvent
     */

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == theView.resetButton) {
            reset();
        } else if (e.getSource() == theView.quitButton) {
            saveGame();
            System.exit(0);
        } else if (e.getSource() == theView.undoButton) {
            restoreState();
        } else if (e.getSource() == theView.redoButton) {
            redoState();
        } else if (e.getSource() == theView.settingsButton) {
            JOptionPane.showConfirmDialog(null, theView.settingsBox, "Game Settings", JOptionPane.DEFAULT_OPTION);
        } else if (e.getSource() == theView.planeButton) {
            if (theModel.torusMode){
                saveState();
                clearNextStates();
                theModel.torusMode = false;
            }
        } else if (e.getSource() == theView.torusButton) {
            if (!theModel.torusMode) {
                saveState();
                clearNextStates();
                theModel.torusMode = true;
            }
        } else if (e.getSource() == theView.orthogonalButton) {
            if (theModel.diagonalMode) {
                saveState();
                clearNextStates();
                theModel.diagonalMode = false;
            }
        } else if (e.getSource() == theView.diagonalButton) {
            if (!theModel.diagonalMode) {
                saveState();
                clearNextStates();
                theModel.diagonalMode = true;
            }
        } else {
            for (int x = 0; x < gameSize; x++) {
                for (int y = 0; y < gameSize; y++) {
                    if (e.getSource() == theView.board[x][y]) {
                        if (theModel.getNumberOfSteps() < 0) {
                            saveState();
                            clearNextStates();
                            theModel.capture(x, y);
                            selectColor(theModel.model[x][y].getColor());
                        }
                        if (!theModel.model[x][y].isCaptured()) {
                            saveState();
                            clearNextStates();
                            selectColor(theModel.model[x][y].getColor());
                        }
                        break;
                    }
                }
            }
        }

    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives two options: start a new game, or exit
     * @param color
     *            the newly selected color
     */
    private void selectColor(int color){

        if (theModel.getCurrentSelectedColor() == color)
            return;

        theModel.setCurrentSelectedColor(color);

        int size = theModel.getSize();

        GenericLinkedStack<DotInfo> theStack = new GenericLinkedStack<DotInfo>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (theModel.model[x][y].isCaptured())
                    theStack.push(theModel.model[x][y]);
            }
        }

        while (!theStack.isEmpty()) {

            DotInfo theDot = theStack.pop();
            int x = theDot.getX();
            int y = theDot.getY();

            int max = size - 1;

            if (x > 0) {
                checkCapture(x-1, y, color, theStack);
            } else if (theModel.torusMode) {
                checkCapture(max, y, color, theStack);
            }

            if (x < max) {
                checkCapture(x+1, y, color, theStack);
            } else if (theModel.torusMode) {
                checkCapture(0, y, color, theStack);
            }

            if (y > 0) {
                checkCapture(x, y-1, color, theStack);
            } else if (theModel.torusMode) {
                checkCapture(x, max, color, theStack);
            }

            if (y < max) {
                checkCapture(x, y+1, color, theStack);
            } else if (theModel.torusMode) {
                checkCapture(x, 0, color, theStack);
            }

            if (theModel.diagonalMode) {

                // Note: Java's modulus operator returns negative numbers on negative input,
                // hence we must first add the modulus if the input may be negative.

                if (x > 0 && y > 0) {
                    checkCapture(x-1, y-1, color, theStack);
                } else if (theModel.torusMode) {
                    checkCapture((x-1+size) % size, (y-1+size) % size, color, theStack);
                }

                if (x < max && y < max) {
                    checkCapture(x+1, y+1, color, theStack);
                } else if (theModel.torusMode) {
                    checkCapture((x+1) % size, (y+1) % size, color, theStack);
                }

                if (x > 0 && y < max) {
                    checkCapture(x-1, y+1, color, theStack);
                } else if (theModel.torusMode) {
                    checkCapture((x-1+size) % size, (y+1) % size, color, theStack);
                }

                if (x < max && y > 0) {
                    checkCapture(x+1, y-1, color, theStack);
                } else if (theModel.torusMode) {
                    checkCapture((x+1) % size, (y-1+size) % size, color, theStack);
                }
            }
        }

        theModel.step();
        theView.update();

        if (theModel.isFinished()) {

            if (JOptionPane.showOptionDialog(null, 
                "You won in " + Integer.toString(theModel.getNumberOfSteps()) + " steps.  Play again?", 
                "Victory!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Quit", "Play again"}, "default") == JOptionPane.OK_OPTION) 
            {
                System.exit(0);
            } else {
                reset();
            }
        }

    }

    /**
     * Check whether or not we need to capture a dot; if it's the right color, put it on the stack
     * 
     * @param dot the dot we wish to check.
     * @param color the color that we are capturing.
     * @param stack the stack on which to put captured dots.
     */

    private void checkCapture(int x, int y, int color, GenericLinkedStack<DotInfo> stack) {
        DotInfo theDot = theModel.model[x][y];
        if (theDot.getColor() == color && !theDot.isCaptured()) {
            theModel.capture(x, y);
            stack.push(theDot);
        }
    }

}
