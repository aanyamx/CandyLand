import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JOptionPane;

/**
 * This class provides all the functionality of the backend. It is responsible
 * for using ChatGPT to generate the trivia questions for the player, checking
 * whether they were correct or not, and building the queue representation of
 * the progress report, as well as the 2D array complement.
 */
public class Player
{
    /**
     * the number of questions the user is asked
     */
    private final int               NUM_QUESTIONS   = 8;

    /**
     * the name of the user
     */
    private String                  name;

    /**
     * the topic/theme
     */
    private String                  theme;

    /**
     * the number of points they've gotten correct
     */
    private int                     score;

    /**
     * the keys are C or W and an id number. value is the question. Used to give
     * user feedback.
     */
    private HashMap<String, String> questions;

    /**
     * 2D array of the user's answer, the question, whether they got it right,
     * and what the right answer was
     */
    private String[][]              QuestionReport;

    /**
     * all the answers that the user provided
     */
    private Queue<String>           allAnswers;

    /**
     * false when user is answering so that the side scroll doesn't keep moving
     * forward. true when the board needs to keep moving.
     */
    public boolean                  answerSubmitted = true;

    /**
     * what number question the user is on/what landmark they are at this is
     * primarily used for the hashmap and sidescrolling
     */
    private int                     marker;

    /**
     * shows the user the question or if there is a question coming up or if all
     * questions have been answered
     */
    private JLabel                  questionLabel;

    /**
     * shows the user how many questions theyve gotten right
     */
    private JLabel                  scoreLabel;

    /**
     * dialogue at the bottom of the screen from the characeters
     */
    private JLabel                  dialogueLabel;

    /**
     * field where users answer questions
     */
    private JTextField              answerField;

    /**
     * sneds users answer for processing
     */
    private JButton                 submitButton;

    /**
     * the text that will be shown to the user at the bottom of the screen
     * having conversation with the user + engaging them in the game
     */
    private String[]                dialogues       = {
        "Welcome! I'm Mama Gingerbread. Please feel free to take a drink of tea.",
        "Golly! Who do we have here? I myself am Mr. Mint, if you so please.",
        "Wow, I don't get a lot of visitors in Gumdrop Valley. Pleased to meet you, my name is Jolly!",
        "Ohoho, welcome. My name is LORD LICORICE. Call me as such, or face the wrath of my smolder >:)",
        "Howdy! Come on down to Peanut Acres. We got lots of yummy peanut treats.",
        "Oh wow, you look so colorful! This here is Lollipop Woods. I'm Lolly.",
        "HI! This here is Snowflake Lake. I'm Princess Frostine!! Let me know if you need a coat!",
        "*sound of rising chocolate mound* I  a m  G l o o p y. " };

    /**
     * the area where the submit button, the question, and the textfield are
     * shown
     */
    public JPanel                   QAPanel;

    /**
     * conversation with the user is shown
     */
    public JPanel                   dialoguePanel;

    /**
     * constructor, sets name, score, intializes questions, initializes answers,
     * sets marker, initializes questionreport, intializes panels, texts, and
     * fields and sets all formatting.
     */
    public Player()
    {
        name = "";
        score = 0;
        questions = new HashMap<String, String>();
        allAnswers = new LinkedList<String>();
        marker = 0;
        QuestionReport = new String[NUM_QUESTIONS][4];

        QAPanel = new JPanel(new GridBagLayout());
        QAPanel.setBackground(new Color(250, 230, 248));
        GridBagConstraints constraints = new GridBagConstraints();

        // new Panel
        dialoguePanel = new JPanel();
        dialoguePanel.setBackground(new Color(250, 230, 248));

        // question
        questionLabel = new JLabel("Next Question coming up!");
        questionLabel.setFont(new Font("MONOSPACED", Font.BOLD, 16));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets.left = 5;
        QAPanel.add(questionLabel, constraints);

        // answer
        answerField = new JTextField(20);
        constraints.gridx = 0;
        constraints.gridy = 1;
        answerField.setEditable(false);
        QAPanel.add(answerField, constraints);

        // submission button
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("MONOSPACED", Font.ITALIC, 14));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.WEST;
        QAPanel.add(submitButton, constraints);

