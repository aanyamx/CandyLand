import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The board is the basis of the whole GUI and will depict the side scroll as
 * well as the background. Its main functionality is that it keeps the
 * background scrolling for 10 seconds and pops up the additional GUIs (when
 * asking for the answer to a question/the other sprites) when needed.
 */
public class Board
    extends JPanel
    implements ActionListener
{
    /**
     * the background image
     */
    BufferedImage img;
    /**
     * timer to add delay when user presses key
     */
    Timer         time;
    /**
     * main character, draws at stationary position
     */
    Jib           jib;
    /**
     * backend, handles users reponses
     */
    Player        p;
    /**
     * the location of the board
     */
    int           dx, y, nx2;

    /**
     * Initializes the main character, adds key listener, makes focusable,
     * processes image, starts timer, sets dimensions, sets background, sets
     * layout, sets position of board, initializes player, and draws panels.
     */
    Board()
    {
        try
        {
            jib = new Jib();
            addKeyListener(new AL());
            setFocusable(true);
            img = ImageIO.read(new File("background.png"));
            time = new Timer(3, this);
            time.start();
            setPreferredSize(new Dimension(1600, 1200));
            setBackground(Color.WHITE);
            setLayout(null);

            nx2 = 960;
            y = 235;
            p = new Player();

            p.QAPanel.setBounds(0, 0, 1600, 60);
            add(p.QAPanel);

            p.dialoguePanel.setBounds(0, 750, 1600, 60);
            add(p.dialoguePanel);

        }
        catch (IOException e)
        {

            // Print the exception along with line number
            // using printStackTrace() method
            e.printStackTrace();
        }
    }


    /**
     * If answer is not submitted, don't move. Otherwise, add dx to position of
     * the board, moving it downwards. If at landmark(1260), set dx to 0 and
     * make the user stop moving and generate the question.
     */
    public void move()
    {
        if ((p != null) && !p.answerSubmitted)
            return;

        nx2 = nx2 + dx;

        if (nx2 % 1260 == 0 && nx2 != 0)
        {
            dx = 0;
            p.answerSubmitted = false;
            p.generateQA();
            dx = 1;
            nx2 = nx2 + dx;
        }
    }


    /**
     * Called everytime JPanel is repainted. Moves the board and repaints it.
     */
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();

    }


    /**
     * Getter for the backend.
     * 
     * @return p, the instance of the backend
     */
    public Player getPlayer()
    {
        return p;
    }


    /**
     * Draws the background and the main character.
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        p.setVisible(false);
        if (p != null)
            p.clearQA();
        g2d.drawImage(img, 960 - nx2, 0, null);
        p.setVisible(true);
        g2d.drawImage(jib.getImage(), -100, y, null);
    }

    /**
     * Handles when the user presses a key. makes the user move forward if the
     * right arrow key is pressed.
     */
    private class AL
        extends KeyAdapter
    {
        /**
         * Decides what is supposed to happen if a key is pressed based on
         * keystroke.
         * 
         * @param e
         *            if the user presses a key
         */
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT)
            {
                dx = 0;
            }

            if (key == KeyEvent.VK_RIGHT)
            {
                dx = 1;
            }
        }

    }

}
