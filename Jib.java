import javax.swing.ImageIcon;
import java.awt.*;

/**
 * Responsible for drawing/creating Jib (the player) in the same location.
 */
public class Jib
{
    /**
     * How the main character looks.
     */
    Image still;

    /**
     * The constructor. intializes the image for jib and scales it down to the
     * right size to draw it.
     */
    public Jib()
    {
        ImageIcon i = new ImageIcon("jib.png");
        Image image = i.getImage(); // transform it
        Image newimg = image.getScaledInstance(500, 500, java.awt.Image.SCALE_SMOOTH); // scale
                                                                                       // it
                                                                                       // the
                                                                                       // smooth
                                                                                       // way
        i = new ImageIcon(newimg);
        still = i.getImage();
    }


    /**
     * Getter method for the jib drawing.
     * 
     * @return the jib drawing
     */
    public Image getImage()
    {
        return still;
    }

}
