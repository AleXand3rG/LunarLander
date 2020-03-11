package main.java.com.pdag;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

/**
 * Client Config class - stores all configuration data used in application.
 * This class sets offline data basing on offline-config file.
 * It also tries to load online data if server is available. Otherwise, it loads offline data
 */
public class Config {

    /**
     * isOnline - defines if Config class should try to get resources from server
     */
    public static boolean isOnline = false;

    /**
     * configFile - Client Config File used to get offline data
     */
    private static final Properties configFile = new Properties();
    public static HighScores highScores = new HighScores();
    private static final String offlineConfigFileName = "offline-config.properties";

    //loading Config file form disc as a Properties file used later to get data from
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(offlineConfigFileName)) {

            if (input == null) {
                printError(new FileNotFoundException(), "Error: Couldn't find offline-config file!");
            } else {
                configFile.load(input);
            }
        } catch (IOException ex) {
            printError(ex, "Error: Couldn't load offline-config file!");
        }
    }

    /**
     * default data used when offline-config file has not been found
     */
    //not from server
    private static final String defaultTitle = "Lunar Lander";
    private static final String defaultSignature = "by PDAG";
    private static final String defaultIP = "localhost";
    private static final int defaultPort = 1000;
    private static final int defaultTimeout = 3000;

    private static final String defaultInstruction = "Press ENTER to start. Use your arrow keys as controls. Land safely!";
    private static final String defaultWinMessage = "Congratulations! Level completed!";
    private static final String defaultEndGameMessage = "Congratulations! You finished all levels! Enter your nickname:";
    private static final String defaultCreationFailedMessage = "Level creation failed! Application will try to reload first level.";
    private static final String defaultAboutError = "Error opening \"About\" web page!";
    private static final String defaultUserManualError = "Error opening user manual file!";
    private static final String defaultGetHighScoresError = "Error getting High Scores from Server!";

    private static final String defaultAboutURL = "https://gitlab-stud.elka.pw.edu.pl/agrzybow/lunarlander-client";
    private static final String defaultUserManualURL = "userManual.pdf";
    private static final String defaultIconURL = "/main/resources/img/icon.img";
    private static final String defaultBackgroundURL = "/main/resources/img/background.jpg";
    private static final String defaultOfflineLevelsURL = "/main/resources/lvl/lvl";
    private static final String defaultSpacecraftURL = "/main/resources/img/spacecraft720.png";
    private static final String defaultSpacecraftWithFlameURL = "/main/resources/img/spacecraft_with_flame720.png";
    private static final String defaultSpacecraftCrashedURL = "/main/resources/img/spacecraft_crashed720.png";
    private static final String defaultFont = "Courier New";

    /**
     * default data used when server not available and offline-config file has not been found
     */
    private static final int defaultPrefFrameWidth = 720;
    private static final int defaultPrefFrameHeight = 576;
    private static final int defaultDialogWidth = 300;
    private static final int defaultDialogHeight = 400;
    private static final int defaultLevelNumber = 1;
    private static final int defaultNumberOfLevels = 10;
    private static final int defaultSignatureX = 630;
    private static final int defaultSignatureY = 520;
    private static final int defaultInstructionX = 50;
    private static final int defaultInstructionY = 540;
    private static final int defaultScoreBoxX = 600;
    private static final int defaultTimeY = 20;
    private static final int defaultFuelY = 40;
    private static final int defaultScoreY = 60;
    private static final int defaultLevelCompletedY = 80;
    private static final int defaultFontSize = 15;
    private static final int defaultSpacecraftSize = 50;
    private static final int defaultThrust = 30;
    private static final int defaultRotation = 100;
    private static final int defaultNitro = 3; //thrust multiplier
    private static final int defaultBonusScore = 50;
    private static final int defaultCompletedLevelScore = 100;
    private static final int defaultFuelScoreMultiplier = 10;
    private static final int defaultUPS = 144; //updates per second
    private static final int defaultFPS = 144; //frames per second
    private static final double defaultFuel = 4; //plutonium oxide in kg
    private static final double defaultGravity = 5.625;
    private static final double defaultTimeForLevel = 60; //in seconds
    private static final double defaultRequiredSpeedX = 20;
    private static final double defaultRequiredSpeedY = 20;
    private static final double defaultRequiredRotation = 10;

    /**
     * public config references to application parameters
     */
    //editable
    public static String ip;
    public static int port;
    public static int timeout;
    public static int currentFrameWidth;
    public static int currentFrameHeight;
    public static int levelNumber;
    public static int nPoints;
    public static int[] xPoints;
    public static int[] yPoints;
    public static int[] xLandingSitePoints;
    public static int[] yLandingSitePoints;

    //final
    public static final String title;
    public static final String signature;
    public static final String instruction;
    public static final String winMessage;
    public static final String endGameMessage;
    public static final String creationFailedMessage;
    public static final String aboutError;
    public static final String userManualError;
    public static final String getHighScoresError;
    public static final String aboutURL;
    public static final String iconURL;
    public static final String userManualURL;
    public static final String backgroundURL;
    public static final String offlineLevelsURL;
    public static final String spacecraftURL;
    public static final String spacecraftWithFlameURL;
    public static final String spacecraftCrashedURL;
    public static final String font;
    public static int prefFrameWidth;
    public static int prefFrameHeight;
    public static int dialogWidth;
    public static int dialogHeight;
    public static int numberOfLevels;
    public static int signatureX;
    public static int signatureY;
    public static int instructionX;
    public static int instructionY;
    public static int xScoreBox;
    public static int timeY;
    public static int fuelY;
    public static int scoreY;
    public static int levelCompletedY;
    public static int fontSize;
    public static int spacecraftSize;
    public static int thrust;
    public static int rotation;
    public static int nitro;
    public static int bonusScore;
    public static int completedLevelScore;
    public static int fuelScoreMultiplier;
    public static int UPS;
    public static int FPS;
    public static double fuel;
    public static double gravity;
    public static double timeForLevel;
    public static double requiredSpeedX;
    public static double requiredSpeedY;
    public static double requiredRotation;
    public static final int[] bonusRequiredSpeed;

    //setting offline public references
    static {
        title = defaultTitle;
        signature = defaultSignature;
        ip = setOfflineString("ip", defaultIP);
        port = setOfflineInt("port", defaultPort);
        timeout = setOfflineInt("timeout", defaultTimeout);

        fontSize = setOfflineInt("fontSize", defaultFontSize);
        font = setOfflineString("font", defaultFont);
        instruction = setOfflineString("instruction", defaultInstruction);
        aboutURL = setOfflineString("aboutURL", defaultAboutURL);
        winMessage = setOfflineString("winMessage", defaultWinMessage);
        endGameMessage = setOfflineString("endGameMessage", defaultEndGameMessage);
        aboutError = setOfflineString("aboutError", defaultAboutError);
        userManualError = setOfflineString("userManualError", defaultUserManualError);
        getHighScoresError = setOfflineString("getHighScoresError", defaultGetHighScoresError);
        creationFailedMessage = setOfflineString("creationFailedMessage", defaultCreationFailedMessage);
        iconURL = setOfflineString("iconURL", defaultIconURL);
        userManualURL = setOfflineString("userManualURL", defaultUserManualURL);
        backgroundURL = setOfflineString("backgroundURL", defaultBackgroundURL);
        offlineLevelsURL = setOfflineString("offlineLevelsURL", defaultOfflineLevelsURL);
        spacecraftURL = setOfflineString("spacecraftURL", defaultSpacecraftURL);
        spacecraftWithFlameURL = setOfflineString("spacecraftWithFlameURL", defaultSpacecraftWithFlameURL);
        spacecraftCrashedURL = setOfflineString("spacecraftCrashedURL", defaultSpacecraftCrashedURL);
    }

    //setting online public references
    static {
        updateOnlineData();

        bonusRequiredSpeed = new int[numberOfLevels];
        for (int i = 0; i < bonusRequiredSpeed.length; i++) {
            bonusRequiredSpeed[i] = 10 - i;
        }

        //getting level data
        getLevel();
    }

    /**
     * updateOnlineData method - sets all online data and updates them
     */
    public static void updateOnlineData() {
        prefFrameWidth = setInt("prefFrameWidth", defaultPrefFrameWidth);
        prefFrameHeight = setInt("prefFrameHeight", defaultPrefFrameHeight);
        currentFrameWidth = prefFrameWidth;
        currentFrameHeight = prefFrameHeight;
        dialogWidth = setInt("dialogWidth", defaultDialogWidth);
        dialogHeight = setInt("dialogHeight", defaultDialogHeight);

        levelNumber = setInt("initLevelNumber", defaultLevelNumber);
        numberOfLevels = setInt("numberOfLevels", defaultNumberOfLevels);
        signatureX = setInt("signatureX", defaultSignatureX);
        signatureY = setInt("signatureY", defaultSignatureY);
        instructionX = setInt("instructionX", defaultInstructionX);
        instructionY = setInt("instructionY", defaultInstructionY);
        xScoreBox = setInt("xScoreBox", defaultScoreBoxX);
        timeY = setInt("timeY", defaultTimeY);
        fuelY = setInt("fuelY", defaultFuelY);
        scoreY = setInt("scoreY", defaultScoreY);
        spacecraftSize = setInt("preferredSpacecraftSize", defaultSpacecraftSize);
        thrust = setInt("thrust", defaultThrust);
        rotation = setInt("rotation", defaultRotation);
        nitro = setInt("nitro", defaultNitro);
        bonusScore = setInt("bonusScore", defaultBonusScore);
        completedLevelScore = setInt("completedLevelScore", defaultCompletedLevelScore);
        fuelScoreMultiplier = setInt("fuelScoreMultiplier", defaultFuelScoreMultiplier);
        levelCompletedY = setInt("levelCompletedY", defaultLevelCompletedY);
        UPS = setInt("UPS", defaultUPS);
        FPS = setInt("FPS", defaultFPS);

        fuel = setDouble("fuel", defaultFuel);
        gravity = setDouble("gravity", defaultGravity);
        timeForLevel = setDouble("levelTime", defaultTimeForLevel);
        requiredSpeedX = setDouble("requiredSpeedX", defaultRequiredSpeedX);
        requiredSpeedY = setDouble("requiredSpeedY", defaultRequiredSpeedY);
        requiredRotation = setDouble("requiredRotation", defaultRequiredRotation);
    }

    /**
     * public graphics references
     */
    public static final Image icon;
    public static BufferedImage background;
    public static BufferedImage spacecraft;
    public static BufferedImage spacecraftWithFlame;
    public static BufferedImage spacecraftCrashed;

    //loading from files and setting up graphics
    static {
        icon = Toolkit.getDefaultToolkit().getImage(iconURL);

        //background
        try {
            URL url = Config.class.getResource(backgroundURL);
            background = ImageIO.read(url);
        } catch (IOException ex) {
            printError(ex, "Error: Couldn't find background file!");
        }

        //spacecraft
        try {
            spacecraft = ImageIO.read(Config.class.getResource(Config.spacecraftURL));
            spacecraftWithFlame = ImageIO.read(Config.class.getResource(spacecraftWithFlameURL));
            spacecraftCrashed = ImageIO.read(Config.class.getResource(spacecraftCrashedURL));
        } catch (Exception ex) {
            printError(ex, "Error: Couldn't find spacecraft file!");
        }
    }

    /**
     * getLevel method - if isOnline parameter is true, tries to get level from server.
     * If not or if can't get level from server, it tries to load offline level from disc.
     * If level is missing or something is wrong with the level, it tries to load first level.
     * If still first level can't be loaded, application closes.
     */
    public static void getLevel() {
        boolean isCreated = false;

        if (isOnline) {
            try {
                isCreated = Client.downloadLevel();
            } catch (IOException | ClassNotFoundException ex) {
                Config.printError(ex, "Error: Couldn't download level from server!");
            }
        }

        if (!isCreated) {
            isCreated = loadOfflineLevel();
        }

        if (!isCreated) {
            JOptionPane.showConfirmDialog(Game.mainWindow, Config.creationFailedMessage, null, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            Config.levelNumber = 1;
            isCreated = Config.loadOfflineLevel();
        }

        if (!isCreated) {
            System.exit(-1);
        }
    }

    /**
     * getHighScores method - tries to download serialized high scores data from server
     */
    public static void getHighScores() {
        try {
            Client.getHighScores();
        } catch (Exception ex) {
            Config.printError(ex, "Error: Couldn't get High Scores from server!");
        }
    }

    /**
     * loadOfflineLevel method - gets level data from level config file .lvl
     *
     * @return true if level has been loaded, false otherwise
     */
    public static boolean loadOfflineLevel() {
        String inputPath = Config.offlineLevelsURL + Config.levelNumber + ".lvl";
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
     * setOfflineString method - used to set offline String values of public references
     *
     * @param parameter     - which application parameter should be loaded
     * @param defaultString - default String value if getting from file failed
     * @return String value
     */
    private static String setOfflineString(String parameter, String defaultString) {
        String temp;
        temp = getFromFile(parameter);
        if (temp == null) {
            return defaultString;
        } else {
            return temp;
        }
    }

    /**
     * setOfflineInt method - used to set offline int values of public references
     *
     * @param parameter  - which application parameter should be loaded
     * @param defaultInt - default int value if getting from file failed
     * @return int value
     */
    private static int setOfflineInt(String parameter, int defaultInt) {
        int integer;
        String temp;
        temp = getFromFile(parameter);
        if (temp == null) {
            return defaultInt;
        } else {
            try {
                integer = Integer.parseInt(temp);
            } catch (Exception ex) {
                printError(ex, "Error: Couldn't read" + parameter + "from offline-config file!");
                return defaultInt;
            }
            return integer;
        }
    }

    /**
     * setOfflineDouble method - used to set offline double values of public references
     *
     * @param parameter     - which application parameter should be loaded
     * @param defaultDouble - default double value if getting from file failed
     * @return double value
     */
    private static double setOfflineDouble(String parameter, double defaultDouble) {
        double value;
        String temp;
        temp = getFromFile(parameter);
        if (temp == null) {
            return defaultDouble;
        } else {
            try {
                value = Double.parseDouble(temp);
            } catch (Exception ex) {
                printError(ex, "Error: Couldn't read" + parameter + "from offline-config file!");
                return defaultDouble;
            }
            return value;
        }
    }

    /**
     * setInt method - used to set int values of public references
     *
     * @param parameter  - which application parameter should be loaded
     * @param defaultInt - default int value if getting from server or offline-config file failed
     * @return int value
     */
    private static int setInt(String parameter, int defaultInt) {
        String temp;
        if (isOnline) {
            temp = getFromServer(parameter);
            if (temp == null) {
                return setOfflineInt(parameter, defaultInt);
            } else {
                return Integer.parseInt(temp);
            }
        } else {
            return setOfflineInt(parameter, defaultInt);
        }
    }

    /**
     * setDouble method - used to set double values of public references
     *
     * @param parameter     - which application parameter should be loaded
     * @param defaultDouble - default double value if getting from server or offline-config file failed
     * @return double value
     */
    private static double setDouble(String parameter, double defaultDouble) {
        String temp;
        if (isOnline) {
            temp = getFromServer(parameter);
            if (temp == null) {
                return setOfflineDouble(parameter, defaultDouble);
            } else {
                return Double.parseDouble(temp);
            }
        } else {
            return setOfflineDouble(parameter, defaultDouble);
        }
    }

    /**
     * getFromServer method - tries to get asked parameter from server
     *
     * @param parameter - which application parameter should be loaded
     * @return String value of gotten property
     */
    private static String getFromServer(String parameter) {
        String tmp;
        try {
            tmp = Client.getPropertyFromServer(parameter);
        } catch (Exception ex) {
            Config.printError(ex, "Error: Couldn't get data from server!");
            return null;
        }
        return tmp;
    }

    /**
     * getFromFile method - gets given key from Properties file
     *
     * @param key - specifies which value should  be loaded
     * @return String value of gotten property
     */
    private static String getFromFile(String key) {
        return configFile.getProperty(key);
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