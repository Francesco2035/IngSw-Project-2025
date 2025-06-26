package org.example.galaxy_trucker.Controller;

import javafx.util.Pair;
import org.example.galaxy_trucker.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.Controller.Messages.ConnectionRefusedEvent;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.Controller.Messages.ReconnectedEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The GamesHandler class is responsible for managing the lifecycle of games, handling player initialization,
 * processing login requests, managing game controllers, and communication between players and games.
 * It serves as a bridge between the RMI server, virtual views, game controllers, and listeners for lobby events.
 * Implements the LobbyListener interface to receive updates for lobby-related events.
 */
public class GamesHandler implements LobbyListener {

    private final HashMap<String, String> tokenToGame = new HashMap<>();
    private final HashMap<String, GameController> gameControllerMap;
    private final BlockingQueue<Pair<Command, VirtualView>> pendingLogins;
    private ArrayList<GhListener> listeners = new ArrayList<>();
    private ArrayList<LobbyEvent> lobbyEvents = new ArrayList<>();
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
        if (game == null) {
            throw new InvalidInput("game null: ");
        }

        synchronized (gameControllerMap) {
            GameController gc = gameControllerMap.get(game);
            if (gc != null){
                gc.stopPlayer(token);
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
        if (game == null) {
            throw new InvalidInput("game null: ");
        }

        synchronized (gameControllerMap) {
            gameControllerMap.get(game).startPlayer(token);
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
