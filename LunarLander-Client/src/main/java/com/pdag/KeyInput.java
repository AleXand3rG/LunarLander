package main.java.com.pdag;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * KeyInput class - gets input from keyboard and handles events
 *
 * @see java.awt.event.KeyAdapter
 * @see java.awt.event.KeyEvent
 */
public class KeyInput extends KeyAdapter {

    public enum Controls {ARROWS, WSAD}

    public static Controls controls;

    private final Spacecraft player;
    private final Game game;

    /**
     * KeyInput constructor
     *
     * @param player - current player to be handled
     * @param game - main game from Game class
     */
    public KeyInput(Spacecraft player, Game game) {
        controls = Controls.ARROWS;
        this.player = player;
        this.game = game;
    }

    /**
     * KeyPressed override method - defines events after some particular key is pressed
     *
     * @param e - Event: some key has been pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (controls) {
            case ARROWS:
                switch (key) {
                    case KeyEvent.VK_UP:
                        player.setThrust(true);
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.setRotatingRight(true);
                        break;
                    case KeyEvent.VK_LEFT:
                        player.setRotatingLeft(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        player.setNitro(true);
                        break;
                    case KeyEvent.VK_ENTER:
                        if (Game.gameState == Game.GameState.INIT || Game.gameState == Game.GameState.FAILURE)
                            game.relaunch();
                        break;
                    default:
                        break;
                }
                break;
            case WSAD:
                switch (key) {
                    case KeyEvent.VK_W:
                        player.setThrust(true);
                        break;
                    case KeyEvent.VK_D:
                        player.setRotatingRight(true);
                        break;
                    case KeyEvent.VK_A:
                        player.setRotatingLeft(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        player.setNitro(true);
                        break;
                    case KeyEvent.VK_ENTER:
                        if (Game.gameState == Game.GameState.INIT || Game.gameState == Game.GameState.FAILURE)
                            game.relaunch();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * KeyReleased override method - defines events after some particular key is released
     *
     * @param e - Event: some key has been released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (controls) {
            case ARROWS:
                switch (key) {
                    case KeyEvent.VK_UP:
                        player.setThrust(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.setRotatingRight(false);
                        break;
                    case KeyEvent.VK_LEFT:
                        player.setRotatingLeft(false);
                    case KeyEvent.VK_SPACE:
                        player.setNitro(false);
                        break;
                    default:
                        break;
                }
                break;
            case WSAD:
                switch (key) {
                    case KeyEvent.VK_W:
                        player.setThrust(false);
                        break;
                    case KeyEvent.VK_D:
                        player.setRotatingRight(false);
                        break;
                    case KeyEvent.VK_A:
                        player.setRotatingLeft(false);
                    case KeyEvent.VK_SPACE:
                        player.setNitro(false);
                        break;
                    default:
                        break;
                }
                break;
        }
    }
}