        // score
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("MONOSPACED", Font.BOLD, 16));
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.EAST;
        QAPanel.add(scoreLabel, constraints);

        // character dialogue
        dialogueLabel =
            new JLabel("Welcome to CandyLand! Begin your journey by pressing the right arrow key.");
        dialogueLabel.setFont(new Font("MONOSPACED", Font.BOLD, 20));
        dialoguePanel.add(dialogueLabel);

        submitButton.addActionListener(e -> {
            String answer = answerField.getText();
            checkAnswer(answer, questionLabel.getText());
            answerSubmitted = true;

        });
    }


    /**
     * Gets the 2D Array of questions, user's answers, and whether they were
     * right or not.
     * 
     * @return Question Report of user data
     */
    public String[][] getQuestionReport()
    {
        return QuestionReport;
    }


    /**
     * Gets the marker (used to determine whether we need to stop and where we
     * are for dialogue)
     * 
     * @return marker
     */
    public int getMarker()
    {
        return marker;
    }


    /**
     * Gets the user's score.
     * 
     * @return score
     */
    public int getScore()
    {
        return score;
    }


    /**
     * Sets the user's name.
     * 
     * @param s
     *            user's name
     */
    public void setName(String s)
    {
        name = s;
    }


    /**
     * Sets the theme the user desires.
     * 
     * @param s
     *            desired theme.
     */
    public void setTheme(String s)
    {
        theme = s;
    }


    /**
     * Gets the user's name.
     * 
     * @return user's name
     */
    public String getName()
    {
        return name;
    }


    /**
     * gets the user's theme.
     * 
     * @return theme
     */
    public String getTheme()
    {
        return theme;
    }


    /**
     * Sets the frame to be visible or not.
     * 
     * @param visible
     *            if true, makes visible, if false, makes invisible
     */
    public void setVisible(boolean visible)
    {
        // questionLabel.setVisible(visible);
        // answerField.setVisible(visible);
        // submitButton.setVisible(visible);
    }


    /**
     * Once the user has submitted an answer, it replaces the question dialogue.
     * It lets the user know if more is coming up or if they are done. It also
     * clears the answer box.
     */
    public void clearQA()
    {
        if (answerSubmitted)
        {
            if (marker == NUM_QUESTIONS)
            {
                questionLabel.setText("No further questions.");
                dialogueLabel.setText(
                    "You have completed your journey and are now approaching Candy Castle.");
            }
            else
            {
                questionLabel.setText("Next Question coming up!");
            }
            answerField.setText("");

        }
    }


    /**
     * Checks if user has reached end of the game (if the user has finished 8
     * questions), displaying EndScreen if they have. Otherwise, it creates the
     * question, processes it, adds it to questionreport, and then displays the
     * question and dialogue. It also takes in the user's answer and sends it to
     * be checked with checkAnswer.
     */
    public void generateQA()
    {
        if (marker == NUM_QUESTIONS)
        {
            endMessage();
            EndScreen ending = new EndScreen();
            return;
        }
        else
        {
            try
            {
                marker++;
                String query = "Give me a trivia question about " + theme
                    + ". Write a delimiter of '###'', afterwards add the short answer. Do not give me any other information, not even a period at the end of the sentence";
                String gptQ = chatGPT(query); // gptQ contains the response
                                              // ChatGPT generated of the query
                                              // above
                while (gptQ.length() > 90) // If the length is > 90, it may
                                           // cause formatting issues
                {
                    gptQ = chatGPT(query);
                }
                String question = gptQ.split("###")[0];

                if (question.charAt(0) == '.' || question.charAt(0) == ';'
                    || question.charAt(0) == '!' || question.charAt(0) == ',')
                {
                    question = question.substring(1);
                }

                String answer = gptQ.split("###")[1];
                QuestionReport[marker - 1][3] = answer;
                allAnswers.add(answer);
                String myQS = question.trim();
                dialogueLabel.setText(dialogues[marker - 1]);
                questionLabel.setText(myQS);
                answerField.setEditable(true);

                setVisible(true);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }


    /**
     * Checks whether the user is correct or not. Sets values in the final user
     * report for their question, their answer, and whether they got it right or
     * not. Also determines whether they will recieve a popup that says they are
     * correct or incorrect.
     * 
     * @param uAnswer
     *            user's answer.
     * @param q
     *            question asked
     */
    public void checkAnswer(String uAnswer, String q)
    {
        try
        {
            String query = "This is a question: " + q + ". This is my answer: " + uAnswer
                + ". If my answer is correct, just return 1 and do not give me anything else. If it is incorrect, return 0 and nothing else.";
            String gpt01 = chatGPT(query); // gptQ01 contains the response
                                           // ChatGPT generated of the query
                                           // above

            QuestionReport[marker - 1][0] = q;
            QuestionReport[marker - 1][1] = uAnswer;

            if (gpt01.contains("1")) // Correct answer
            {
                JOptionPane.showMessageDialog(
                    QAPanel,
                    "You are correct!",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                score++;
                scoreLabel.setText("Score: " + score);
                QuestionReport[marker - 1][2] = "Yes";
                QuestionReport[marker - 1][3] = uAnswer;
                questions.put("C" + marker, q.trim()); // Indicates correct
            }
            else // Wrong answer
            {
                JOptionPane.showMessageDialog(
                    QAPanel,
                    "You are incorrect :(",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                QuestionReport[marker - 1][2] = "No";
                questions.put("W" + marker, q.trim()); // Indicates wrong
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            generateQA();
        }
    }


    /**
     * Is responsible for creating a queue of correct and wrong answers. It
     * iterates through map and adds to correct queue. It uses this information
     * to calculate percentages for bar graph and to create it.
     */
    public void endMessage()
    {
        // Separates the correct questions and the wrong questions
        Queue<String> correct = new LinkedList<String>();
        Queue<String> wrong = new LinkedList<String>();
        Iterator<Map.Entry<String, String>> iterator = questions.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();

            if (key.charAt(0) == 'C')
            {
                correct.add(entry.getValue());
            }
            else
            {
                wrong.add(entry.getValue());
            }
        }

        String askgpt = "";
        String topics = "";
        String percentages = "";

        try
        {
            String query = "Questions I got right: " + correct + ".\n Questions I got wrong: "
                + wrong + ".\n return two lists - one containing 3 specific topics about " + theme
                + " that considers ALL of the questions (both right and wrong). The topics should be seperated by commas, even if there are two topics. the second list should contain the corresponding percentages (NOT FRACTIONS, NOT DECIMALS) (how many i got right on that topic/how many questions on that topic). the percentages should be between 0 and 100. both lists should be the same length of 3. there should be a delimiter of a ### between the topics and the percentages. the format should be the following: topic1, topic2, topic3###number1, number2, number3. do not give a %. do not give me any other information besides the format i provided you. no explanations. do not give me percent symbols.";
            askgpt = chatGPT(query); // askgpt contains the response ChatGPT
                                     // generated of the query above
            askgpt = askgpt.replace("%", ""); // Formatting issue

            topics = (askgpt.split("###")[0]).trim();
            percentages = (askgpt.split("###")[1]).trim();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        int c = 0;
        // Figures out how many topics there are (this is to create the length
        // of the array)
        for (int i = 0; i < topics.length(); i++)
        {
            if (topics.charAt(i) == ',')
            {
                c++;
            }
        }

        double[] topicArray = new double[c + 1];
        String[] percentageArray = new String[c + 1];

        // Parses through the percentages and adds it into the array
        for (int i = 0; i < topics.length(); i++)
        {
            int comma = topics.indexOf(",");
            if (topics.contains(","))
            {
                if (comma != -1)
                {
                    percentageArray[i] = topics.substring(i, comma);
                    topics = topics.substring(comma + 1);
                }
                else if (comma == -1)
                {
                    percentageArray[i] = topics.substring(i - 1);
                    topics = "";
                    break;
                }
            }
            else
            {
                percentageArray[i] = topics;
                break;
            }
        }

        // Parses through the topics and adds it into the array
        int count = 0;
        for (int i = 0; i < percentages.length(); i++)
        {
            int comma = percentages.indexOf(",");
            if (comma != -1)
            {
                topicArray[count] = Double.parseDouble(percentages.substring(i, comma));
                percentages = percentages.substring(comma + 2);
                count++;
                i = -1;
            }
            else
            {
                topicArray[count] = Double.parseDouble(percentages.substring(i));
                break; // dfsjkhdsfkj
            }
        }

        Bargraph bars = new Bargraph("CandyLand Statistics", topicArray, percentageArray);
        try
        {
            bars.display(topicArray);
        }
        catch (Exception e)
        {
            System.out.print("");
        }
    }


    /**
     * Initializes connection to openAI API, passes the query and saves the
     * response.
     * 
     * @param text
     *            user's query to chatgpt
     * @return chatgpt's response
     * @throws Exception
     *             if cannot connect to openAI
     */
    public static String chatGPT(String text)
        throws Exception
    {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty(
            "Authorization",
            "Bearer "); // Insert an OpenAI API Key here

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 2000); // Character limit
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
            .reduce((a, b) -> a + b).get();

        return (new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text")); // ChatGPT's
                                                                                                    // response
    }

}
