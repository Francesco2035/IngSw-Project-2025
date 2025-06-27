package org.example.galaxy_trucker.ClientServer;

import javafx.util.Pair;
import org.example.galaxy_trucker.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.ClientServer.Messages.ConnectionRefusedEvent;
import org.example.galaxy_trucker.ClientServer.Messages.LobbyEvent;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The GamesHandler class is responsible for managing the lifecycle of games, handling player initialization,
 * processing login requests, managing game controllers, and communication between players and games.
 * It serves as a bridge between the RMI server, virtual views, game controllers, and listeners for lobby events.
 * Implements the LobbyListener interface to receive updates for lobby-related events.
 */
public class GamesHandler implements LobbyListener {

    /**
     * A map that associates unique player tokens with their corresponding game IDs.
     *
     * This map is used to track the relationship between a player's token, acting as a unique identifier,
     * and the specific game instance they are associated with. It facilitates operations such as
     * player reconnections, game removal, and command routing by providing a way to quickly identify
     * the game linked to a given token.
     *
     * Thread-safety and access synchronization considerations need to be taken into account
     * when modifying or accessing this map, as it is a shared resource within the GamesHandler class.
     */
    private final HashMap<String, String> tokenToGame = new HashMap<>();
    /**
     * A mapping of game identifiers to their respective {@link GameController} instances.
     * This map is used to manage and interact with active game controllers within the context of the game handler.
     *
     * Thread-safety:
     * Access to this map may involve synchronization where required to ensure it can be safely
     * accessed and modified in a concurrent environment.
     *
     * Usage context:
     * - The map allows retrieving or updating {@link GameController} instances based on their associated game IDs.
     * - It is a key component of the game management system, facilitating game operations like adding, removing,
     *   or processing game-specific commands for individual games.
     */
    private final HashMap<String, GameController> gameControllerMap;
    /**
     * A thread-safe blocking queue that holds pairs of {@link Command} and {@link VirtualView}
     * instances, representing pending player login requests to be processed.
     *
     * Each entry in the queue contains:
     * - A {@link Command} object containing details required for player initialization or login.
     * - A {@link VirtualView} instance representing the player's communication interface with the server.
     *
     * The queue is utilized in a multithreaded environment to ensure that login requests
     * are processed asynchronously and in the order they are received. The processing of these
     * requests is handled by a background thread that continuously polls the queue until interrupted.
     *
     * Thread-safety is achieved through the use of a {@link BlockingQueue}, which allows
     * multiple threads to safely add and retrieve elements concurrently.
     *
     * This queue is primarily used internally by the {@link GamesHandler} class
     * to manage the initialization and addition of players to their respective game sessions.
     */
    private final BlockingQueue<Pair<Command, VirtualView>> pendingLogins;
    /**
     * A list of {@link GhListener} instances that are registered to listen for
     * and handle events related to the game lobby and player actions.
     *
     * This collection serves as a centralized repository for all listeners
     * that have expressed interest in monitoring lobby events, game updates,
     * and player-related actions. The listeners list is used to:
     * - Notify all registered listeners about the current state of the lobby or
     *   specific events by invoking their respective methods.
     * - Add new listeners through {@link GamesHandler#setListeners(GhListener)}
     *   to extend event-handling capabilities.
     * - Provide access to the list of listeners through
     *   {@link GamesHandler#getListeners()}, offering flexibility to inspect
     *   or manage the listeners dynamically.
     *
     * This field is a core component of the event broadcasting mechanism within
     * the {@link GamesHandler}, ensuring all relevant parties are updated about
     * significant events in the game or lobby environment.
     */
    private ArrayList<GhListener> listeners = new ArrayList<>();
    /**
     * A collection that stores {@link LobbyEvent} instances representing events
     * occurring within the game lobby.
     *
     * This list is utilized to maintain the history of lobby events and to
     * facilitate the broadcasting of events to registered listeners.
     * Events added to this list are typically propagated to all instances of
     * {@link GhListener} that are registered with the containing class.
     *
     * Thread-safety considerations:
     * - Access to this list should be properly synchronized if shared across multiple threads.
     */
    private ArrayList<LobbyEvent> lobbyEvents = new ArrayList<>();
    /**
     * Represents an instance of the `RMIServer` used for managing remote method invocation (RMI)
     * operations in the context of the GamesHandler.
     *
     * This field serves as the primary entry point for handling RMI-based communication
     * and is used to facilitate interactions between the server-side logic and remote clients.
     *
     * The `rmi` instance can be set or updated using the `setRmiServer` method.
     * It plays a crucial role in providing networked communication capabilities for
     * game management and player interactions.
     *
     * Thread-safety: The usage of this field should be synchronized or managed carefully
     * to ensure no concurrent modification issues arise when accessing or updating the RMI server logic.
     */
    private RMIServer rmi;

