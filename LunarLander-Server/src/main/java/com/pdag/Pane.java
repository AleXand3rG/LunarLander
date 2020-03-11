package main.java.com.pdag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Server Pane class - defines how server application looks like, creates the whole GUI
 *
 * @see JPanel
 */
public class Pane extends JPanel {

    /**
     * public mainPane - main application module. User can see what is happening with the server
     */
    public static Pane mainPane;

    /**
     * creating GUI components
     */
    private JPanel infoPanel = new JPanel();
    private JButton startButton = new JButton(Config.text.get(2));
    private JButton exitButton = new JButton(Config.text.get(4));
    private JTextField portNumberTextField = new JTextField(Integer.toString(Config.port));
    private boolean isOnline = false;
    private Connection connection;

    /**
     * Pane class constructor - creates and sets JPanel
     */
    public Pane() {
        mainPane = this;

        //creating GUI elements
        JPanel buttonsPanel = new JPanel();
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel portNumberPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(infoPanel);

        //setting layouts
        setLayout(new BorderLayout());
        southPanel.setLayout(new BorderLayout());
        northPanel.setLayout(new BorderLayout());
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        //setting Start Button
        startButton.setFocusable(false);
        startButton.setPreferredSize(new Dimension(Config.buttonWidth, Config.buttonHeight));

        //setting Exit Button
        exitButton.setFocusable(false);
        exitButton.setPreferredSize(new Dimension(Config.buttonWidth, Config.buttonHeight));

        //setting Port Number Text Field
        portNumberTextField.setPreferredSize(new Dimension(0, Config.textFieldHeight));
        portNumberTextField.setColumns(Config.textFieldWidth);
        portNumberTextField.setSize(new Dimension(0, Config.textFieldHeight));

        //setting IP
        JLabel IP;
        try {
            IP = new JLabel("   " + Config.text.get(6) + ":   " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            addMessage(Config.text.get(7), ERROR, true);
            IP = new JLabel("   " + Config.text.get(6) + ":   " + Config.text.get(8));
        }
        IP.setForeground(Config.fontColor[4]);
        IP.setFont(IP.getFont().deriveFont(Config.fontSize));

        //setting Port Text
        JLabel portText = new JLabel(Config.text.get(5) + ": ");
        portText.setForeground(Config.fontColor[4]);
        portText.setFont(IP.getFont().deriveFont(Config.fontSize));

        //adding components to panels
        buttonsPanel.add(startButton);
        buttonsPanel.add(exitButton);

        portNumberPanel.add(portText);
        portNumberPanel.add(portNumberTextField);

        southPanel.add(buttonsPanel, BorderLayout.EAST);
        northPanel.add(IP, BorderLayout.WEST);
        northPanel.add(portNumberPanel, BorderLayout.EAST);

        //adding sub-panels to main JPanel
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(northPanel, BorderLayout.NORTH);

        //adding Action Listeners to buttons
        exitButton.addActionListener(ae -> System.exit(0));

        startButton.addActionListener(ae -> {

            if (isOnline) {
                connection.setListening(false);
                connection.interrupt();
                setEnabled();
            } else {
                try {
                    Config.port = Integer.parseInt(portNumberTextField.getText());
                    startButton.setText(Config.text.get(3));
                    portNumberTextField.setEnabled(false);
                    isOnline = true;
                    try {
                        connection = new Connection();
                        connection.setListening(true);
                    } catch (Exception ex) {
                        addMessage(Config.text.get(20), Config.ERROR, true);
                    }
                } catch (NumberFormatException ex) {
                    addMessage(Config.text.get(9), Config.ERROR, true);
                }
            }
        });

        infoPanel.addContainerListener(new ContainerListener() {
            public void componentAdded(ContainerEvent e) {
                scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
            }

            public void componentRemoved(ContainerEvent e) {
            }
        });
    }

    /**
     * addMessage method - adds messages to mainPane, so that user could see what's happening
     *
     * @param message - message text to be displayed
     * @param status  - status of message (INFO = 0, SENT = 1, RECEIVED = 2, ERROR = 3)
     * @param isTime  - defines if message should contain current time
     */
    public void addMessage(String message, int status, boolean isTime) {
        JLabel label;
        if (isTime) {
            label = new JLabel(" [" + DateFormat.getDateTimeInstance().format(new Date()) + "] " + message);
        } else {
            label = new JLabel(message);
        }
        label.setFont(label.getFont().deriveFont(Config.fontSize));
        label.setForeground(Config.fontColor[status]);
        infoPanel.add(label);
        infoPanel.revalidate();
    }

    /**
     * setEnabled method - sets application GUI looking "enabled"
     */
    public void setEnabled() {
        startButton.setText(Config.text.get(2));
        exitButton.setText(Config.text.get(4));
        portNumberTextField.setEnabled(true);
        isOnline = false;
    }
}