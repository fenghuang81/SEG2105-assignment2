// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
    // Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
    ChatIF clientUI;
    
    /**
     * The id of the client
     */
    String loginid;

    // Constructors ****************************************************

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */

    public ChatClient(String loginid, String host, int port, ChatIF clientUI) throws IOException {
        super(host, port); // Call the superclass constructor
        this.loginid = loginid;
        this.clientUI = clientUI;
        openConnection();
    }

    // Instance methods ************************************************

    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    @Override
    public void handleMessageFromServer(Object msg) {
        clientUI.display(msg.toString());

    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(String message) {
        try {
            if (message.startsWith("#")) {
                handleCommand(message);
            } else {
                sendToServer(message);
            }
        } catch (IOException e) {
            clientUI.display("Could not send message to server.  Terminating client.");
            quit();
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
            quit();
        } else if (command.startsWith("#logoff")) {
            try {
                closeConnection();
            } catch (IOException e) {
            }
        } else if (command.startsWith("#sethost")) {
            if (!isConnected()) {
                setHost(command.split("\\s")[1]);
            } else {
                clientUI.display("Error, client is still logged in");
            }
        } else if (command.startsWith("#setport")) {
            if (!isConnected()) {
                setPort(Integer.parseInt(command.split("\\s")[1]));
            } else {
                clientUI.display("Error, client is still logged in");
            }
        } else if (command.startsWith("#login")) {
            if (!isConnected()) {
                try {
                    openConnection();
                } catch (IOException e) {
                }
            } else {
                clientUI.display("Error, client is still logged in");
            }
        } else if (command.startsWith("#gethost")) {
            clientUI.display(getHost());
        } else if (command.startsWith("#getport")) {
            clientUI.display(String.valueOf(getPort()));
        } else {
            clientUI.display("Invalid command");
        }

    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }

    /**
     * Implements the hook method called each time an exception is thrown by the
     * client's thread that is waiting for messages from the server. The method may
     * be overridden by subclasses.
     * 
     * @param exception the exception raised.
     */
    @Override
    protected void connectionException(Exception exception) {
        clientUI.display("The server has shut down");
        quit();

    }

    /**
     * Implements the hook method called after the connection has been closed. The
     * default implementation does nothing. The method may be overridden by
     * subclasses to perform special processing such as cleaning up and terminating,
     * or attempting to reconnect.
     */
    @Override
    protected void connectionClosed() {
        clientUI.display("Connection is closed");
    }
    
    /**
     * Implements the hook method called after a connection has been established. The default
     * implementation does nothing. It may be overridden by subclasses to do
     * anything they wish.
     */
    @Override
    protected void connectionEstablished() {
        try {
            sendToServer("#login " + loginid);
            System.out.println(loginid + " has logged on.");
        } catch (IOException e) {
        }
    }
}
//End of ChatClient class
