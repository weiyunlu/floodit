import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * In the application <b>FloodIt</b>, a <b>DotButton</b> is a specialized color of
 * <b>JButton</b> that represents a dot in the game. It can have one of six colors
 * 
 * The icon images are stored in a subdirectory ``data''. We have 3 sizes, ``normal'',
 * ``medium'' and ``small'', respectively in directory ``N'', ``M'' and ``S''.
 *
 * The images are 
 * ball-0.png => grey icon
 * ball-1.png => orange icon
 * ball-2.png => blue icon
 * ball-3.png => green icon
 * ball-4.png => purple icon
 * ball-5.png => red icon
 *
 *  <a href=
 * "http://developer.apple.com/library/safari/#samplecode/Puzzler/Introduction/Intro.html%23//apple_ref/doc/uid/DTS10004409"
 * >Based on Puzzler by Apple</a>.
 * @author Weiyun Lu, University of Ottawa
 */

public class DotButton extends JButton {

    private int row, column;
    private int color;
    private int iconSize;
    private static final ImageIcon iconsSmall[] = new ImageIcon[6];          // Array to hold 6 possible colors.  Since a button is always same size, we don't need more than this.
    private static final ImageIcon iconsMedium[] = new ImageIcon[6];
    private static final ImageIcon iconsNormal[] = new ImageIcon[6];

    /**
     * Constructor used for initializing a cell of a specified color.
     * 
     * @param row
     *            the row of this Cell
     * @param column
     *            the column of this Cell
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */

    public DotButton(int row, int column, int color, int iconSize) {

        this.row = row;
        this.column = column;
        this.iconSize = iconSize;

        setBackground(Color.white);
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        setBorder(emptyBorder);
        setBorderPainted(false);
        setIcon(getImageIcon(color, iconSize));
    }

 /**
     * Other constructor used for initializing a cell of a specified color.
     * no row or column info available. Uses "-1, -1" for row and column instead
     * 
     * @param color
     *            specifies the color of this cell
     * @param iconSize
     *            specifies the size to use, one of SMALL_SIZE, MEDIUM_SIZE or MEDIUM_SIZE
     */   
    public DotButton(int color, int iconSize) {

        row = -1;
        column = -1;
        this.iconSize = iconSize;

        setBackground(Color.white);
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        setBorder(emptyBorder);
        setBorderPainted(false);
        setIcon(getImageIcon(color, iconSize));
    }
 


    /**
     * Changes the cell color of this cell. The image is updated accordingly.
     * 
     * @param color
     *            the color to set
     */

    public void setColor(int color) {

        this.color = color;
        setIcon(getImageIcon(color, iconSize));
    }

    /**
     * Getter for color
     *
     * @return color
     */
    public int getColor(){

        return color;
    }
 
    /**
     * Getter method for the attribute row.
     * 
     * @return the value of the attribute row
     */

    public int getRow() {

        return row;
    }

    /**
     * Getter method for the attribute column.
     * 
     * @return the value of the attribute column
     */

    public int getColumn() {

        return column;
    }


    /**
     * Method to convert integer size to a string to find our icon.
     * 0 means small, 1 means medium, any other size means "normal"
     * @param the size to input
     * @return string to find the file path
     */

    private String sizeToString(int iconSize) {
        if (iconSize == 0)
            return "S";
        if (iconSize == 1)
            return "M";
        return "N";
    }

    /**
     * Determine the image to use based on the cell type. Uses
     * <b>getResource</b> to locate the image file, either on the file system or
     * the .jar file.
     *
     * Since icons never need to be more than one size, once it exists in the array, it's fine.
     * 
     * @return the image to be displayed by the button
     */

    private ImageIcon getImageIcon(int color, int iconSize) {

        ImageIcon[] icons;

        if (iconSize == 0) {
            icons = iconsSmall;
        } else if (iconSize == 1) {
            icons = iconsMedium;
        } else {
            icons = iconsNormal;
        }

        if (icons[color] == null) {
            String size = sizeToString(iconSize);
            String strColor = Integer.toString(color);
            icons[color] = new ImageIcon(DotButton.class.getResource("/data/" + size +  "/ball-" + strColor + ".png"));
        }

        return icons[color];
    }



}
