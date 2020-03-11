package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;


/**
 * LunarLanderServer Class
 * main application class and method
 * <p>
 * sets server JFrame and creates new instance of Pane class
 *
 * @author PDAG
 * @version 1.0
 * @see JFrame
 * @see Pane
 * </p>
 */
public class LunarLanderServer extends JFrame {

    /**
     * LunarLanderServer constructor - sets JFrame and creates new Pane
     */
    private LunarLanderServer() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setPreferredSize(new Dimension(Config.frameWidth, Config.frameHeight));
        this.setTitle(Config.text.get(1));

        Pane pane = new Pane();
        add(pane);
    }

    /**
     * main application method - starts server application
     * @param args - application arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LunarLanderServer server = new LunarLanderServer();
            server.pack();
            Dimension screenSize = server.getToolkit().getScreenSize();
            server.setLocation(
                    (int) (screenSize.getWidth() / 10 - server.getWidth() / 10),
                    (int) (screenSize.getHeight() / 5 - server.getHeight() / 5));
            server.setVisible(true);
        });
    }
}