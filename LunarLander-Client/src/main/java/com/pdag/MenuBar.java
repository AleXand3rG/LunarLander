package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;

/**
 * MenuBar class - creates menu bar (with ActionListener) on top of the window
 *
 * @see javax.swing.JMenuBar
 * @see java.awt.event.ActionListener
 */
public class MenuBar extends JMenuBar {

    /**
     * MenuBar class constructor - creates menuBar
     *
     * @see MenuItemListener
     * @param menuItemListener - listens evens from menuBar
     */
    public MenuBar(MenuItemListener menuItemListener) {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        final JMenu menuGame = new JMenu("Game");
        {
            JMenuItem game_New = new JMenuItem("New");
            game_New.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            game_New.setActionCommand("New");
            game_New.addActionListener(menuItemListener);
            JMenuItem game_PlayPause = new JMenuItem("Play/Pause");
            game_PlayPause.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            game_PlayPause.setActionCommand("Play/Pause");
            game_PlayPause.addActionListener(menuItemListener);
            JMenuItem game_HighScores = new JMenuItem("High Scores");
            game_HighScores.setActionCommand("HighScores");
            game_HighScores.addActionListener(menuItemListener);
            JSeparator gameSeparator = new JSeparator();
            JMenuItem game_Quit = new JMenuItem("Quit");
            game_Quit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            game_Quit.setActionCommand("Quit");
            game_Quit.addActionListener(menuItemListener);

            menuGame.add(game_New);
            menuGame.add(game_PlayPause);
            menuGame.add(game_HighScores);
            menuGame.add(gameSeparator);
            menuGame.add(game_Quit);
        }

        final JMenu menuSettings = new JMenu("Settings");
        {
            final JMenu menuSettingsLevel = new JMenu("Level");
            {
                JMenuItem[] lvlItems = new JMenuItem[10];
                for (int i = 0; i < 10; i++) {
                    lvlItems[i] = new JMenuItem("Level " + (i + 1));
                    lvlItems[i].setActionCommand("Level " + (i + 1));
                    lvlItems[i].addActionListener(menuItemListener);
                    menuSettingsLevel.add(lvlItems[i]);
                }
                JMenuItem level_Points = new JMenuItem("Points");
                level_Points.setActionCommand("Points");
                level_Points.addActionListener(menuItemListener);
                menuSettingsLevel.add(level_Points);
            }

            final JMenu menuSettingsDifficulty = new JMenu("Difficulty");
            {
                JMenuItem difficulty_Easy = new JMenuItem("Easy");
                difficulty_Easy.setActionCommand("Easy");
                difficulty_Easy.addActionListener(menuItemListener);
                JMenuItem difficulty_Medium = new JMenuItem("Normal");
                difficulty_Medium.setActionCommand("Normal");
                difficulty_Medium.addActionListener(menuItemListener);
                JMenuItem difficulty_Hard = new JMenuItem("Hard");
                difficulty_Hard.setActionCommand("Hard");
                difficulty_Hard.addActionListener(menuItemListener);

                menuSettingsDifficulty.add(difficulty_Easy);
                menuSettingsDifficulty.add(difficulty_Medium);
                menuSettingsDifficulty.add(difficulty_Hard);
            }

            JMenu menuSettingsControls = new JMenu("Controls");
            {
                JMenuItem controls_Arrows = new JMenuItem("Arrows");
                controls_Arrows.setActionCommand("Arrows");
                controls_Arrows.addActionListener(menuItemListener);
                JMenuItem controls_WSAD = new JMenuItem("WSAD");
                controls_WSAD.setActionCommand("WSAD");
                controls_WSAD.addActionListener(menuItemListener);

                menuSettingsControls.add(controls_Arrows);
                menuSettingsControls.add(controls_WSAD);
            }

            JMenuItem serverConfigurations = new JMenuItem("Configure IP and Port");
            serverConfigurations.setActionCommand("Configure");
            serverConfigurations.addActionListener(menuItemListener);

            menuSettings.add(menuSettingsLevel);
            menuSettings.add(menuSettingsDifficulty);
            menuSettings.add(menuSettingsControls);
            menuSettings.add(serverConfigurations);
        }

        JMenu menuWindow = new JMenu("Window");
        {
            JMenuItem window_Maximize = new JMenuItem("Maximize");
            window_Maximize.setActionCommand("Maximize");
            window_Maximize.addActionListener(menuItemListener);
            JMenuItem window_Minimize = new JMenuItem("Minimize");
            window_Minimize.setActionCommand("Minimize");
            window_Minimize.addActionListener(menuItemListener);
            JMenuItem window_Show = new JMenuItem("Show");
            window_Show.setActionCommand("Show");
            window_Show.addActionListener(menuItemListener);
            JMenuItem window_Hide = new JMenuItem("Hide");
            window_Hide.setActionCommand("Hide");
            window_Hide.addActionListener(menuItemListener);

            menuWindow.add(window_Maximize);
            menuWindow.add(window_Minimize);
            menuWindow.add(window_Show);
            menuWindow.add(window_Hide);
        }

        JMenu menuHelp = new JMenu("Help");
        {
            JMenuItem help_Instruction = new JMenuItem("User Manual");
            help_Instruction.setActionCommand("userManual");
            help_Instruction.addActionListener(menuItemListener);
            JMenuItem help_About = new JMenuItem("About");
            help_About.setActionCommand("About");
            help_About.addActionListener(menuItemListener);

            menuHelp.add(help_Instruction);
            menuHelp.add(help_About);
        }

        this.add(menuGame);
        this.add(menuSettings);
        this.add(menuWindow);
        this.add(menuHelp);
        this.setBorderPainted(false);
    }
}