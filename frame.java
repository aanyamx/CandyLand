import javax.swing.JFrame;

/**
 * The runner class. Called automatically using main method. Creates a new board
 * and a welcome screen.
 */
public class frame
{
  // must be static such that other GUI functions can run.
  static Board gameboard;

  /**
   * Method that runs the whole game.
   * 
   * @param args
   *          arbitrary args
   */
  public static void main(String[] args)
  {
    gameboard = new Board();
    JFrame game = new JFrame("game");
    WelcomeScreen ws = new WelcomeScreen(game);
    game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    game.getContentPane().add(gameboard);
    game.pack();

  }

}
