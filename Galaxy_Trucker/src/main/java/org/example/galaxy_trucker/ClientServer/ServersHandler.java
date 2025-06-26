package org.example.galaxy_trucker.ClientServer;

import org.example.galaxy_trucker.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.ClientServer.TCP.TCPServer;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ServersHandler class is responsible for managing the initialization and execution of the server components
 * for both TCP and RMI communication mechanisms. It handles the creation of server threads and their associated
 * resources, ensuring proper connectivity for clients and management of their interactions.
 *
 * This class implements the Runnable interface to enable concurrent execution of the servers.
 *
 * Responsibilities:
 * - Initializes and starts the TCP server in a separate thread.
 * - Initializes and starts the RMI server in a separate thread.
 * - Shares common resources, including a map for client sessions and a list for disconnected clients,
 *   between the TCP and RMI servers.
 * - Associates the RMI server with the game handler to support client-side communication.
 */
public class ServersHandler implements Runnable {



    /**
     * Executes the initialization and concurrent management of both TCP and RMI server components.
     * This method is the entry point for the server setup and is executed when the class instance is
     * run in a thread. It initializes and starts the servers in separate threads, ensuring proper
     * communication setup and client management.
     *
     * Functionality:
     * - Creates a shared instance of the `GamesHandler` to manage game logic.
     * - Initializes a shared list for tracking disconnected clients.
     * - Initializes and starts the TCP server in a separate thread using a token map and disconnected
     *   clients list for session and client management.
     * - Initializes and starts the RMI server in a separate thread using the same shared resources.
     * - Links the RMI server to the game handler for further communication handling.
     *
     * Exceptions:
     * - Throws a `RuntimeException` if a `RemoteException` occurs during the initialization of
     *   either the TCP or RMI server.
     */
    public void run(){

        GamesHandler gameHandler = new GamesHandler();

        ArrayList<String> DisconnectedClients = new ArrayList<>();

        //start thread server tcp
        ConcurrentHashMap<String, VirtualView> tokenMap = new ConcurrentHashMap<String, VirtualView>();
        TCPServer TCP = null;
        try {
            TCP = new TCPServer(gameHandler, tokenMap, DisconnectedClients);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Thread ThreadTCP = new Thread(TCP);
        ThreadTCP.start();

        //start thread server rmi
        RMIServer RMI = null;
        try {
            RMI = new RMIServer(gameHandler, tokenMap, DisconnectedClients);
            gameHandler.setRmiServer(RMI);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Thread ThreadRMI = new Thread(RMI);
        gameHandler.setListeners(RMI);
        ThreadRMI.start();

    }

}
