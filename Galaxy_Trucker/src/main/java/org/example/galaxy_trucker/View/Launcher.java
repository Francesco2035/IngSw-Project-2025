package org.example.galaxy_trucker.View;

import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.ServersHandler;

/**
 * The Launcher class serves as the entry point for the application, providing
 * functionality to initialize the application in either server or client mode.
 * The mode is determined by the first argument provided to the program.
 *
 * Main functionalities:
 * - Server mode: Launches the server processes and handles incoming connections.
 * - Client mode: Initializes and runs the client-side application.
 *
 * If no arguments are provided or an unsupported mode is specified, the program
 * outputs usage instructions and terminates.
 */
public class Launcher {

    /**
     * The main method serves as the entry point for the application. It initializes
     * the program in either "server" or "client" mode based on the provided command-line
     * arguments. If no arguments are specified or an unknown mode is provided, the method
     * outputs usage instructions and terminates the program.
     *
     * @param args Command-line arguments to determine the mode of the application.
     *             The first argument must be either "server" or "client". Optional
     *             additional arguments may be provided for client mode.
     * @throws Exception If an error occurs during the execution of the server or client mode.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java -jar app.jar <server|client> [CLIENT ARGUMENTS...]");
            System.exit(1);
        }

        String mode = args[0].toLowerCase();

        switch (mode) {
            case "server":
                ServersHandler serversHandler = new ServersHandler();
                serversHandler.run();
                break;
            case "client":
                Client client = new Client();
                //String[] clientArgs = Arrays.copyOfRange(args, 1, args.length);
                client.run();
                break;
            default:
                System.err.println("Unknown mode: " + mode);
                System.err.println("Usage: java -jar app.jar <server|client> [CLIENT ARGUMENTS...]");
                System.exit(1);
        }
    }

}
