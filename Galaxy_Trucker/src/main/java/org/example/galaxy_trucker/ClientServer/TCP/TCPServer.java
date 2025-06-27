package org.example.galaxy_trucker.ClientServer.TCP;

import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The TCPServer class is responsible for managing and running a TCP server that listens for client connections
 * and handles communication with clients using multi-threaded client handlers.
 * It maintains a map of client tokens, disconnected clients, and active threads for clients.
 *
 * This server listens on a specific TCP port defined in the Settings class and accepts incoming client connections.
 * For each client connection, a new thread is created to handle communication and interaction
 * with the client via the MultiClientHandler class.
 *
 * The TCPServer class also manages the lifecycle of client connections and provides functionality to remove
 * and clean up resources associated with a specific client when necessary.
 */
public class TCPServer implements  Runnable {

    /**
     * A static instance of the GamesHandler used for managing game-related operations
     * and logic within the server. This variable serves as the main access point
     * for game handling functionalities, providing centralized management of
     * game sessions, players, and associated interactions.
     *
     * The GamesHandler instance is shared across multiple threads within the
     * TCP server to synchronize game state and handle client communications.
     */
    private static GamesHandler gamesHandler;
    /**
     * A thread-safe map that associates unique client tokens (represented as Strings) with their corresponding
     * VirtualView instances. This map is primarily used to manage client connections in the TCPServer
     * and to provide an efficient way to retrieve and handle virtual views for each client using their token.
     */
    private ConcurrentHashMap<String, VirtualView> tokenMap;
    /**
     * A list of tokens representing clients that have disconnected from the server.
     * This field is utilized by the {@code TCPServer} to track and manage the clients
     * that have been disconnected, ensuring proper handling and resource management
     * within the server.
     *
     * It is initialized during the construction of the {@code TCPServer} instance.
     * The list is used to maintain a record of disconnected client identifiers, allowing
     * for any necessary cleanup or reallocation of resources associated with these clients.
     */
    private ArrayList<String> DisconnectedClients;
    /**
     * A map that associates each {@link MultiClientHandler} instance with its corresponding {@link Thread}.
     * This structure is used to manage and track active client threads in the server.
     *
     * Each {@code MultiClientHandler} is responsible for handling communication with an individual client,
     * and the associated {@code Thread} allows for asynchronous handling of multiple clients simultaneously.
     *
     * This map enables the server to maintain an organized structure, facilitating operations such as
     * managing, interrupting, or removing specific client threads during the server's execution.
     */
    private HashMap<MultiClientHandler,Thread> multiClientThreads;

    /**
     * Instantiates a new TCPServer object used for managing a multi-threaded TCP server.
     * This constructor initializes the server with the required handlers, maintains a map of client tokens,
     * tracks disconnected clients, and sets up a structure to manage active client threads.
     *
     * @param gamesHandler       The main GamesHandler instance that manages game-related operations and logic.
     * @param tokenMap           A thread-safe map associating client tokens with their corresponding VirtualView instances.
     * @param DisconnectedClients A list of tokens representing clients that have disconnected from the server.
     * @throws RemoteException   If a remote communication error occurs during the initialization process.
     */
    public TCPServer(GamesHandler gamesHandler, ConcurrentHashMap<String, VirtualView> tokenMap, ArrayList<String> DisconnectedClients) throws RemoteException {
        this.gamesHandler = gamesHandler;
        this.tokenMap = tokenMap;
        this.DisconnectedClients = DisconnectedClients;
        multiClientThreads = new HashMap<>();
    }

    /**
     * Executes the main logic of the TCP server by initializing and running a server socket
     * that listens for incoming client connections on a specific TCP port. The method performs the following tasks:
     *
     * - Prints a message to indicate that the TCP server has started.
     * - Initializes a `ServerSocket` to listen on the port specified by `Settings.TCP_PORT`.
     * - Enters an infinite loop where it accepts client connections and handles communication with them.
     * - For each connection, it:
     *     - Accepts a client socket connection and prints a message indicating acceptance.
     *     - Creates a new `MultiClientHandler` to manage communication with the connected client.
     *     - Sets the listeners in the `gamesHandler` to interact with the newly created `MultiClientHandler`.
     *     - Starts a new thread to run the `MultiClientHandler` to handle the client asynchronously.
     *     - Adds the client handler and its associated thread to a synchronized map of threads (`multiClientThreads`).
     *
     * This method ensures that the server operates continuously to handle multiple client connections using
     * a multi-threaded approach, enabling efficient handling of multiple clients simultaneously.
     *
     * Exceptions from socket operations such as `IOException` are caught and logged using stack traces.
     */
    public void run() {

        System.out.println("TCP Server Started!");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(Settings.TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Listening on port " + Settings.TCP_PORT + "...");


        while (true) {
            Socket clientSocket = null;
            try {
                assert serverSocket != null;
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accepted");
            MultiClientHandler clientHandler = new MultiClientHandler(clientSocket, gamesHandler, tokenMap, DisconnectedClients, this);
            gamesHandler.setListeners(clientHandler);
            Thread t = new Thread(clientHandler);
            t.start();
            synchronized (multiClientThreads) {
                multiClientThreads.put(clientHandler, t);
            }
        }
    }

    public void removeMC(MultiClientHandler clientHandler) {
//        synchronized (multiClientThreads) {
//            Thread t = multiClientThreads.remove(clientHandler);
//            try {
//                t.interrupt();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }





}
