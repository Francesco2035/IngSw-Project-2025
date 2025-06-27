package org.example.galaxy_trucker.ClientServer.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.ClientServer.Messages.ConnectionRefusedEvent;
import org.example.galaxy_trucker.ClientServer.Messages.LobbyEvent;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Exceptions.InvalidInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The MultiClientHandler class implements the Runnable interface and the GhListener interface
 * to handle multiple client connections and provide appropriate event handling logic
 * for game lobby and client interactions.
 *
 * This class manages the communication with a single client, processes incoming commands,
 * and ensures proper synchronization with the overall game logic. It also handles client
 * disconnections, reconnections, and updates related to the game lobby.
 */
public class MultiClientHandler implements Runnable, GhListener {

    /**
     * The socket associated with the connected client.
     *
     * This variable represents the communication endpoint for the client's connection
     * to the server. It is used to send and receive data during the client's session.
     * The `clientSocket` is initialized during the creation of the `MultiClientHandler`
     * instance and is managed throughout the lifecycle of the connection, including
     * handling reconnections, disconnections, and session termination.
     *
     * Proper resource management ensures the socket is closed upon disconnection to
     * release server resources and maintain stability in a multi-client environment.
     */
    private Socket clientSocket;
    /**
     * Manages game-related operations and interactions for the connected client.
     *
     * The `GamesHandler` is responsible for handling game sessions, events, and commands
     * related to the client's gameplay. It serves as the primary interface to process
     * commands received from the client, such as starting new games, managing active sessions,
     * updating game states, and client reconnections. Additionally, it helps coordinate
     * broader game logic and ensures server-client synchronization.
     *
     * This variable is utilized in `MultiClientHandler` to delegate game-specific tasks
     * and ensure that client requests are routed to the appropriate game session logic.
     */
    private GamesHandler gameHandler;
    /**
     * A thread-safe concurrent hash map that manages the association between
     * session tokens (as {@code String}) and corresponding {@code VirtualView} instances.
     *
     * This map is utilized to maintain a relationship between unique tokens
     * generated during client authentication (such as during login or reconnection)
     * and the associated {@code VirtualView} object, which represents the server-side
     * representation of a connected client's game interaction state.
     *
     * The {@code tokenMap} plays a critical role in:
     * - Tracking active user sessions by mapping session tokens to client-specific views.
     * - Supporting client reconnections by ensuring that previously authenticated tokens
     *   can retrieve their respective {@code VirtualView}.
     * - Facilitating multi-threaded operations safely, as it employs thread-safe
     *   mechanisms provided by {@code ConcurrentHashMap}.
     *
     */
    private ConcurrentHashMap<String, VirtualView> tokenMap;
    /**
     * A list maintaining the identifiers (such as usernames or session tokens) of clients
     * that are currently disconnected from their active sessions.
     *
     * This variable is utilized to track clients who have lost connection either temporarily
     * or due to network interruptions. It allows the system to manage reconnection attempts
     * and session continuity for disconnected clients.
     *
     * In the context of the MultiClientHandler, this list is accessed and modified to
     * add or remove clients during events such as disconnection, reconnection, or
     * termination of sessions.
     */
    private ArrayList<String> disconnectedClients;
    /**
     * Represents the maximum number of reconnection attempts allowed for a client.
     *
     * This variable is used within the context of client connection management.
     * It sets a limit on the number of retries a client can make to reconnect
     * after being disconnected from the server. Exceeding this limit results
     * in termination of further attempts to re-establish the connection.
     */
    private int attempts = 3;
    /**
     * Represents the unique session token associated with the connected client.
     *
     * This variable is used to uniquely identify and authenticate a client's session
     * within the server. It is dynamically assigned upon client login or reconnection
     * and is utilized to map client sessions to their respective virtual views.
     *
     * The token plays a critical role in managing session persistence and ensuring
     * that each client's state is securely maintained and retrievable across interactions.
     */
    private String Token;
    /**
     * A collection that maps unique string identifiers to corresponding {@code LobbyEvent} objects.
     *
     * This map serves as a centralized data structure for managing lobby-related events,
     * where each key represents a unique game or lobby identifier, and the value is the
     * associated {@code LobbyEvent} instance providing details about that specific lobby
     * (such as its level, list of players, and maximum player capacity).
     *
     * The {@code lobbyEvents} map is primarily used to keep track of the state of ongoing
     * game lobbies in a thread-safe manner within the context of the multi-client handler.
     */
    private HashMap<String, LobbyEvent> lobbyEvents = new HashMap<>();
    /**
     * The TCP variable represents the TCPServer instance that handles core server operations
     * and manages client connections in the application.
     *
     * It serves as the central server system responsible for:
     * - Establishing and maintaining client connections.
     * - Handling incoming and outgoing data streams.
     * - Coordinating multi-client communication processes.
     * - Managing connection-related events such as disconnections and reassignments.
     *
     * This instance is used as a foundational component for the MultiClientHandler
     * to interact with the server and facilitate robust client-server communication
     * in a concurrent and networked environment.
     */
    TCPServer TCP ;
    /**
     * Represents the connection status of the client managed by the MultiClientHandler.
     *
     * This variable indicates whether the client associated with this handler is currently
     * connected to the server. The state is initialized to `false` and updated when the connection
     * is established or terminated during the lifecycle of the handler.
     *
     * Key points:
     * - A value of `true` indicates that the client is actively connected to the server.
     * - A value of `false` indicates that the client is not connected, either due to
     *   an intentional disconnection or an error.
     * - The variable is used internally to monitor and manage the connection state during operations
     *   such as handling commands, managing reconnections, and processing client requests.
     */
    private boolean connected = false;

