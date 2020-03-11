package main.java.com.pdag;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Server GameData class - creates new Socket when connection is established and communicates with client application
 * This class sends and receives all game data from ond to client application
 */
public class GameData extends Thread {

    /**
     * bufferedReader - used to read incoming data
     * printWriter - used to send data to client
     * objectWriter - used to send serialized objects to client
     */
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private ObjectOutputStream objectWriter;

    /**
     * Properties file where application gets data from
     */
    private Properties lunarConfigFile = new Properties();

    /**
     * GameData constructor - initializes attributes, loads client-config file and starts new Thread
     *
     * @throws IOException - might
     * @param socket - socked connected to client
     */
    public GameData(Socket socket) throws IOException {
        this.socket = socket;
        InputStream inputStream = socket.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        OutputStream outputStream = socket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);

        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(Config.lunarConfigFileName)) {
            if (input == null) {
                Config.printError(new FileNotFoundException(), "Error: Couldn't find client-config file!");
            } else {
                lunarConfigFile.load(input);
            }
        } catch (IOException ex) {
            Config.printError(ex, "Error: Couldn't load client-config file!");
        }

        start();
    }

    /**
     * fromFile method - gets asked data from Properties file
     *
     * @param key - specifies which value should  be loaded
     * @return String value of gotten property
     */
    private String fromFile(String key) {
        return lunarConfigFile.getProperty(key);
    }

    /**
     * Thread override run method - communicates with client: reads and writes Protocol and executes commands
     * Also adds messages to Pane.mainPane
     *
     * @see Pane
     */
    @Override
    public void run() {

        String command = null;
        try {
            command = bufferedReader.readLine();
        } catch (IOException ex) {
            Config.printError(ex, "Error: Couldn't read data!");
        }
        Pane.mainPane.addMessage(Config.text.get(15) + " "
                + socket.getInetAddress().getHostAddress() + ": "
                + command, Config.RECEIVED, true);
        if (command != null) {

            if (command.contains(Protocol.GET_CONFIG_DATA)) {
                int index = command.indexOf(":");
                String cmd = command.substring(index + 2);
                String answer = fromFile(cmd);
                printWriter.println(answer);
                Pane.mainPane.addMessage(Config.text.get(15) + " "
                        + socket.getInetAddress().getHostAddress() + ": "
                        + Protocol.DATA_SENT, Config.RECEIVED, true);
            } else if (command.contains(Protocol.GET_LEVEL)) {
                int index = command.indexOf(":");
                int levelNumber = Integer.parseInt(command.substring(index + 2));

                boolean isCreated;
                isCreated = Config.loadLevelFromFile(levelNumber);
                if (isCreated) {
                    LevelData levelData = new LevelData();
                    try {
                        objectWriter = new ObjectOutputStream(socket.getOutputStream());
                    } catch (IOException ex) {
                        Config.printError(ex, "Error: Couldn't create objectWriter!");
                    }
                    try {
                        objectWriter.writeObject(levelData);
                    } catch (IOException ex) {
                        Config.printError(ex, "Error: Couldn't send level data!");
                    }
                }
            } else {

                switch (command) {

                    case Protocol.CHECK: {
                        printWriter.println(Protocol.CHECKED);
                        Pane.mainPane.addMessage(Config.text.get(14) + " "
                                + socket.getInetAddress().getHostAddress() + ": "
                                + Protocol.CHECKED, Config.SENT, true);
                        break;
                    }

                    case Protocol.NEW_HIGHSCORE: {
                        double score = 0;
                        String nick = null;
                        try {
                            nick = bufferedReader.readLine();
                        } catch (IOException ex) {
                            Config.printError(ex, "Error: Couldn't read data!");
                        }
                        try {
                            score = Double.parseDouble(bufferedReader.readLine());
                        } catch (IOException ex) {
                            Config.printError(ex, "Error: Couldn't read data!");
                        }
                        Config.saveScore(score, nick);
                        Config.saveHighScores();
                        break;
                    }

                    case Protocol.GET_HIGHSCORES: {
                        try {
                            objectWriter = new ObjectOutputStream(socket.getOutputStream());
                        } catch (IOException ex) {
                            Config.printError(ex, "Error: Couldn't create objectWriter!");
                        }
                        try {
                            objectWriter.writeObject(Config.highScores);
                        } catch (IOException ex) {
                            Config.printError(ex, "Error: Couldn't send level data!");
                        }
                        break;
                    }

                    case Protocol.CONNECT: {
                        printWriter.println(Protocol.CONNECTED);
                        Pane.mainPane.addMessage(Config.text.get(14) + " "
                                + socket.getInetAddress().getHostAddress() + ": "
                                + Protocol.CONNECTED, Config.SENT, true);
                        break;
                    }

                    case Protocol.CLOSE_CONNECTION: {
                        printWriter.println(Protocol.CONNECTION_CLOSED);
                        Pane.mainPane.addMessage(Config.text.get(14) + " "
                                + socket.getInetAddress().getHostAddress() + ": "
                                + Protocol.CONNECTION_CLOSED, Config.SENT, true);
                        break;
                    }

                    default: {
                        printWriter.println(Protocol.ERROR);
                        Pane.mainPane.addMessage(Config.text.get(12), Config.ERROR, true);
                        Pane.mainPane.addMessage(Config.text.get(13) + " "
                                + socket.getInetAddress().getHostAddress() + ": "
                                + Protocol.ERROR, Config.SENT, true);
                        break;
                    }
                }
                try {
                    socket.close();
                } catch (IOException ex) {
                    Config.printError(ex, Config.text.get(22));
                }
            }
        }
    }
}
