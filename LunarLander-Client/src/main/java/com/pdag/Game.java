package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;

/**
 * Game class - core game class
 * <p>
 * - organizes all classes
 * - creates game window
 * - implements main game runtime in method run()
 * </p>
 */
public class Game extends JComponent implements Runnable {

    /**
     * public Game State - defines in what state is Game at the moment
     */
    public enum GameState {INIT, RUNNING, PAUSED, SUCCESS, FAILURE, NEW_GAME, CHANGING_LEVEL, PROCESSING_WIN, END_GAME}

    public enum GameDifficulty {EASY, NORMAL, HARD}

    public static GameState gameState;
    public static GameDifficulty gameDifficulty;

    /**
     * frame and system attributes
     */
    private Thread mainThread;
    public static Window mainWindow;
    private static boolean running = false;

    /**
     * gameplay attributes
     */
    private final Spacecraft player;
    private Level lvl;

    /**
     * Game class constructor - creates game and all needed instances of other classes
     *
     * @see Spacecraft
     * @see Level
     * @see Window
     * @see KeyInput
     * @see Config
     */
    Game() {
        gameState = GameState.INIT;
        gameDifficulty = GameDifficulty.NORMAL;
        lvl = new Level();
        player = new Spacecraft();
        KeyInput keyboardReader = new KeyInput(player, this);
        mainWindow = new Window(keyboardReader, this);
    }

    /**
     * run override method - main game runtime method, synchronizes updates, manages input and output
     *
     * @see Thread
     */
    @Override
    public void run() {
        long initialTime = System.nanoTime();
        final double timeU = 1000000000 / (double) Config.UPS;
        final double timeF = 1000000000 / (double) Config.FPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, updates = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                //System.out.println(String.format("UPS: %s, FPS: %s", updates, frames));
                frames = 0;
                updates = 0;
                timer += 1000;
            }
        }
        stop();
    }

    /**
     * update method - updates game model: Level, Spacecraft, GameState. It's called with UPS frequency
     *
     * @see Config
     */
    private void update() {
        lvl.update();
        player.update((double) 1 / Config.UPS);
        Score.update();

        switch (gameState) {
            case RUNNING:
                checkCollisions();
                break;
            case NEW_GAME:
                Config.levelNumber = 1;
                Score.reset();
                reset();
                break;
            case SUCCESS:
                Score.levelCompleted(Config.levelNumber);
                JOptionPane.showConfirmDialog(Game.mainWindow, Config.winMessage, null, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                gameState = GameState.PROCESSING_WIN;
                break;
            case CHANGING_LEVEL:
                Score.resetLevelScore();
                reset();
                break;
            case PROCESSING_WIN:
                if (Score.isAllLevelsCompleted()) {
                    gameState = GameState.END_GAME;
                } else {
                    if (Config.levelNumber != Config.numberOfLevels) {
                        Config.levelNumber++;
                    } else {
                        Config.levelNumber = 1;
                    }
                    gameState = GameState.INIT;
                    reset();
                }
                break;
            case END_GAME:
                Settings.openEndGameDialog();
                reset();
                break;
            default:
                break;
        }
    }

    /**
     * checkCollisions - checks spacecraft's collisions with other elements
     * sets gameState basing on current Spacecraft state
     */
    private void checkCollisions() {

        boolean isCollision = false;
        boolean isLanded = false;

        Polygon levelHitBox = lvl.getHitBox();
        Polygon landingSite = lvl.getLandingSite();
        Point[] pHitPoints = player.getHitPoints();

        for (Point p : player.getHitPoints()) {
            if (levelHitBox.contains(p)) {
                isCollision = true;
            }
        }

        if (landingSite.contains(pHitPoints[5]) || landingSite.contains(pHitPoints[6]) || landingSite.contains(pHitPoints[7])) {
            double req = Config.requiredRotation;
            double rot = player.getRotation();
            if ((rot > 0 && rot < req) || (rot < 360 && rot > 360 - req)) {
                if (player.getSpeedX() < Config.requiredSpeedX && player.getSpeedY() < Config.requiredSpeedY) {
                    isLanded = true;
                    if (Math.abs(player.getSpeedX()) < Config.bonusRequiredSpeed[Config.levelNumber - 1])
                        Score.claimBonus(Config.bonusScore);
                    if (Math.abs(player.getSpeedY()) < Config.bonusRequiredSpeed[Config.levelNumber - 1])
                        Score.claimBonus(Config.bonusScore);
                    if (Math.abs(player.getRotation()) < 1) Score.claimBonus(Config.bonusScore * 2);
                }
            }
        }

        if (isLanded) gameState = GameState.SUCCESS;
        else if (isCollision) gameState = GameState.FAILURE;
        else gameState = GameState.RUNNING;
    }

    /**
     * paintComponent override method - draws all graphic connected with Game class
     *
     * @param g - Graphics instance
     * @see Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Config.background, 0, 0, this.getWidth(), this.getHeight(), null);
        player.paintComponent(g);
        lvl.paintComponent(g);
    }

    /**
     * start method - starts executing Game Thread
     */
    public synchronized void start() {
        mainThread = new Thread(this);
        mainThread.start();
        running = true;
    }

    /**
     * stop method - stops executing Game Thread
     */
    public synchronized void stop() {
        try {
            mainThread.join();
            running = false;
        } catch (InterruptedException ex) {
            Config.printError(ex, "Couldn't stop main game thread");
        }
    }

    /**
     * reset method - resets current game
     */
    public void reset() {
        player.reset();
        Config.getLevel();
        lvl = new Level();
        gameState = GameState.INIT;
    }

    /**
     * relaunch method - resets current level without corrupting Total Score
     */
    public void relaunch() {
        player.reset();
        Score.resetLevelScore();
        gameState = GameState.RUNNING;
        Score.resetLevelTimer();
    }
}
