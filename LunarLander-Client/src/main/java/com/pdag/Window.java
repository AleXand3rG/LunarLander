package main.java.com.pdag;

import javax.swing.*;

/**
 * Window class - creates game window and sets its preferences basing on Config class
 *
 * @see javax.swing.JFrame
 * @see Config
 */
public class Window extends JFrame {

    /**
     * Window class constructor - creates main application window
     *
     * @param keyboardReader - KeyInput instance
     * @param game           - Game instance
     */
    public Window(KeyInput keyboardReader, Game game) {

        //setting window preferences
        this.setTitle(Config.title);
        this.setSize(Config.prefFrameWidth, Config.prefFrameHeight);
        this.setIconImage(Config.icon);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setMaximumSize(new Dimension(this.getToolkit().getScreenSize()));

        //adding Menu Bar to the window
        final MenuBar menuBar = new MenuBar(new MenuItemListener());
        this.setJMenuBar(menuBar);

        //adding game view to the window
        this.add(game);

        //adding Key Listener to the window
        this.addKeyListener(keyboardReader);

        this.setVisible(true);
        game.start();
    }
}