package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Controller.*;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.ClientServer.Messages.ConnectionRefusedEvent;
import org.example.galaxy_trucker.ClientServer.Messages.LobbyEvent;
import org.example.galaxy_trucker.ClientServer.Messages.ReconnectedEvent;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;




/**
 * The RMIServer class is responsible for implementing remote server-side functionalities
 * using the RMI (Remote Method Invocation) mechanism. It handles client-server communication,
 * manages client connections, supports remote method execution, and monitors client health
 * through periodic ping checks.
 *
 * This class extends UnicastRemoteObject and implements the following interfaces:
 * - ServerInterface: Provides server methods to interact with RMI clients.
 * - Runnable: Allows the server to run concurrently.
 * - GhListener: Allows listening and reacting to events in the GamesHandler.
 *
 * Responsibilities:
 * - Manages client connections, including registration and disconnection handling.
 * - Implements a ping mechanism to monitor client connectivity.
 * - Synchronizes client data with the server and manages concurrent client actions.
 * - Facilitates communication with clients through remote method calls.
 * - Manages game-related events and maintains a virtual view for clients.
 *
 * Constructor:
 * - RMIServer: Initializes the server with given GamesHandler, token map, and disconnected clients list.
 *
 * Methods:
 * - StartServer: Configures and starts the RMI registry and binds server methods.
 * - receivePong: Receives a pong response from a client, updating the last ping time.
 * - command: Processes commands received from clients, including login and lobby events.
 * - handleDisconnection: Handles the disconnection of a client, updating server state and attempting to recover.
 * - startTimeoutChecker: Monitors client activity and detects timeouts for inactive clients.
 *
 * Thread Safety:
 * - Uses thread-safe data structures (e.g., ConcurrentHashMap) and synchronization blocks to manage concurrent operations.
 *
 * Dependencies:
 * - GamesHandler: Manages game logic and player data.
 * - ClientInterface: Defines methods for client communication.
 * - VirtualView: Represents client-specific game views.
 * - LobbyEvent: Represents events in the game lobby.
 * - Command: Represents client commands handled by the server.
 */
public class RMIServer extends UnicastRemoteObject implements ServerInterface, Runnable , GhListener {


    GamesHandler gh;
    private ConcurrentHashMap<String, VirtualView> tokenMap;
    private final HashMap<ClientInterface, String> clients;
    private  ArrayList<String> DisconnectedClients = new ArrayList<>();
    private final HashMap<ClientInterface, Integer> attempts = new HashMap<>();
    private ArrayList<ClientInterface> lobby = new ArrayList<>();
    private HashMap<String, LobbyEvent> lobbyEvents;
    private HashMap<String, ClientInterface> pending = new HashMap<>();
    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(8);
    private HashMap<ClientInterface, ExecutorService> pingClient = new HashMap<>();
    private final ConcurrentHashMap<ClientInterface, Long> lastPingMap = new ConcurrentHashMap<>();


