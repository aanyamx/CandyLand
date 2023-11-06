import java.awt.event.*;
import javax.swing.*;

/**
 * The class WelcomeScreen introduces the user to the game by asking them to
 * input their name and the theme they desire to be asked questions about. They
 * then may start the game from here!
 */
public class WelcomeScreen
    extends JFrame
    implements ActionListener
{
    /**
     * field where player enters name
     */
    private JTextField nameEnter;
    /**
     * field where player enters desired theme
     */
    private JTextField themeEnter;
    /**
     * button to start game
     */
    private JButton    playButton;
    /**
     * game object
     */
    private JFrame     myGame;
    /**
     * this screen
     */
    private JFrame     inputScreen;

    /**
     * Constructs the welcome screen and is essentially responsible for all the
     * action and creation inside such an object. Creates two textfields for
     * name and theme, both of which are set up within the Player class once
     * entered. Has a Start Game button that closes this window and begins the
     * game.
     * 
     * @param game
     *            the game so that it can be made visible.
     */
    public WelcomeScreen(JFrame game)
    {
        myGame = game;
        inputScreen = new JFrame("welcome screen");
        inputScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inputScreen.setSize(300, 200);

        nameEnter = new JTextField("name");
        themeEnter = new JTextField("theme");
        nameEnter.setEditable(true);
        themeEnter.setEditable(true);
        nameEnter.setBounds(20, 30, 200, 30);
        themeEnter.setBounds(20, 80, 200, 30);
        nameEnter.setVisible(true);
        themeEnter.setVisible(true);
        nameEnter.addActionListener(this);
        themeEnter.addActionListener(this);
        inputScreen.add(nameEnter);
        inputScreen.add(themeEnter);
        inputScreen.setLayout(null);
        playButton = new JButton("start game");
        playButton.setBounds(70, 130, 100, 30);
        playButton.addActionListener(this);
        inputScreen.add(playButton);
        inputScreen.setVisible(true);
    }


    /**
     * the actionPerformed method sets the name and theme within the player, as
     * well as begins the game.
     * 
     * @param e
     *            event that has been fired
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == nameEnter)
        {
            frame.gameboard.p.setName(nameEnter.getText());
        }
        if (e.getSource() == themeEnter)
        {
            frame.gameboard.p.setTheme(themeEnter.getText());
        }
        if (e.getSource() == playButton)
        {
            frame.gameboard.p.setName(nameEnter.getText());
            frame.gameboard.p.setTheme(themeEnter.getText());
            myGame.setVisible(true);
            String message = "Welcome, " + frame.gameboard.p.getName() + ". Your theme is "
                + frame.gameboard.p.getTheme() + ". Let's get started!";
            JOptionPane.showMessageDialog(inputScreen, message);
            inputScreen.setVisible(false);

        }
    }

}
