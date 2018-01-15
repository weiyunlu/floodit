//temp
import java.util.Scanner;

/**
 * The class <b>FloodIt</b> launches the game
 *
 * @author Weiyun Lu, University of Ottawa
 */

public class FloodIt {


   /**
     * <b>main</b> of the application. Creates the instance of  GameController 
     * and starts the game. If a game size (10 or more) is passed as parameter, it is 
     * used as the board size. Otherwise, a default value of 12 is passed
     * 
     * @param args
     *            command line parameters
     */

     public static void main(String[] args) {

        StudentInfo.display();

        int size;

        //temp
        //Scanner in = new Scanner(System.in);

        if (args.length >= 1 && Integer.parseInt(args[0]) >= 10) {
            size = Integer.parseInt(args[0]);
        } else {
            size = 12;
        }

        GameController controller = new GameController(size);

        // temp ask player for inputs
        //while (!controller.theModel.isFinished()) {
         //   System.out.println("Enter next color (0-6):");
          //  int color = in.nextInt();
           // controller.selectColor(color);
        //}

   }


}