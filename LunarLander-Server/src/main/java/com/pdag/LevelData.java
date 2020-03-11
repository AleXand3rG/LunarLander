package main.java.com.pdag;

import java.io.Serializable;

/**
 * Server LevelData class - stores temp level data
 * This class implements Serializable interface, so that it can be sent through socket
 *
 * @see Serializable
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 */
public class LevelData implements Serializable {

    /**
     * serialVersionUID - used to recognize this class from another application
     */
    private static final long serialVersionUID = 1L;

    public int nPoints;
    public int[] xPoints;
    public int[] yPoints;
    public int[] xLandingSitePoints;
    public int[] yLandingSitePoints;

    LevelData() {
        this.nPoints = Config.nPoints;
        this.xPoints = Config.xPoints;
        this.yPoints = Config.yPoints;
        this.xLandingSitePoints = Config.xLandingSitePoints;
        this.yLandingSitePoints = Config.yLandingSitePoints;
    }
}
