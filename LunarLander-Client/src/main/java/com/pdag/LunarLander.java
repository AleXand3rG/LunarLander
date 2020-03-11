package main.java.com.pdag;

import javax.swing.*;

/**
 * LunarLander class - main application class and method
 * <p>
 * creates new instance of Game() class
 * </p>
 *
 * @author PDAG
 * @version 1.0
 * @see Game
 */
public class LunarLander {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}