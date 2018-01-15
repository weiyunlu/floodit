import java.util.Random;
import java.io.*;

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the following information:
 * - the state of all the ``dots'' on the board (color, captured or not)
 * - the size of the board
 * - the number of steps since the last reset
 * - the current color of selection
 *
 * The model provides all of this informations to the other classes through 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Weiyun Lu, University of Ottawa
 */

public class GameModel implements Cloneable, Serializable {


    /**
     * predefined values to capture the color of a DotInfo
     */

    public static final int COLOR_0           = 0;
    public static final int COLOR_1           = 1;
    public static final int COLOR_2           = 2;
    public static final int COLOR_3           = 3;
    public static final int COLOR_4           = 4;
    public static final int COLOR_5           = 5;
    public static final int NUMBER_OF_COLORS  = 6;


    private int gameSize;
    private int currentColor;
    private int steps;
    protected int capturedDots;

    protected DotInfo[][] model;

    protected boolean torusMode;
    protected boolean diagonalMode;

    Random random = new Random();

    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {

        gameSize = size;
        model = new DotInfo[size][size];
        torusMode = false;
        diagonalMode = false;
        reset();

    }


    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){

            
        // We initialize to -1 because before the player chooses his first color, we must "autoplay" the zeroth turn by pre-capturing
        // adjacent dots that already match the top-left dot (if any), which then puts steps to 0.
        steps = -1;
        capturedDots = 0;

        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                model[x][y] = new DotInfo(x, y, random.nextInt(6));
            }
        }

    }


    /**
     * Getter method for the size of the game
     * 
     * @return the value of the attribute sizeOfGame
     */   
    public int getSize(){

        return gameSize;
    }

    /**
     * returns the current color  of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public int getColor(int i, int j){
        if (model[i][j].isCaptured()) {
            return currentColor;
        } else {
            return model[i][j].getColor();
        }
    }

    /**
     * returns true if the dot is captured, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isCaptured(int i, int j){

        return model[i][j].isCaptured();
    }

    /**
     * Sets the status of the dot at coordinate (i,j) to captured
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void capture(int i, int j){
        capturedDots++;
        model[i][j].setCaptured(true);
    }


    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){

        return steps;
    }

    /**
     * Setter method for currentSelectedColor
     * 
     * @param val
     *            the new value for currentSelectedColor
    */   
    public void setCurrentSelectedColor(int val) {

        currentColor = val;
    }

    /**
     * Getter method for currentSelectedColor
     * 
     * @return currentSelectedColor
     */   
    public int getCurrentSelectedColor() {

        return currentColor;
    }


    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
      * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {

        return model[i][j];
    }


   /**
     * The method <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the player selected a new color.
     */
    public void step(){

        steps ++;
    }
 
   /**
     * The method <b>isFinished</b> returns true iff the game is finished, that
     * is, all the dots are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){

        return capturedDots == gameSize * gameSize;
        // for (int x = 0; x < gameSize; x++) {
        //    for (int y = 0; y < gameSize; y++) {
        //        if (!model[x][y].isCaptured())
        //            return false;
        //    }
        //}
        //return true;
    }

    /**
     * Clones the game model
     *
     * @return modelCopy
     */

    public GameModel clone() throws CloneNotSupportedException {

        GameModel modelCopy = (GameModel) super.clone();
        modelCopy.model = new DotInfo[gameSize][gameSize];

        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                modelCopy.model[x][y] = model[x][y].clone();
            }
        }

        return modelCopy;
    }

   /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){

        String output = new String();

        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {
                if (model[x][y].isCaptured()) {
                    output += currentColor + " ";
                } else {
                    output += Integer.toString(model[x][y].getColor()) + " ";
                }
            }
            output += "\n";
        }

        output += "Steps: " + Integer.toString(steps);

        return output;
    }
}