    /**
     * Sets the RMIServer instance to be used by the GamesHandler.
     *
     * @param rmi the RMIServer instance to associate with the GamesHandler
     */
    public void setRmiServer(RMIServer rmi) {
        this.rmi = rmi;
    }

    /**
     * Adds a listener to the list of listeners and updates its state by calling
     * the listener's updateLobby method for each existing LobbyEvent.
     *
     * @param listener the GhListener instance to be added to the list of listeners and updated
     *                 with the current lobby events.
     */
    public void setListeners(GhListener listener) {
        this.listeners.add(listener);
        for (LobbyEvent lobbyEvent : lobbyEvents){
            listener.updateLobby(lobbyEvent);
        }

    }

    /**
     * Constructs a new instance of the GamesHandler class.
     * This constructor initializes the internal data structures required for the game handler,
     * including a map for game controllers and a queue for pending logins.
     *
     * Additionally, it starts a background thread to process pending player login requests
     * asynchronously. The thread continuously handles entries from the pending logins queue
     * in a loop until interrupted.
     */
    public GamesHandler() {
        this.gameControllerMap = new HashMap<>();
        this.pendingLogins = new LinkedBlockingQueue<>();


        Thread loginWorker = new Thread(this::processPendingLogins);
        loginWorker.setDaemon(true);
        loginWorker.start();
    }

    /**
     * Adds a command and its associated virtual view to the queue of pending player logins.
     *
     * @param command The command object containing details related to player initialization or login.
     * @param virtualView The virtual view instance that represents the player's client-side communication interface.
     */
    public void enqueuePlayerInit(Command command, VirtualView virtualView) {
        pendingLogins.offer(new Pair<>(command, virtualView));
    }

    /**
     * Continuously processes pending login requests from a queue and initializes players accordingly.
     *
     * This method retrieves login requests from the `pendingLogins` queue in a blocking manner and processes
     * them until the thread is interrupted or terminated. Each retrieved login request consists of a
     * {@link Command} object and a {@link VirtualView} object, which are used to initialize a player.
     *
     * Error handling is provided for {@link InterruptedException}, which interrupts the thread and breaks
     * the loop, and for generic exceptions that may occur during player initialization.
     *
     * Key operations performed:
     * - Retrieves login entries from the `pendingLogins` queue.
     * - Extracts relevant data from the {@link Command}.
     * - Invokes the `initPlayer` method to register and initialize the player.
     * - Handles errors and logs messages for debugging purposes.
     *
     * Thread-safety:
     * - This method operates in an environment with potential concurrency, as it utilizes a shared blocking queue.
     * - Ensures the thread is interrupted correctly when an {@link InterruptedException} occurs.
     */
    private void processPendingLogins() {
        while (true) {
            try {

                Pair<Command, VirtualView> entry = pendingLogins.take();

                System.out.println(entry.getKey().playerId);

                initPlayer(entry.getKey(), entry.getValue());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.out.println("Error while initializing player: " + e.getMessage());
            }
        }
    }


