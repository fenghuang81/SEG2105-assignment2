package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;

import edu.seg2105.client.common.ChatIF;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
    // Class variables *************************************************
    
    /**
     * The key for getting a client's loginid
     */
    String loginKey = "loginid";

    // Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
    ChatIF serverUI;

    /**
     * Whether closed() has been called (which sets the socket to null)
     */
    boolean isClosed;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port     The port number to listen from.
     * @param clientUI The interface type variable.
     */
    public EchoServer(int port, ChatIF serverUI) throws IOException {
        super(port); // Call the superclass constructor
        this.serverUI = serverUI;
        listen(); // Start listening for connections
    }

    // Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
    @Override
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);

        String msgStr = (String) msg;

        // Check whether to echo or not
        if (msgStr.startsWith("#login")) {
            // If already logged in
            if (client.getInfo(loginKey) != null) {
                try { // Send an error message to the client and terminate the connection
                    client.sendToClient("Error, already logged in. Terminating connection.");
                    client.close();
                } catch (IOException e) {
                }
            }
            // Set the loginid key's value (2nd item from split)
            String loginid = msgStr.split("\\s")[1];
            client.setInfo(loginKey, loginid);
        } else {
            // Echo the message with their loginid prefixed
            String loginid = (String) client.getInfo(loginKey);
            this.sendToAllClients(loginid + "> " + msgStr);
        }
    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromServerUI(String message) {
        if (message.startsWith("#")) {
            handleCommand(message);
        } else {
            serverUI.display(message);
            sendToAllClients("SERVER MSG> " + message);
        }
    }

    /**
     * Handles a command (prefix of "#") that a user may input.
     * 
     * @param command The command to be processed.
     */
    private void handleCommand(String command) {
        // Check what command is being called
        if (command.startsWith("#quit")) {
            // Closes and exits
            quit();
        } else if (command.startsWith("#stop")) {
            // Stops listening for new connections
            stopListening();
        } else if (command.startsWith("#close")) {
            // Closes (which stops listening and disconnects all clients)
            try {
                close();
            } catch (IOException e) {
            }
        } else if (command.startsWith("#setport")) {
            if (isClosed) {
                setPort(Integer.parseInt(command.split("\\s")[1]));
            } else {
                serverUI.display("Error, server has not been closed.");
            }
        } else if (command.startsWith("#start")) {
            if (!isListening()) {
                try {
                    listen();
                } catch (IOException e) {
                }
            } else {
                serverUI.display("Error, server has not been stopped.");
            }
        } else if (command.startsWith("#getport")) {
            serverUI.display(String.valueOf(getPort()));
        } else {
            serverUI.display("Invalid command");
        }

    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            close();
        } catch (IOException e) {
        }
        System.exit(0);
    }

    /**
     * This method overrides the one in the superclass. Called when the server
     * starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass. Called when the server stops
     * listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    /**
     * Implements the hook method called each time a new client connection is
     * accepted. The default implementation does nothing.
     * 
     * @param client the connection connected to the client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println(client + " has connected");
    }

    /**
     * Implements the hook method called each time a client disconnects. The default
     * implementation does nothing. The method may be overridden by subclasses but
     * should remains synchronized.
     *
     * @param client the connection with the client.
     */
    @Override
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        System.out.println(client + " has disconnected");
    }

    /**
     * Implements the hook method called when the server is closed. The default
     * implementation does nothing. This method may be overridden by subclasses.
     * When the server is closed while still listening, serverStopped() will also be
     * called.
     */
    @Override
    protected void serverClosed() {
        isClosed = true;
    }
}
//End of EchoServer class
