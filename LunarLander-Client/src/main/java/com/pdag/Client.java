package main.java.com.pdag;

import java.io.*;
import java.net.Socket;

/**
 * Client class - creates Socket and establishes connection with server
 */
public class Client {

    /**
     * connection method - creates new instance of Socket, Print Writer and Buffered Reader to communicate with server
     *
     * @param protocol - message/command to be sent to server
     * @return String value of gotten server answer
     * @throws IOException - method throws IOException due to socket handling
     */
    private static String connection(String protocol) throws IOException {
        Socket socket = new Socket(Config.ip, Config.port);
        socket.setSoTimeout(Config.timeout);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        pw.println(protocol);

        String answer;
        answer = br.readLine();
        if (answer.equals(Protocol.ERROR)) {
            throw new IOException("Error: Server error!");
        }
        socket.close();
        return answer;
    }

    /**
     * getPropertyFromServer method - tries to get asked property from server, returns null when couldn't
     *
     * @param parameter - which application parameter should be downloaded
     * @return String value of gotten parameter
     */
    public static String getPropertyFromServer(String parameter) {
        String answer;
        try {
            answer = connection(Protocol.GET_CONFIG_DATA + parameter);
        } catch (IOException ex) {
            Config.printError(ex, "Error: Couldn't connect to server!");
            return null;
        }
        return answer;
    }

    /**
     * downloadLevel method - tries to download serialized level data from server
     *
     * @return true if level has been downloaded, false otherwise
     * @throws ClassNotFoundException if levelData can't be initialized with gotten object, because of being of different class
     * @throws IOException - method throws IOException due to socket handling
     */
    public static boolean downloadLevel() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(Config.ip, Config.port);
        socket.setSoTimeout(Config.timeout);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);

        pw.println(Protocol.GET_LEVEL + Config.levelNumber);

        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        LevelData levelData = (LevelData) is.readObject();
        socket.close();
        Config.nPoints = levelData.nPoints;
        Config.xPoints = levelData.xPoints;
        Config.yPoints = levelData.yPoints;
        Config.xLandingSitePoints = levelData.xLandingSitePoints;
        Config.yLandingSitePoints = levelData.yLandingSitePoints;
        return Config.nPoints != 0;
    }

    /**
     * getHighScores method - tries to download serialized high scores data from server
     *
     * @throws ClassNotFoundException if levelData can't be initialized with gotten object, because of being of different class
     * @throws IOException - method throws IOException due to socket handling
     */
    public static void getHighScores() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(Config.ip, Config.port);
        socket.setSoTimeout(Config.timeout);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);

        pw.println(Protocol.GET_HIGHSCORES);
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        Config.highScores = (HighScores) is.readObject();
        socket.close();
    }

    /**
     * senScore method - tries to send current score data to server
     *
     * @param nick  - player's nick
     * @param score - player's score
     */
    public static void sendScore(String nick, double score) {
        try {
            Socket socket = new Socket(Config.ip, Config.port);
            socket.setSoTimeout(Config.timeout);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println(Protocol.NEW_HIGHSCORE);
            pw.println(nick);
            pw.println(score);
            socket.close();
        } catch (IOException ex) {
            Config.printError(ex, "Error: Couldn't send new High Score!");
        }
    }
}