    private long lastPingTime;

    /**
     * Constructor for the MultiClientHandler class, responsible for initializing
     * the client connection and handling related game operations.
     *
     * @param clientSocket the socket associated with the connected client.
     * @param gameHandler the handler responsible for managing game sessions and events.
     * @param tokenMap a concurrent hash map linking session tokens to virtual views for clients.
     * @param disconnectedClients a list of clients currently disconnected from their active sessions.
     * @param TCP the TCP server instance managing overall server operations and client connections.
     */
    public MultiClientHandler(Socket clientSocket, GamesHandler gameHandler, ConcurrentHashMap<String, VirtualView> tokenMap, ArrayList<String> disconnectedClients, TCPServer TCP) {
        this.clientSocket = clientSocket;
        this.gameHandler = gameHandler;
        this.tokenMap = tokenMap;
        this.disconnectedClients = disconnectedClients;
        this.TCP = TCP;
        lobbyEvents = new HashMap<>();
        lobbyEvents.put("EMPTY CREATE NEW GAME", new LobbyEvent("EMPTY CREATE NEW GAME", -1,null, -1));
    }


    /**
     * Starts the MultiClientHandler instance and manages the client's connection lifecycle.
     *
     * This method is executed when the Runnable interface's run method is invoked.
     * It initializes the connection status by setting the `connected` field to true
     * and logs that the handler has started. Then, it invokes the clientLoop method,
     * which manages the interaction with the connected client, including processing commands
     * and managing connection states.
     */
    @Override
    public void run() {
        connected = true;
        System.out.println("MultiClientHandler started");
        clientLoop();
    }


    /**
     * Continuously handles the connection with a client, processes incoming commands, and provides appropriate responses.
     *
     * The method communicates with the client using input and output streams, manages client commands, and coordinates
     * reconnections, disconnections, and game-related logic. It ensures protocol adherence and handles various types
     * of client requests such as "Lobby", "Login", "Reconnect", or other commands processed by the game handler.
     *
     * Key operations include:
     * - Receiving and parsing client commands in JSON format using the ObjectMapper.
     * - Responding to a client "ping" with a "pong" message to maintain connection stability.
     * - Handling "Lobby" commands to send back a thread-safe list of lobby events.
     * - Managing "Login" commands to authenticate players and generate unique tokens for session management.
     * - Processing "Reconnect" commands to allow previously disconnected clients to rejoin their sessions.
     * - Delegating other command-specific operations to the game handler.
     *
     * The method also enforces a limited number of reconnection attempts and manages socket timeouts. It ensures that
     * tokens and client virtual views are synchronized and safely handled within multi-threaded contexts.
     *
     * Proper resource management is ensured in the finally block, where the client socket is closed, and the
     * client's disconnection status is updated in relevant data structures.*/
    private void clientLoop() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            clientSocket.setSoTimeout(10000);
            ObjectMapper objectMapper = new ObjectMapper();
            String s;

