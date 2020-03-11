package main.java.com.pdag;

import java.io.Serializable;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Client HighScores class - stores high scores data
 * This class implements Serializable interface, so that it can be sent through socket
 *
 * @see Serializable
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 */
public class HighScores implements Serializable {

    /**
     * serialVersionUID - used to recognize this class from another application
     */
    private static final long serialVersionUID = 2L;
    public TreeMap<Double, String> highScores;

    public HighScores() {
        highScores = new TreeMap<>(Collections.reverseOrder());
    }
}

