package main.java.com.pdag;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Server Config class - stores all configuration data used in application.
 * This class loads online levels from files available in server memory and reads data from configuration files.
 */
public class Config {

    /**
     * configFile - Server Config File used when launching server
     * lunarConfigFile - Config File used when client application asks for configuration data
     * levelFilesPath - specifies where levels on server are
     */
    private static Properties configFile = new Properties();
    public static HighScores highScores = new HighScores();

    public static final String configFileName = "server-config.properties";
    public static final String lunarConfigFileName = "client-config.properties";
    public static final String highscoresFileName = "highscores.hs";
    public static final String levelFilesPath = "/main/resources/lvl/lvl";

    //loading Server Config file form server memory as a Properties file used later to get data from
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                printError(new FileNotFoundException(), "Error: Couldn't find server-config file!");
            } else {
                configFile.load(input);
            }
        } catch (IOException ex) {
            printError(ex, "Error: Couldn't load server-config file!");
        }
    }

    /**
     * default data used when server-config file has not been found
     */
    public static int INFO = 0, SENT = 1, RECEIVED = 2, ERROR = 3;
    private static final int defaultFrameWidth = 450;
    private static final int defaultFrameHeight = 250;
    private static final int defaultButtonWidth = 80;
    private static final int defaultButtonHeight = 25;
    private static final int defaultTextFieldWidth = 4;
    private static final int defaultTextFieldHeight = 25;
    private static final int defaultFontSize = 12;
    private static final int defaultPort = 1000;
    private static final int defaultTimeout = 3000;

    /**
     * defining default colors used in server application
     */
    private static final Color[] defaultFontColor =
            {new Color(0x0044AA), new Color(0x006600), new Color(0x8B008B),
                    new Color(0xEE1111), new Color(0x0055BB)};

    //setting all texts used in LunarLander-Server application
    private static final Map<Integer, String> defaultText;

    static {
        TreeMap<Integer, String> tmp = new TreeMap<>();
        tmp.put(1, "Lunar Lander Server");
        tmp.put(2, "Start");
        tmp.put(3, "Stop");
        tmp.put(4, "Exit");
        tmp.put(5, "Port");
        tmp.put(6, "Server IP");
        tmp.put(7, "Cannot fix the IP number");
        tmp.put(8, "Unknown");
        tmp.put(9, "Incorrect port number");
        tmp.put(10, "Server is ready to work on port");
        tmp.put(11, "is already in use");
        tmp.put(12, "Server error!");
        tmp.put(13, "Unknown command");
        tmp.put(14, "To");
        tmp.put(15, "From");
        tmp.put(16, "Connection interrupted.");
        tmp.put(17, "Port number is too long");
        tmp.put(18, "Socket timeout");
        tmp.put(19, "Data sent by client is incorrect");
        tmp.put(20, "Connection problem");
        tmp.put(21, "Can't break connection");
        tmp.put(22, "Input/Output Exception!");
        tmp.put(23, "Error getting file from server!");
        defaultText = Collections.unmodifiableMap(tmp);
    }

    /**
     * public config references to server application parameters
     */
    public static final int frameWidth;
    public static final int frameHeight;
    public static final int buttonWidth;
    public static final int buttonHeight;
    public static final int textFieldWidth;
    public static final int textFieldHeight;
    public static final int fontSize;
    public static final int timeout;
    public static int port;
    public static final Color[] fontColor;
    public static final Map<Integer, String> text;

    //setting public references
    static {
        text = defaultText;
        fontColor = defaultFontColor;
        frameWidth = setInt("frameWidth", defaultFrameWidth);
        frameHeight = setInt("frameHeight", defaultFrameHeight);
        buttonWidth = setInt("buttonWidth", defaultButtonWidth);
        buttonHeight = setInt("buttonHeight", defaultButtonHeight);
        textFieldWidth = setInt("textFieldWidth", defaultTextFieldWidth);
        textFieldHeight = setInt("textFieldHeight", defaultTextFieldHeight);
        fontSize = setInt("fontSize", defaultFontSize);
        port = setInt("defaultPort", defaultPort);
        timeout = setInt("socketTimeout", defaultTimeout);

        getHighScoresFromFile();
    }

    /**
     * level data - server sends this data to client when asked for level data
     */
    public static int nPoints;
    public static int[] xPoints;
    public static int[] yPoints;
    public static int[] xLandingSitePoints;
    public static int[] yLandingSitePoints;

    /**
     * loadLevelFromFile method - gets level data from level config file .lvl
     *
     * @param levelNumber - specifies which level should be loaded
     * @return true if level has been loaded, false otherwise
     */
    public static boolean loadLevelFromFile(int levelNumber) {
        String inputPath = levelFilesPath + levelNumber + ".lvl";
        try (InputStream input = Config.class.getResourceAsStream(inputPath)) {
            try (Scanner lvlScanner = new Scanner(input)) {
                try {
                    nPoints = Integer.parseInt(lvlScanner.next());
                    xPoints = new int[nPoints];
                    yPoints = new int[nPoints];
                    for (int i = 0; i < nPoints; i++) {
                        xPoints[i] = Integer.parseInt(lvlScanner.next());
                        yPoints[i] = Integer.parseInt(lvlScanner.next());
                    }

                    xLandingSitePoints = new int[4];
                    yLandingSitePoints = new int[4];
                    for (int i = 0; i < 4; i++) {
                        xLandingSitePoints[i] = Integer.parseInt(lvlScanner.next());
                        yLandingSitePoints[i] = Integer.parseInt(lvlScanner.next());
                    }
                } catch (NumberFormatException ex) {
                    Config.printError(ex, "Error: Wrong data in level config file!");
                    return false;
                }
            } catch (Exception ex) {
                Config.printError(ex, "Error: Couldn't read level file!");
                return false;
            }
        } catch (NullPointerException | IOException ex) {
            Config.printError(ex, "Error: Level file has not been found!");
            return false;
        }
        return true;
    }

    /**
     * setInt method - used to set values of public references
     *
     * @param parameter  - which application parameter should be loaded
     * @param defaultInt - default int value if getting from file failed
     * @return int value
     */
    private static int setInt(String parameter, int defaultInt) {
        String temp = fromConfigFile(parameter);
        if (temp == null) {
            return defaultInt;
        } else {
            int tempInt;
            try {
                tempInt = Integer.parseInt(temp);
            } catch (NumberFormatException ex) {
                printError(ex, "Error: Couldn't read" + parameter + "from server-config file!");
                return defaultInt;
            }
            return tempInt;
        }
    }

    /**
     * fromConfigFile method - gets given key from Properties file
     *
     * @param key - specifies which value should  be loaded
     * @return String value of gotten property
     */
    private static String fromConfigFile(String key) {
        return configFile.getProperty(key);
    }

    /**
     * saveScore method - saves given new score to HighScores class
     * @param score - given player's score
     * @param nick - given player's nick
     */
    public static void saveScore(double score, String nick) {
        highScores.highScores.put(score, nick);
    }

    /**
     * saveScore method - saves 20 best high scores to file
     */
    public static void saveHighScores() {
        try (FileWriter outputStream = new FileWriter(highscoresFileName)) {
            BufferedWriter out = new BufferedWriter(outputStream);
            int counter=0;
            for (Map.Entry<Double, String> entry : highScores.highScores.entrySet()) {
                counter++;
                if (counter < 20) {
                    out.write(String.valueOf(entry.getKey()));
                    out.newLine();
                    out.write(entry.getValue());
                    out.newLine();
                    out.flush(); // Flush the buffer and write all changes to the disk
                }
            }
            out.close();
        } catch (IOException ex) {
            Config.printError(ex, "Error: Couldn't save High Scores!");
        }
    }

    /**
     * getHighScores method - gets High Scores from highscores.hs file
     */
    public static void getHighScoresFromFile() {
        String nick;
        double score;

        try (FileReader inputStream = new FileReader(highscoresFileName)) {
            Scanner in = new Scanner(inputStream);

            while (in.hasNext()) {
                try {
                    score = Double.parseDouble(in.next());
                    nick = in.next();
                    highScores.highScores.put(score, nick);
                } catch (Exception ex) {
                    Config.printError(ex, "Error: Couldn't read from highscores.hs file");
                }
            }
            in.close();
        } catch (IOException ex) {
            Config.printError(ex, "Error: Couldn't load High Scores from file!");
        }
    }

    /**
     * printError method - prints errors in command line
     *
     * @param ex          - thrown Exception
     * @param description - description of error
     */
    public static void printError(Exception ex, String description) {
        System.err.println(ex + "\n" + description);
    }
}
