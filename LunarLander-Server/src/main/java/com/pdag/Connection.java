package main.java.com.pdag;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server Connection class - creates ServerSocket and establishes connection with clients
 */
public class Connection extends Thread {

    /**
     * if isOnline is true - server tries to get new GameData
     */
    private ServerSocket serverSocket;
    private boolean isOnline;
    private boolean isListening; //used to interrupt connection without error

    /**
     * Connection class constructor
     * sets isOnline and isListening true by default and starts Thread reading GameData
     */
    public Connection() {
        isOnline = true;
        isListening = true;
        start();
    }

    /**
     * setListening method - defines if Server should be opened for new connections
     *
     * @param value - true or false
     */
    public void setListening(boolean value) {
        isListening = value;
    }

    /**
     * Thread override run method - creates new ServerSocket and tries to establish new connection while online
     * If new connection established, creates GameData instance to communicate with client
     *
     * @see GameData
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Config.port);
            Pane.mainPane.addMessage(Config.text.get(10) + " " + Config.port, Config.INFO, true);
        } catch (IOException ex) {
            Pane.mainPane.addMessage(Config.text.get(5) + " " + Config.port + " " + Config.text.get(11), Config.ERROR, true);
            Pane.mainPane.setEnabled();
            return;
        } catch (IllegalArgumentException e) {
            Pane.mainPane.addMessage(Config.text.get(17), Config.ERROR, true);
            Pane.mainPane.setEnabled();
            return;
        }

        while (isOnline) {
            try {
                new GameData(serverSocket.accept());
            } catch (Exception ex) {
                if (isListening) {
                    Pane.mainPane.addMessage(Config.text.get(20), Config.ERROR, true);
                } else {
                    Pane.mainPane.addMessage(Config.text.get(16), Config.INFO, true);
                }
            }
        }
    }

    /**
     * Thread override interrupt method - interrupts thread stopping server connection
     */
    @Override
    public void interrupt() {
        try {
            serverSocket.close();
            isOnline = false;
        } catch (IOException ex) {
            Pane.mainPane.addMessage(Config.text.get(21), Config.ERROR, true);
        }
    }
}