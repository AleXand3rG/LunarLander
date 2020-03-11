package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Spacecraft class - manages object of spacecraft, calculates its movement based on input handed over from Game.run() method
 *
 * @see Game
 */
public class Spacecraft extends JPanel {

    /**
     * public Spacecraft State - defines in what state is spacecraft at the moment
     */
    public enum SpacecraftState {NONEXISTENT, FLYING, LANDED, CRASHED}

    private SpacecraftState spacecraftState = SpacecraftState.NONEXISTENT;
    static double fuelLeft; //used in Level

    /**
     * Spacecraft attributes
     * Spacecraft HitPoints used to check if Spacecraft crashed or landed in Game.checkCollisions() method
     *
     * @see Game
     */
    private final int prefImageSize = Config.spacecraft.getWidth();
    private double x;
    private double y;
    private double rotation;
    private double speedX = 0, speedY = 0;
    private double fuel = Config.fuel;
    private boolean isThrust = false, isNitro = false, isRotatingRight = false, isRotatingLeft = false;
    private Point[] hitPoints;

    /**
     * temp values used for scaling
     */
    private double scaledX, scaledY;
    private int scaledSize;

    /**
     * Spacecraft class constructor - creates hitPoints
     */
    public Spacecraft() {
        hitPoints = new Point[8];
    }

    /**
     * generatePosition method - generates position and rotation of Spacecraft on screen
     *
     * @see Math
     */
    private void generatePosition() {
        x = Math.random() * (Config.prefFrameWidth - 50);
        y = Math.random() * ((float) Config.prefFrameHeight / 10 - 50);
        rotation = Math.random() * 361;
    }

    /**
     * reset method - resets spacecraft's attributes to its initial settings
     */
    public void reset() {
        isThrust = false;
        generatePosition();
        speedX = 0;
        speedY = 0;
        fuel = Config.fuel;
    }

    /**
     * update method - updates spacecraft's current state and model basing on actual Game.gameState
     *
     * @param dt - time
     */
    public void update(double dt) {
        scaledX = (int) ((long) x * (long) Game.mainWindow.getWidth() / (long) Config.prefFrameWidth);
        scaledY = (int) ((long) y * (long) Game.mainWindow.getHeight() / (long) Config.prefFrameHeight);
        scaledSize = (int) ((long) Config.spacecraftSize *
                ((long) Game.mainWindow.getWidth() + (long) Game.mainWindow.getHeight()) /
                ((long) Config.prefFrameWidth + (long) Config.prefFrameHeight));

        if (Game.gameState == Game.GameState.RUNNING) {
            spacecraftState = SpacecraftState.FLYING;
            double gravity = Config.gravity;
            switch (Game.gameDifficulty) {
                case NORMAL:
                    gravity = Config.gravity * 2;
                    break;
                case HARD:
                    gravity = Config.gravity * Config.gravity;
                    break;
            }
            applyGravity(gravity, dt);
            if (fuel > 0) {
                if (isThrust) {
                    applyThrust(dt);
                }
                if (isRotatingRight) {
                    rotation -= applyRotation(dt);
                }
                if (isRotatingLeft) {
                    rotation += applyRotation(dt);
                }
            }
            adjustCoordinates(dt);

            hitPoints = new Point[]{
                    new Point((int) (scaledX + scaledSize / 3), (int) (scaledY + scaledSize / 3)), //left upper corner
                    new Point((int) (scaledX + 2 * scaledSize / 3), (int) (scaledY + scaledSize / 3)), //right upper corner

                    new Point((int) (scaledX + scaledSize / 2), (int) (scaledY + scaledSize / 3)), //up
                    new Point((int) (scaledX + scaledSize / 3), (int) (scaledY + scaledSize / 2)), //left
                    new Point((int) (scaledX + 2 * scaledSize / 3), (int) (scaledY + scaledSize / 2)), //right

                    new Point((int) (scaledX + scaledSize / 4), (int) (scaledY + 2 * scaledSize / 3)), //left bottom corner
                    new Point((int) (scaledX + scaledSize / 2), (int) (scaledY + scaledSize / 3)), //down
                    new Point((int) (scaledX + 3 * scaledSize / 4), (int) (scaledY + 2 * scaledSize / 3)) //right bottom corner
            };
        }
        if (Game.gameState == Game.GameState.FAILURE) {
            rotation = 0;
            spacecraftState = SpacecraftState.CRASHED;
        }
        if (Game.gameState == Game.GameState.PAUSED) {
            isThrust = false;
        }
        if (Game.gameState == Game.GameState.INIT) {
            spacecraftState = SpacecraftState.NONEXISTENT;
        }
        if (Game.gameState == Game.GameState.SUCCESS) {
            spacecraftState = SpacecraftState.LANDED;
        }
        updateFuel(dt);
    }

    /**
     * updateFuel method - updates spacecraft's fuel state
     *
     * @param dt - time used in updating calculated using UPS
     * @see Game
     * @see Config
     */
    private void updateFuel(double dt) {
        if (spacecraftState == SpacecraftState.FLYING) {
            if (fuel > 0) {
                if (isThrust) {
                    fuel -= 0.05 * dt;
                }
                if (isNitro) {
                    fuel -= 0.1 * dt;
                }
                if (isRotatingLeft || isRotatingRight) {
                    fuel -= 0.01 * dt;
                }
            } else {
                fuel = 0;
            }
        }
        fuelLeft = fuel;
    }