    /**
     * Initiates a background thread that monitors client connections for timeouts.
     * The method continuously checks the timestamps in the `lastPingMap` to determine
     * whether a client has exceeded the allowed inactivity period of 10 seconds. If a
     * timeout is detected, the client is removed from the map, their associated
     * executor service is shut down, and the `handleDisconnection` method is invoked
     * to handle disconnection logic.
     *
     * The thread sleeps for 2 seconds between each iteration to minimize CPU usage and
     * ensure periodic checks.
     *
     * If the thread is interrupted, it gracefully terminates the execution.
     *
     * This function is private and intended to be used internally to maintain the
     * connection health of clients automatically.
     */
    private void startTimeoutChecker() {
        new Thread(() -> {
            while (true) {
                long now = System.currentTimeMillis();
                for (Map.Entry<ClientInterface, Long> entry : lastPingMap.entrySet()) {
                    if (now - entry.getValue() > 10000) {
                        ClientInterface client = entry.getKey();
                        System.out.println("Timeout: " + client);
                        lastPingMap.remove(client);
                        ExecutorService pingExec = pingClient.remove(client);
                        if (pingExec != null) {
                            pingExec.shutdown();
                            System.out.println("pingExec shutdown");
                        }
                        handleDisconnection(client);
                    }
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }


    /**
     * Handles the disconnection of a client. This method is responsible for:
     * - Retrieving the token associated with the client.
     * - Updating the disconnection status of the client in the corresponding VirtualView.
     * - Removing the client from the active clients list.
     * - Adding the client's token to the disconnected clients list.
     * - Notifying the game handler about the player's disconnection.
     *
     * @param client the client interface representing the client that has disconnected.
     */
    private void handleDisconnection(ClientInterface client) {
        String token = clients.get(client);
        if (token != null) {

            VirtualView vv = tokenMap.get(token);

            if (vv != null && !vv.getDisconnected()) {
                vv.setDisconnected(true);
                vv.setClient(null);

                System.out.println("Client disconnected: " + client);
                    synchronized (DisconnectedClients) {
                        DisconnectedClients.add(token);
                    }
                    synchronized (clients) {
                        clients.remove(client);
                    }
                gh.PlayerDisconnected(token);
            }
        }

    }


    /**
     * Constructs a new RMIServer instance and initializes the server with the given parameters.
     * Configures the maps and lists used for handling clients and server functionality.
     * Sets up the necessary components for managing client-server communication, including timeout checking
     * and game management through the GamesHandler.
     *
     * @param gamesHandler the handler used to manage games and player interactions.
     * @param tokenMap a concurrent map that associates a client's token with their corresponding VirtualView.
     * @param DisconnectedClients a list tracking tokens of clients that have been disconnected from the server.
     * @throws RemoteException if a communication-related exception occurs during the initialization.
     */
    public RMIServer(GamesHandler gamesHandler, ConcurrentHashMap<String, VirtualView> tokenMap, ArrayList<String> DisconnectedClients) throws RemoteException {
        this.tokenMap = tokenMap;
        gh = gamesHandler;
        clients = new HashMap<>();
        this.DisconnectedClients = DisconnectedClients;

        //ping thread
//        Thread pingThread = new Thread(() -> {
//
//            while (true) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//                Map<ClientInterface, String> clientsSnapshot;
//                synchronized (clients) {
//                    clientsSnapshot = new HashMap<>(clients);
//                }
//
//                for (ClientInterface client : clientsSnapshot.keySet()) {
//                    try {
//
//                        ExecutorService executor = Executors.newSingleThreadExecutor();
//                        Future<Void> future = executor.submit(() -> {
//                            try {
//                                client.receivePing();
//                            } catch (RemoteException e) {
//                                throw new ExecutionException(e);
//                            }
//                            return null;
//                        });
//
//
//                        future.get(1500, TimeUnit.MILLISECONDS);
//
//                        //System.out.println("Ping successful for client: " + client);
//
//                        executor.shutdown();
//
//                    } catch (TimeoutException e) {
//
//                        String token = clientsSnapshot.get(client);
//                        if (token != null) {
//
//                            VirtualView vv = tokenMap.get(token);
//
//                            if (vv != null && !vv.getDisconnected()) {
//                                vv.setDisconnected(true);
//                                vv.setClient(null);
//
//                                System.out.println("Client disconnected: " + client);
//                                    synchronized (DisconnectedClients) {
//                                        DisconnectedClients.add(token);
//                                    }
//                                    synchronized (clients) {
//                                        clients.remove(client);
//                                    }
//                                gh.PlayerDisconnected(token);
//
//                            }
//                        }
//                    }
//                    catch (ExecutionException | InterruptedException e) {
//                    String token = clientsSnapshot.get(client);
//                        if (token != null ) {
//
//                            VirtualView vv = tokenMap.get(token);
//                            //attempts.remove(client);
//
//                            if (vv != null && !vv.getDisconnected()) {
//                                vv.setDisconnected(true);
//                                vv.setClient(null);
//                                System.out.println("Client disconnected: " + client);
//
//                                synchronized (DisconnectedClients) {
//                                    DisconnectedClients.add(token);
//                                }
//                                synchronized (clients) {
//                                    clients.remove(client);
//                                }
//                                gh.PlayerDisconnected(token);
//                            }
//                        }
//                    }
//             }}
//            });
//        pingThread.start();
        startTimeoutChecker();


        lobbyEvents = new HashMap<>();
        lobbyEvents.put("EMPTY CREATE NEW GAME", new LobbyEvent("EMPTY CREATE NEW GAME", -1,null, -1));


    }


    /**
     * Starts the RMI server and binds it to a specified name and port defined in the Settings class.
     * The method configures the server's hostname and initializes a new RMI registry on the defined port.
     *
     * It then binds a remote object to the name "CommandReader", enabling clients to interact with
     * the server using that registry name. If binding fails due to an exception, it will log the
     * exception stack trace.
     *
     * @throws RemoteException if there are any issues in creating the registry or binding the object.
     */
    @Override
    public void StartServer() throws RemoteException {

        System.setProperty("java.rmi.server.hostname",Settings.SERVER_NAME);

        Registry registry = LocateRegistry.createRegistry(Settings.RMI_PORT);
        try {
            registry.bind("CommandReader", this);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("RMI Server Started!");
    }


    /**
     * Handles the reception of a pong message from a client for connection monitoring.
     * This method updates the last known activity timestamp for the given client interface.
     *
     * @param clientInterface the client interface instance sending the pong message. It is used
     *                        to identify and track the client's connection status.
     * @throws RemoteException if a communication-related exception occurs during the remote method call.
     */
    @Override
    public void receivePong(ClientInterface clientInterface) throws RemoteException {
        lastPingMap.put(clientInterface, System.currentTimeMillis());
    }


    /**
     * Processes a command received from the client. Depending on the command type, this method performs
     * appropriate actions such as handling lobby connections, login requests, and reconnection attempts.
     * For unknown command types, the request is forwarded to the game handler.
     *
     * @param cmd the command received containing client information, command title, and associated data.
     * @throws RemoteException if a remote communication error occurs while processing the command.
     */
    @Override
    public void command(Command cmd) throws RemoteException{
        if (cmd.getTitle().equals("Lobby")){
            System.out.println("LOBBY");
            if (lobby.contains(cmd.getClient())){
                cmd.getClient().receiveMessage(new ReconnectedEvent("lobby", "placeholder", "placeholder", -1));
            }
            else {
                lobby.add(cmd.getClient());
            }

            new Thread(()->{
                synchronized (lobbyEvents) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<Void> future = executor.submit(() -> {
                        try {
                            for (LobbyEvent event : lobbyEvents.values()) {
                                    cmd.getClient().receiveMessage(event);
                            }
                        } catch (RemoteException e) {
                            lobby.remove(cmd.getClient());
                            throw new ExecutionException(e);
                        }
                        return null;
                    });

                    try {
                        future.get(1500, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        lobby.remove(cmd.getClient());
                    }
                    executor.shutdown();

                }
            }).start();

            ScheduledExecutorService pingExecutor = Executors.newSingleThreadScheduledExecutor();

            pingExecutor.scheduleAtFixedRate(() -> {

                try {
                    cmd.getClient().receivePing();
                    System.out.println("PING");
                } catch (RemoteException e) {
                    System.out.println("Client disconnected: " + cmd.getClient());
                    pingExecutor.shutdown();
                }
            }, 0, 3, TimeUnit.SECONDS);
            pingClient.put(cmd.getClient(), pingExecutor);
            lastPingMap.put(cmd.getClient(), System.currentTimeMillis());

        }
        else if (cmd.getTitle().equals("Login")){
            System.out.println("LOGIN");
            LoginCommand lcmd = (LoginCommand) cmd;
            if (!pingClient.containsKey(lcmd.getClient())) {
                ScheduledExecutorService pingExecutor = Executors.newSingleThreadScheduledExecutor();

                pingExecutor.scheduleAtFixedRate(() -> {

                    try {
                        cmd.getClient().receivePing();
                        System.out.println("PING");
                    } catch (RemoteException e) {
                        System.out.println("Client disconnected: " + cmd.getClient());
                        lobby.remove(cmd.getClient());
                        pingExecutor.shutdown();
                    }
                }, 0, 3, TimeUnit.SECONDS);
                pingClient.put(cmd.getClient(), pingExecutor);
                lastPingMap.put(cmd.getClient(), System.currentTimeMillis());
            }
            lobby.remove(cmd.getClient());
            UUID token  = null;
            VirtualView vv = new VirtualView(cmd.getPlayerId(), cmd.getGameId(), cmd.getClient(), null);
            String shortToken = "";
            synchronized (tokenMap){
                do{
                    token = UUID.randomUUID();
                    shortToken = token.toString().substring(0,8);
                }while(tokenMap.containsKey(shortToken));
                tokenMap.put(shortToken, vv);
                System.out.println("Generated token "+shortToken+" for "+cmd.getPlayerId());
            }
            synchronized (pending){
                pending.put(shortToken, cmd.getClient());
            }
            vv.setToken(shortToken);
            gh.enqueuePlayerInit(cmd,vv);

        }
        else if (cmd.getTitle().equals("Reconnect")){
            System.out.println("RECONNECT:");
            String token = cmd.getToken();
            System.out.println(cmd.getToken() + " " + cmd.getClient());
            ScheduledExecutorService pingExecutor = Executors.newSingleThreadScheduledExecutor();

            pingExecutor.scheduleAtFixedRate(() -> {

                try {
                    cmd.getClient().receivePing();
                    //System.out.println("PING");
                } catch (RemoteException e) {
                    System.out.println("Client disconnected: " + cmd.getClient());
                    lobby.remove(cmd.getClient());
                    pingExecutor.shutdown();
                }
            }, 0, 3, TimeUnit.SECONDS);
            pingClient.put(cmd.getClient(), pingExecutor);
            lastPingMap.put(cmd.getClient(), System.currentTimeMillis());
            if (tokenMap.containsKey(token) && DisconnectedClients.contains(token)) {

                VirtualView temp = tokenMap.get(token);
                temp.setClient(cmd.getClient());
                temp.setDisconnected(false);

                synchronized (DisconnectedClients) {
                    DisconnectedClients.remove(token);
                }
                synchronized (clients) {
                    clients.put(cmd.getClient(), token);
                }

                asyncExecutor.submit(temp::reconnect);
                reconnectPlayer(token);

            }
            else {
                cmd.getClient().receiveMessage(new ConnectionRefusedEvent("Reconnection not possible: " + token));
            }
        }
        else{
            gh.receive(cmd);
        }
    }


    /**
     * Entrypoint for running the RMI server. This method attempts to start the server by invoking
     * the {@link #StartServer()} method. If an exception occurs during server startup, it catches
     * the exception and logs an error message indicating that the server failed to start, along
     * with the exception's message.
     */
    public void run(){
        try {
            this.StartServer();
        } catch (Exception e) {
            System.err.println("Error starting Rmi Server: "+ e.getMessage());
        }
    }


    /**
     * Sends a lobby event to all connected clients.
     * Updates the lobby event map with the given event, removes disconnected clients,
     * and sends the event to active clients. The method uses an asynchronous task
     * to send events and handles any disconnection or timeout issues.
     *
     * @param event the lobby event to be sent. This includes information such as
     *              the game ID, level, maximum number of players, and current players in the lobby.
     */
    @Override
    public void sendEvent(LobbyEvent event) {

        System.out.println("Sending event: " + event);
        lobbyEvents.remove("EMPTY CREATE NEW GAME");
        lobbyEvents.remove(event.getGameId());
        lobbyEvents.put(event.getGameId(), event);
        ArrayList<ClientInterface> toRemove = new ArrayList<>();

            for (ClientInterface client : lobby) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<Void> future = executor.submit(() -> {
                    try {
                        client.receiveMessage(event);
                    } catch (RemoteException e) {
                        toRemove.add(client);
                        throw new ExecutionException(e);
                    }
                    return null;
                });

                try{
                    future.get(3000, TimeUnit.MILLISECONDS);

                    //System.out.println("Ping successful for client: " + client);

                    executor.shutdown();
                }
                catch(InterruptedException | ExecutionException | TimeoutException e){
                    toRemove.add(client);
                }

            }

            lobby.removeAll(toRemove);

    }


    /**
     * Updates the state of the lobby based on the provided LobbyEvent.
     * This method processes a lobby-related event, allowing modifications to the
     * current state of the lobby such as updating the list of players, lobby level,
     * or maximum number of players.
     *
     * @param event the LobbyEvent instance containing information about the game lobby,
     *              such as the game ID, level, maximum number of players, and the list
     *              of players currently in the lobby
     */
    @Override
    public void updateLobby(LobbyEvent event) {
    }

    /**
     * Removes a client from the list of active clients when they quit the game.
     * This method is triggered by the QuitCommand and ensures thread-safe removal
     * of the client from the shared list of clients.
     *
     * @param event the QuitCommand event containing the client to be removed from the active clients list
     */
    @Override
    public void quitPlayer(QuitCommand event) {
        synchronized (clients){
            clients.remove(event.getClient());
        }
    }


    /**
     * Adds a token to the pending list and performs actions to move the token from
     * pending to active clients if the token already exists in the pending map.
     *
     * If the token exists in the pending map, it removes the token, notifies the client
     * by invoking the {@code receiveToken} method, and then moves the client to the active clients map.
     *
     * If the client fails to receive the token due to a RemoteException, an error message
     * is printed.
     *
     * @param token the unique identifier associated with a client that is being added
     *              to the pending map or transitioned to the active clients list.
     */
    public void addPending(String token){
        if (pending.containsKey(token)){
            System.out.println("removing pending ...");
            ClientInterface client;
            synchronized (pending){
                client = pending.remove(token);
            }
            try{
                client.receiveToken(token);
            } catch (RemoteException e) {
                System.out.println("cannot send token: "+token);
            }

            synchronized (clients) {
                clients.put(client, token);
            }
        }

    }


    /**
     * Reconnects a player to the game session using their unique token. The method attempts
     * to reintegrate the player's connection to the server by invoking the game handler's
     * reconnection logic. This operation is performed asynchronously on a separate thread.
     *
     * @param token the unique identifier associated with the player attempting to reconnect.
     * @throws RemoteException if a communication-related exception occurs during player reconnection.
     */
    public void reconnectPlayer(String token) throws RemoteException {
        asyncExecutor.submit(() -> {
            try {
                gh.PlayerReconnected(token);;
                System.out.println("Client reconnected: " + token);
            } catch (Exception e) {
                System.out.println("cannot reconnect player: "+token);
            }
        });
    }
}
