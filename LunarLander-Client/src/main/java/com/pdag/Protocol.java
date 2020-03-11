package main.java.com.pdag;

/**
 * Client Protocol Class - specifies commands used when communicating with server
 */
public class Protocol {
    public static final String CHECK = "Are you available";
    public static final String CHECKED = "Server available";
    public static final String CONNECT = "Connect";
    public static final String CONNECTED = "Connected";

    public static final String GET_CONFIG_DATA = "Get configuration: ";
    public static final String GET_LEVEL = "Get level: ";
    public static final String GET_HIGHSCORES = "Get high scores";
    public static final String NEW_HIGHSCORE = "New high score";

    public static final String CLOSE_CONNECTION = "Close connection";
    public static final String CONNECTION_CLOSED = "Connection closed";
    public static final String ERROR = "Error";
}