    /**
     * applyGravity method - applies gravity of the moon
     *
     * @param gravity - gravity constant
     * @param dt      - time
     */
    private void applyGravity(double gravity, double dt) {
        this.speedY += gravity * dt;
    }

    /**
     * applyThrust method - applies thrust of spacecraft
     *
     * @param dt - time
     */
    private void applyThrust(double dt) {
        double thrust = Config.thrust;
        if (isNitro) {
            thrust *= Config.nitro;
        }
        this.speedX -= Math.sin(Math.toRadians(rotation)) * dt * thrust;
        this.speedY -= Math.cos(Math.toRadians(rotation)) * dt * thrust;
    }

    /**
     * applyRotation method - applies rotation of spacecraft
     *
     * @param dt - time
     * @return rotation delta (angle which should be added or subtracted to/from rotation attribute)
     */
    private double applyRotation(double dt) {
        double rotationDelta;
        switch (Game.gameDifficulty) {
            case NORMAL:
                rotationDelta = Config.rotation * dt;
                break;
            case HARD:
                rotationDelta = Math.abs(Math.random()) * 4 * Config.rotation * dt;
                break;
            default:
                rotationDelta = 1.2 * Config.rotation * dt;
                break;
        }
        return rotationDelta;
    }

    /**
     * adjustCoordinates method - makes spacecraft moving on the screen, updates its position
     *
     * @param dt - time
     */
    private void adjustCoordinates(double dt) {
        this.x += speedX * dt;
        this.y += speedY * dt;
    }

    /**
     * paintComponent override method - draws all graphic connected with Spacecraft class
     *
     * @param g - Graphics instance
     * @see Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setDoubleBuffered(true);

        if (spacecraftState != SpacecraftState.NONEXISTENT) {
            g.drawImage(processImage(), (int) scaledX, (int) scaledY, scaledSize, scaledSize, null);
        }


        if (Game.gameDifficulty == Game.GameDifficulty.EASY) {
            //Speed & Rotation
            double req = Config.requiredRotation;
            if ((getRotation() > 0 && getRotation() < req) || (getRotation() < 360 && getRotation() > 360 - req)) {
                if ((Math.abs(speedX) < Config.requiredSpeedX && Math.abs(speedY) < Config.requiredSpeedY)) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.RED);
                }
            } else {
                g.setColor(Color.RED);
            }

            int scaledFontSize = (int) ((long) Config.fontSize - 5 * (long) Config.currentFrameWidth / (long) Config.prefFrameWidth);
            g.setFont(new Font(Config.font, Font.PLAIN, scaledFontSize));
            g.drawString("Speed X: " + String.format("%.2f", getSpeedX()), (int) x, (int) y - 20);
            g.drawString("SpeedY: " + String.format("%.2f", getSpeedY()), (int) x, (int) y - 10);
            g.drawString("Rotation: " + String.format("%.2f", getRotation()), (int) x, (int) y);
            g.setColor(Color.WHITE);
        }
    }

    /**
     * processImage method - prepares spacecraft's images to be displayed
     *
     * @return processed image
     */
    private BufferedImage processImage() {
        AffineTransform at = new AffineTransform();
        at.translate((float) prefImageSize / 2, (float) prefImageSize / 2);
        at.rotate(-Math.toRadians(rotation), 0, 0);
        at.translate((float) -prefImageSize / 2, (float) -prefImageSize / 2);
        AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage processedImage;
        switch (spacecraftState) {
            case FLYING:
                if (isThrust && fuel > 0) {
                    processedImage = new BufferedImage(prefImageSize, prefImageSize, Config.spacecraftWithFlame.getType());
                    rotateOp.filter(Config.spacecraftWithFlame, processedImage);
                } else {
                    processedImage = new BufferedImage(prefImageSize, prefImageSize, Config.spacecraft.getType());
                    rotateOp.filter(Config.spacecraft, processedImage);
                }
                break;
            case CRASHED:
                processedImage = new BufferedImage(prefImageSize, prefImageSize, Config.spacecraftCrashed.getType());
                rotateOp.filter(Config.spacecraftCrashed, processedImage);
                break;
            case LANDED:
                processedImage = new BufferedImage(prefImageSize, prefImageSize, Config.spacecraft.getType());
                rotateOp.filter(Config.spacecraft, processedImage);
            default:
                processedImage = new BufferedImage(prefImageSize, prefImageSize, Config.spacecraft.getType());
                rotateOp.filter(Config.spacecraft, processedImage);
                break;
        }
        return processedImage;
    }

    public void setRotatingRight(boolean isRotatingRight) {
        this.isRotatingRight = isRotatingRight;
    }

    public void setRotatingLeft(boolean isRotatingLeft) {
        this.isRotatingLeft = isRotatingLeft;
    }

    public void setThrust(boolean isThrust) {
        this.isThrust = isThrust;
    }

    public void setNitro(boolean isNitro) {
        this.isNitro = isNitro;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getRotation() {
        return normalizeAngle(rotation);
    }

    private double normalizeAngle(double rotation) {
        return Math.abs(rotation % 360);
    }

    public static double getFuelLeft() {
        return fuelLeft;
    }

    public Point[] getHitPoints() {
        return hitPoints;
    }
}