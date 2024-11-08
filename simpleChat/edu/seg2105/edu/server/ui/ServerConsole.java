package edu.seg2105.edu.server.ui;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {
    // Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5555;
    
    // Instance variables **********************************************

    /**
     * The instance of the server that created this ConsoleChat.
     */
    EchoServer server;

    /**
     * Scanner to read from the console
     */
    Scanner fromConsole;
    
    // Constructors ****************************************************

    /**
     * Constructs an instance of the ServerConsole UI.
     *
     * @param port The port to connect on.
     */
    public ServerConsole(int port) {
        try {
            server = new EchoServer(port, this);
            
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }

        // Create scanner object to read from console
        fromConsole = new Scanner(System.in);
    }

    // Instance methods ************************************************

    /**
     * This method waits for input from the console. Once it is received, it sends
     * it to the client's message handler.
     */
    public void accept() {
        try {

            String message;

            while (true) {
                message = fromConsole.nextLine();
                server.handleMessageFromServerUI(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    /**
     * This method overrides the method in the ChatIF interface. It displays a
     * message onto the screen.
     *
     * @param message The string to be displayed.
     */
    @Override
    public void display(String message) {
        System.out.println(message);
    }

    // Class methods ***************************************************

    /**
     * This method is responsible for the creation of the server instance (there is
     * no UI in this phase).
     *
     * @param args[0] The port number to listen on. Defaults to 5555 if no argument
     *                is entered.
     */
    public static void main(String[] args) {
        int port = 0; // Port to listen on

        try {
            port = Integer.parseInt(args[0]); // Get port from command line
        } catch (Throwable t) {
            port = DEFAULT_PORT; // Set port to 5555
        }
        
        ServerConsole chat = new ServerConsole(port);
        chat.accept(); // Wait for console data
    }

}
