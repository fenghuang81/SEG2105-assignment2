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
    // Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
    ChatIF serverUI;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to listen from.
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
        this.sendToAllClients(msg);
    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromServerUI(String message) {
        if (message.startsWith("#")) {
//                handleCommand(message);
        } else {
            serverUI.display(message);
            sendToAllClients("SERVER MSG> " + message);
        }
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
}
//End of EchoServer class
