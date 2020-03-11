package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * MenuItemListener class - creates ActionListener used in MenuBar class to handle events
 *
 * @see MenuBar
 * @see java.awt.event.ActionListener
 */
public class MenuItemListener implements ActionListener {

    /**
     * actionPerformed method - main event processing method based on ActionCommands
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println("JMenuItem clicked: " + command);

        boolean tmp = true;
        if (command.contains("Level")) {
            if (Game.gameState != Game.GameState.INIT && Score.getCurrentTotalScore() != 0) {
                Game.gameState = Game.GameState.PAUSED;
                tmp = confirmDialog(createMessage(command), "Change Level");
                Game.gameState = Game.GameState.RUNNING;
            }
            if (tmp) {
                command = command.substring(6);
                Config.levelNumber = Integer.parseInt(command);
                Game.gameState = Game.GameState.CHANGING_LEVEL;
            }
        } else {
            switch (e.getActionCommand()) {
                //Game
                case "New":
                    if (Score.getCurrentTotalScore() > 0) {
                        tmp = confirmDialog(createMessage(command), "Start New Game");
                    }
                    if (tmp) {
                        Game.gameState = Game.GameState.NEW_GAME;
                    }
                    break;
                case "Play/Pause":
                    if (Game.gameState == Game.GameState.PAUSED) {
                        Game.gameState = Game.GameState.RUNNING;
                    } else if (Game.gameState == Game.GameState.RUNNING) {
                        Game.gameState = Game.GameState.PAUSED;
                    }
                    break;
                case "HighScores":
                    Settings.openHighScoresDialog();
                    break;
                case "Quit":
                    tmp = confirmDialog(createMessage(command), "Quit Game");
                    if (tmp) {
                        System.exit(0);
                    }
                    break;
                //Settings
                case "Points":
                    Settings.openPointsDialog();
                    break;
                case "Easy":
                    Game.gameDifficulty = Game.GameDifficulty.EASY;
                    break;
                case "Normal":
                    Game.gameDifficulty = Game.GameDifficulty.NORMAL;
                    break;
                case "Hard":
                    Game.gameDifficulty = Game.GameDifficulty.HARD;
                    break;
                case "Arrows":
                    KeyInput.controls = KeyInput.Controls.ARROWS;
                    break;
                case "WSAD":
                    KeyInput.controls = KeyInput.Controls.WSAD;
                    break;
                case "Configure":
                    Config.ip = JOptionPane.showInputDialog(Game.mainWindow, "Enter server IP: ", "Server Configuration", JOptionPane.PLAIN_MESSAGE);
                    Config.port = Integer.parseInt(JOptionPane.showInputDialog(Game.mainWindow, "Enter port number: ", "Server Configuration", JOptionPane.PLAIN_MESSAGE));
                    Config.updateOnlineData();
                    break;
                //Window
                case "Maximize":
                    Game.mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    break;
                case "Minimize":
                    Game.mainWindow.setSize(Config.prefFrameWidth, Config.prefFrameHeight);
                    Game.mainWindow.setLocationRelativeTo(null);
                    break;
                case "Show":
                    Game.mainWindow.setVisible(true);
                    break;
                case "Hide":
                    Game.mainWindow.setVisible(false);
                    break;
                //Help
                case "userManual":
                    try {
                        File userManual = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(Config.userManualURL)).getPath());
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            desktop.open(userManual);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showConfirmDialog(Game.mainWindow, Config.userManualError, null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "About":
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI(Config.aboutURL));
                        } catch (IOException | URISyntaxException ex) {
                            JOptionPane.showConfirmDialog(Game.mainWindow, Config.aboutError, null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * confirmDialog method - shows JOptionPane with confirmation window
     *
     * @param message - text used in JOptionPane
     * @param title   - title of JOptionPane
     * @return confirmation or denial
     * @see JOptionPane
     */
    private boolean confirmDialog(String message, String title) {
        int reply = JOptionPane.showConfirmDialog(Game.mainWindow, message, title, JOptionPane.YES_NO_OPTION);
        return reply == JOptionPane.YES_OPTION;
    }

    /**
     * createMessage method - creates message which is being shown to user
     *
     * @param command - command clicked by user
     * @return String value - bilt message
     */
    private String createMessage(String command) {
        String temp = "Are you sure you want to ";
        switch (command) {
            case "Level":
                temp += "change level?";
                break;
            case "New":
                temp += "start new game?";
                break;
            case "Quit":
                temp += "quit game?";
                break;
            default:
                temp += "perform this action?";
                break;
        }
        temp += " All unsaved progress will be lost.";
        return temp;
    }
}