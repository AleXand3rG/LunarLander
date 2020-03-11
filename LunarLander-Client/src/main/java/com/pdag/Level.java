package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;

/**
 * Level Class - creates single level based on given data from Config class
 *
 * @see javax.swing.JPanel
 */
public class Level extends JPanel {

    /**
     * nPoints - defines number of Points
     * xPoints - defines where X coordinates are
     * yPoints - defines where Y coordinates are
     * xLSPoints, yLSPoints - defines where landing site is
     * xP, yP, xLSP, yLSP - temp values used to scale level when scaling application
     * HitBox - used to check if Spacecraft hit anything
     *
     * @see Spacecraft
     */
    private int nPoints;
    private int[] xPoints, xP, xLSPoints, xLSP;
    private int[] yPoints, yP, yLSPoints, yLSP;
    private Polygon currentHitBox;
    private Polygon currentLandingSite; //8 last lines in config files (x,y coordinates)

    /**
     * preferred window dimension used in update() method
     */
    private final double prefFrameWidth = Config.prefFrameWidth;
    private final double prefFrameHeight = Config.prefFrameHeight;

    /**
     * Level class constructor - initializes atributes basing on Config class
     *
     * @see Config
     */
    public Level() {
        nPoints = Config.nPoints;
        xPoints = Config.xPoints;
        yPoints = Config.yPoints;
        xP = new int[nPoints];
        yP = new int[nPoints];
        xLSPoints = Config.xLandingSitePoints;
        yLSPoints = Config.yLandingSitePoints;
        xLSP = new int[4];
        yLSP = new int[4];
    }

    /**
     * update method - updates current level. Method called from Game class
     *
     * @see Game
     */
    public void update() {
        currentHitBox = new Polygon(xPoints, yPoints, nPoints);
        currentLandingSite = new Polygon(xLSPoints, yLSPoints, 4);
        Config.currentFrameWidth = Game.mainWindow.getWidth();
        Config.currentFrameHeight = Game.mainWindow.getHeight();

        for (int i = 0; i < nPoints; i++) {
            xP[i] = (int) ((long) xPoints[i] * (long) Config.currentFrameWidth / (long) prefFrameWidth);
            yP[i] = (int) ((long) yPoints[i] * (long) Config.currentFrameHeight / (long) prefFrameHeight);
        }
        for (int i = 0; i < 4; i++) {
            xLSP[i] = (int) ((long) xLSPoints[i] * (long) Config.currentFrameWidth / (long) prefFrameWidth);
            yLSP[i] = (int) ((long) yLSPoints[i] * (long) Config.currentFrameHeight / (long) prefFrameHeight);
        }
        currentHitBox.xpoints = xP;
        currentHitBox.ypoints = yP;
        currentHitBox.npoints = nPoints;
        currentLandingSite.xpoints = xLSP;
        currentLandingSite.ypoints = yLSP;
    }

    /**
     * getHitBox method - returns current level HitBox
     * @return current Level Hit Box used to check if Spacecraft is crashing
     */
    public Polygon getHitBox() {
        return currentHitBox;
    }

    /**
     * getLandingSite method - returns current Landing Site
     * @return current landing site to check if Spacecraft has landed
     */
    public Polygon getLandingSite() {
        return currentLandingSite;
    }

    /**
     * paintComponent override method - draws all graphic connected with Level class
     *
     * @param g - Graphics instance
     * @see Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setDoubleBuffered(true);

        //drawing level...
        if (currentHitBox != null) {
            g.setColor(Color.BLACK);
            g.fillPolygon(xP, yP, nPoints);
            g.setColor(Color.GREEN);
            g.drawPolygon(currentLandingSite);
            g.setColor(Color.WHITE);
            g.drawPolyline(xP, yP, nPoints);
        }

        //drawing time & fuel
        int scaledFontSize = (int) ((long) Config.fontSize * (long) Config.currentFrameWidth / (long) prefFrameWidth);
        int xScoreBox = (int) ((long) Config.xScoreBox * (long) Config.currentFrameWidth / (long) prefFrameWidth);
        int yTime = (int) ((long) Config.timeY * (long) Config.currentFrameHeight / (long) prefFrameHeight);
        int yFuel = (int) ((long) Config.fuelY * (long) Config.currentFrameHeight / (long) prefFrameHeight);
        int yScore = (int) ((long) Config.scoreY * (long) Config.currentFrameHeight / (long) prefFrameHeight);
        int yLevelCompleted = (int) ((long) Config.levelCompletedY * (long) Config.currentFrameHeight / (long) prefFrameHeight);
        g.setFont(new Font(Config.font, Font.PLAIN, scaledFontSize));

        //Time
        if (Score.getTimeLeft() < 10)
            g.setColor(Color.RED);
        g.drawString("Time: " + String.format("%.2f", Score.getTimeLeft()) + "s", xScoreBox, yTime);
        g.setColor(Color.WHITE);

        //Fuel
        if (Spacecraft.getFuelLeft() == 0)
            g.setColor(Color.RED);
        g.drawString("Fuel: " + String.format("%.2f", Spacecraft.getFuelLeft()) + "kg", xScoreBox, yFuel);
        g.setColor(Color.WHITE);

        //Score
        g.drawString("Score: " + String.format("%.2f", Score.getCurrentTotalScore()), xScoreBox, yScore);
        if (Score.getCompletedLevels()[Config.levelNumber - 1]) {
            g.setColor(Color.GREEN);
            g.drawString("Completed", xScoreBox, yLevelCompleted);
            g.setColor(Color.WHITE);
        }

        //Level 1 contains game title, instructions and signature
        if (Config.levelNumber == 1) {
            int xSignature = (int) ((long) Config.signatureX * (long) Config.currentFrameWidth / (long) prefFrameWidth);
            int ySignature = (int) ((long) Config.signatureY * (long) Config.currentFrameHeight / (long) prefFrameHeight);
            g.setFont(new Font(Config.font, Font.ITALIC, scaledFontSize));
            g.drawString(Config.signature, xSignature, ySignature);

            int fontSizeInstruction = (int) ((long) (Config.fontSize - 3) * (long) Config.currentFrameWidth / (long) prefFrameWidth);
            int xI = (int) ((long) (Config.instructionX) * (long) Config.currentFrameWidth / (long) prefFrameWidth);
            int yI = (int) ((long) (Config.instructionY) * (long) Config.currentFrameHeight / (long) prefFrameHeight);
            if (Config.levelNumber == 1) {
                g.setFont(new Font(Config.font, Font.PLAIN, fontSizeInstruction));
                g.drawString(Config.instruction, xI, yI);
            }
        }
    }
}