            while (attempts > 0) {
                try {
                    s = in.readLine();
                    if (s == null) {
                        System.out.println("Client closed the connection.");
                        break;
                    }
                    //System.out.println(s);
                    if (s.equals("ping")) {
                        attempts = 3;
                        out.println("pong");
                    } else {
                        attempts = 3;
                        //System.out.println("Received: " + s);

                        Command command = objectMapper.readValue(s, Command.class);
                        System.out.println("Deserialized command: " + command.getTitle());

                        if (command.getTitle().equals("Lobby")){
                            System.out.println("Lobby received");
                            PrintWriter finalOut = out;
                            new Thread(()->{
                                synchronized (lobbyEvents) {
                                    for (LobbyEvent event : lobbyEvents.values()) {
                                        ObjectMapper objectMapper1 = new ObjectMapper();
                                        try {
                                            finalOut.println(objectMapper1.writeValueAsString(event));
                                        } catch (JsonProcessingException e) {
                                            System.out.println("Error serializing event: " + e.getMessage());
                                        }
                                    }
                                }
                            }).start();
                        }

                        else if (command.getTitle().equals("Login")) {
                            UUID token;
                            String shortToken = "";
                            VirtualView vv = new VirtualView(command.getPlayerId(), command.getGameId(), command.getClient(), out);
                            synchronized (tokenMap) {
                                do {
                                    token = UUID.randomUUID();
                                    shortToken = token.toString().substring(0,8);
                                } while (tokenMap.containsKey(shortToken));
                                Token = shortToken;
                                vv.setToken(shortToken);
                                tokenMap.put(shortToken, vv);
                            }
                            try{
                                gameHandler.initPlayer(command,vv);
                                out.println("Token: " + shortToken);

                            }catch (InvalidInput e){
                                ConnectionRefusedEvent event = new ConnectionRefusedEvent(e.getMessage());
                                ObjectMapper objectMapper1 = new ObjectMapper();
                                out.println(objectMapper1.writeValueAsString(event));
                                synchronized (tokenMap){
                                    tokenMap.remove(shortToken);
                                }

                            }
                            //gameHandler.enqueuePlayerInit(command, vv);
                        }


                        else if (command.getTitle().equals("Reconnect")) {
                            System.out.println("Reconnecting...");
                            Token = command.getToken();

                            if (Token != null && disconnectedClients.contains(Token)) {
                                synchronized (tokenMap) {
                                    VirtualView vv = tokenMap.get(Token);
                                    vv.setDisconnected(false);
                                    vv.setPrintWriter(out);
                                    vv.reconnect();
                                }
                                synchronized (disconnectedClients) {
                                    disconnectedClients.remove(Token);
                                }
                                gameHandler.PlayerReconnected(Token);
                            } else {
                                ConnectionRefusedEvent event = new ConnectionRefusedEvent("Reconnection failed, token not valid");
                                ObjectMapper objectMapper1 = new ObjectMapper();
                                out.println(objectMapper1.writeValueAsString(event));
                                System.out.println("Illegal token");
                                attempts = -1; //non capisco cosa faccia
                            }
                        } else {
                            gameHandler.receive(command);
                        }
                    }

                } catch (SocketTimeoutException e) {
                    attempts--;
                    System.out.println("Socket timed out, attempts: " + attempts);
                } catch (IOException e) {
                    System.out.println("IO exception: " + e.getMessage());
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (Token != null) {
                    connected = false;
                    synchronized (tokenMap) {
                        VirtualView vv = tokenMap.get(Token);
                        if (vv != null) {
                            vv.setDisconnected(true);
                            vv.setPrintWriter(null);
                        }
                    }
                    synchronized (disconnectedClients) {
                        gameHandler.PlayerDisconnected(Token);
                        if (!disconnectedClients.contains(Token)) {
                            disconnectedClients.add(Token);
                        }
                    }
                }
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }


    /**
     * Sends a lobby event to the client and updates the lobby states accordingly.
     *
     * This method processes the given lobby event, updates the internal lobby state,
     * and attempts to send the event to the connected client if the socket is active.
     * If the client cannot be reached or any error occurs during the transmission,
     * the exception is caught and logged without interrupting the program flow.
     *
     * @param event the {@code LobbyEvent} to be sent, containing details about the
     *              game session and lobby state such as game ID, level, players,
     *              and the maximum number of players.
     */
    @Override
    public void sendEvent(LobbyEvent event) {
        System.out.println(event + " " + event.getGameId());
        lobbyEvents.remove("EMPTY CREATE NEW GAME");
        lobbyEvents.remove(event.getGameId());
        lobbyEvents.put(event.getGameId(), event);
        try{
            if (connected){
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                ObjectMapper objectMapper = new ObjectMapper();
                out.println(objectMapper.writeValueAsString(event));
            }
        }
        catch (Exception e) {
            System.out.println("Error sending log Event: " + e.getMessage());
        }

    }


    /**
     * Updates the current state of the lobby events by removing specific entries
     * and adding or replacing an entry associated with the given lobby event.
     *
     * This method ensures that specific placeholder entries and outdated lobby events
     * are removed, and the provided event is added or updated in the internal
     * representation of lobby events.
     *
     * @param event the {@code LobbyEvent} containing updated information about
     *              the game session, including game identifier, player list, and
     *              other lobby-related details
     */
    @Override
    public void updateLobby(LobbyEvent event) {
        lobbyEvents.remove("EMPTY CREATE NEW GAME");
        lobbyEvents.remove(event.getGameId());
        lobbyEvents.put(event.getGameId(), event);
    }


    /**
     * Handles the player quit action by removing the current client handler
     * from the TCP server's management system.
     *
     * This method is triggered when a player sends a QuitCommand. It ensures
     * that the connection associated with the quitting player is appropriately
     * removed from the TCP server's list of active clients.
     *
     * @param event the {@code QuitCommand} representing the player's quit action,
     *              including related details such as player ID and game context.
     */
    @Override
    public void quitPlayer(QuitCommand event) {
        TCP.removeMC(this);
    }
}
