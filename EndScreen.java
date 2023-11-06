import java.awt.*;
import javax.swing.*;

/**
 * The class EndScreen displays the user's results at the very end of the game
 * using a JTable and data from the Player. It tells the user the questions they
 * answered, what their answer was, and whether that answer was right or not. It
 * also erases content from the main gameboard and provides the user a parting
 * message.
 */
public class EndScreen
{

  /**
   * Constructs an EndScreen that transfers the data from the 2D array created
   * in Player to a JTable and displays it for the user to see their gameplay
   * history.
   */
  public EndScreen()
  {
    String[][] QuestionReport = frame.gameboard.p.getQuestionReport();
    JPanel endPanel = new JPanel(new BorderLayout());
    JFrame endScreen = new JFrame("Results");
    endScreen.setBounds(100, 70, 900, 400);
    endScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    String[] columnNames = { "Questions", "Your Answer", "Correct", "Correct Answer" };

    JTable table = new JTable(QuestionReport, columnNames);
    table.setPreferredScrollableViewportSize(new Dimension(900, 400));
    table.setFillsViewportHeight(true);

    table.setFont(new Font("MONOSPACED", Font.BOLD, 14));
    JScrollPane scrollPane = new JScrollPane(table);

    JLabel scoreLabel = new JLabel("Score: " + frame.gameboard.p.getScore());
    scoreLabel.setFont(new Font("MONOSPACED", Font.BOLD, 18));
    scoreLabel.setBackground(new Color(250, 230, 248));

    JLabel message = new JLabel(
      "Congratulations " + frame.gameboard.p.getName()
        + " on completing your journey! Below you will find a report of your gameplay.");
    message.setFont(new Font("MONOSPACED", Font.BOLD, 18));
    message.setBackground(new Color(250, 230, 248));

    endPanel.add(message, BorderLayout.NORTH);
    endPanel.add(scrollPane, BorderLayout.CENTER);
    endPanel.add(scoreLabel, BorderLayout.SOUTH);

    endScreen.getContentPane().add(endPanel);
    endScreen.pack();
    endScreen.setVisible(true);

  }

}