    /**
     * Processes an incoming command and applies it to the appropriate game controller based on the game ID.
     * The method handles game-specific logic, ensuring that commands are routed and executed accordingly.
     * If the command is a "Quit" command, it attempts to remove the associated player from the game.
     * For other commands, it enqueues the command in the respective game's command queue.
     *
     * @param command the {@link Command} object being received and processed. It contains information
     *                such as the game ID, title, and token, which are used to route and apply the command.
     */
    public void receive(Command command) {

        try{
            System.out.println("Received: " + command.getTitle() + " " + command.getClass().getSimpleName());
            String title = command.getTitle();
            String gameId = command.getGameId();
            if ("Quit".equals(title)) {
                if (gameControllerMap.containsKey(gameId)) {
                    gameControllerMap.get(gameId).removePlayer(command.getToken(), command);

                } else {
                    System.out.println("No player found for token: " + command.getToken());
                    //throw new InvalidInput("GameId doesn't exist: " + gameId);
                }
            } else {
                if (gameControllerMap.containsKey(gameId)) {
                    gameControllerMap.get(gameId).addCommand(command);
                } else {
                    //throw new InvalidInput("GameId doesn't exist: " + gameId);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Removes a game from the management system using its unique identifier.
     * The method identifies the game to be removed by traversing the tokenToGame
     * map, removes the corresponding token, and also clears the related entry
     * from the gameControllerMap.
     *
     * @param gameId the unique identifier of the game to be removed
     */
    public void removeGame(String gameId) {
        System.out.println("Removing game: " + gameId);
        String toRemove = null;
        for (String token : tokenToGame.keySet()) {
            if (tokenToGame.get(token).equals(gameId)) {
                toRemove = token;
            }
        }
        tokenToGame.remove(toRemove);
        gameControllerMap.remove(gameId);
    }

    /**
     * Initializes a player in the game system based on the provided command and virtual view.
     *
     * This method handles player registration and linkage to the appropriate game controller.
     * It verifies the game ID and player details from the command, ensuring the conditions
     * to join or create a game are met. Depending on the presence of a matching game,
     * the player is either added to the existing game or a new game is created.
     * Additionally, it manages the player's link with their virtual view as the
     * communication channel and updates relevant mappings and pending connections.
     *
     * @param command      the {@link Command} instance containing game and player details,
     *                     such as the game ID, player ID, and level.
     * @param virtualView  the {@link VirtualView} instance representing the player's client-side
     *                     communication interface.
     */
    public void initPlayer(Command command, VirtualView virtualView) {
        System.out.println("initPlayer");
        try {
            String gameID = command.getGameId();
            String check = "";
            if (gameControllerMap.containsKey(gameID)) {
                check = gameControllerMap.get(gameID).check(command);

            }
            if (!check.equals("")){
                virtualView.sendEvent(new ConnectionRefusedEvent(check));
                throw new InvalidInput(check);
            }

            else {
                String playerID = command.getPlayerId();
                int lvl = command.getLv();

                Player temp = new Player();
                temp.setId(playerID);
                temp.setPhaseListener(virtualView);
                temp.setState(new BaseState());
                synchronized (tokenToGame) {
                    tokenToGame.putIfAbsent(virtualView.getToken(), gameID);
                }

                if (gameControllerMap.containsKey(gameID)) {
                    System.out.println("Game exists: " + gameID);
                    gameControllerMap.get(gameID).NewPlayer(temp, virtualView, virtualView.getToken());
                    rmi.addPending(virtualView.getToken());

                }
                else {
                    System.out.println("Game doesn't exist: " + gameID);
                    Game curGame = new Game(lvl, gameID);

                    synchronized (gameControllerMap) {
                        GameController gameController = new GameController(gameID, curGame, this,command.getLv(), command.getMaxPlayers());
                        gameController.setLobbyListener(this);
                        gameControllerMap.putIfAbsent(gameID, gameController);
                        gameControllerMap.get(curGame.getGameID()).NewPlayer(temp, virtualView, virtualView.getToken());
                    }
                    rmi.addPending(virtualView.getToken());

                }

            }

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }

    /**
     * Retrieves the map containing the associations between game identifiers and their corresponding
     * GameController instances.
     *
     * This method provides synchronized access*/
    public synchronized HashMap<String, GameController> getGameControllerMap() {
        return gameControllerMap;
    }

    /**
     * Handles the disconnection*/
    public void PlayerDisconnected(String token) {

        String game;

        synchronized (tokenToGame) {
            game = tokenToGame.get(token);
        }
        if (game != null) {
            synchronized (gameControllerMap) {
                GameController gc = gameControllerMap.get(game);
                if (gc != null){
                    gc.stopPlayer(token);
                }

            }
        }



    }

    /**
     * Reconnects a player to their associated game session using*/
    public void PlayerReconnected(String token) {
        String game;
        synchronized (tokenToGame) {
            game = tokenToGame.get(token);
        }
        if (game != null) {
            synchronized (gameControllerMap) {
                gameControllerMap.get(game).startPlayer(token);
            }
        }


    }

    /**
     * Sends a {@link LobbyEvent} to all registered listeners and adds the event to the internal lobby events list.
     *
     * This method ensures that the provided {@link LobbyEvent} is propagated to all instances
     * of {@link GhListener} that have been registered with the class. Additionally, the event
     * is stored in a list for maintaining the history or state of lobby events.
     *
     * @param event the {@link LobbyEvent} instance representing the event to broadcast to all
     *              listeners and store in the lobby events list.
     */
    @Override
    public void sendEvent(LobbyEvent event) {
        lobbyEvents.add(event);
        for (GhListener listener : listeners) {
            listener.sendEvent(event);
        }
    }

    /**
     * Retrieves the list of registered listeners.
     *
     * This method returns the collection of {@link GhListener} instances that are currently
     * registered and actively listening for events related to the game or lobby.
     *
     * @return an {@link ArrayList} of {@link GhListener} objects representing the registered listeners.
     */
    public ArrayList<GhListener> getListeners() {
        return listeners;
    }
}
