package main.java.com.pdag;

import javax.swing.*;
import java.util.Map;

/**
 * Settings Class - opens dialogs from MenuBar Settings
 *
 * @see MenuBar
 * @see MenuItemListener
 */
public class Settings {

    /**
     * openEndGameDialog method - opens dialog when all levels have been completed
     */
    public static void openEndGameDialog() {
        String playerNick = JOptionPane.showInputDialog(Game.mainWindow, Config.endGameMessage, "YOU WIN", JOptionPane.PLAIN_MESSAGE);
        Client.sendScore(playerNick, Score.getTotalScore());
    }

    /**
     * setDialog method - sets current dialog
     *
     * @param dialog - specifies which dialog
     */
    private static void setDialog(JDialog dialog) {
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.setSize(Config.dialogWidth, Config.dialogHeight);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
    }

    /**
     * openPointsDialog method - opens dialog with Total Score and score for every played level
     */
    public static void openPointsDialog() {
        JDialog pointsDialog = new JDialog(Game.mainWindow, "Points");
        setDialog(pointsDialog);

        JPanel pointsPanel = new JPanel();
        pointsPanel.add(new JLabel("Total Score: " + Score.getTotalScore(), JLabel.CENTER));
        for (int i = 0; i < Config.numberOfLevels; i++) {
            pointsPanel.add(new JLabel("Level " + (i + 1) + ": " + Score.getThisLevelTotalScore(i)));
        }
        pointsDialog.add(pointsPanel);
        pointsDialog.setVisible(true);
    }

    /**
     * openHighScoerDialog method - opens dialog with High Scores downloaded from server
     *
     * @see Client
     */
    public static void openHighScoresDialog() {
        JDialog highScoresDialog = new JDialog(Game.mainWindow, "High Scores", true);
        setDialog(highScoresDialog);
        JPanel highScoresPanel = new JPanel();

        Config.getHighScores();
        for (Map.Entry<Double, String> entry : Config.highScores.highScores.entrySet()) {
            highScoresPanel.add(new JLabel("Nick: " + entry.getValue() + " ..... Score: " + entry.getKey()));
        }

        highScoresDialog.add(highScoresPanel);
        highScoresDialog.setVisible(true);
    }
}
