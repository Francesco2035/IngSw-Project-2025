package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.ClientServer.GamesHandler;
import org.example.galaxy_trucker.ClientServer.Messages.*;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Controller.Listeners.GameLobbyListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.LogEvent;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Connectors.UNIVERSAL;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.MainCockpitComp;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The GameController class is responsible for managing the game's main state, including player interactions,
 * commands, and overall game flow. It serves as the central hub for the game's operations, managing the players,
 * their connections, and game state transitions.
 *
 * This class interacts directly with game components such as players, controllers, commands, and virtual views.
 * It also handles essential game processes like starting the game, managing flight mode, checking for game over
 * conditions, and sending updates to players and game lobby listeners.
 *
 * GameController further implements multiple listener interfaces to handle concurrent events, readiness, and the
 * end of the game. These listeners ensure effective handling and notification of important events within
 * the game lifecycle.
 *
 * Superclasses:
 * - java.lang.Object
 * - org.example.galaxy_trucker.Controller.Messages.ConcurrentCardListener
 * - org.example.galaxy_trucker.Controller.Messages.ReadyListener
 * - org.example.galaxy_trucker.Controller.Messages.FinishListener
 *
 * Fields:
 * - idGame: Identifier for the game.
 * - ControllerMap: Maps players to their corresponding controllers.
 * - connectedPlayers: Tracks currently connected players.
 * - commandQueues: Manages queues of commands for processing.
 * - threads: Tracks threads associated with the game state.
 * - VirtualViewMap: Maps player tokens to their virtual views.
 * - tokenToPlayerId: Maps tokens to player IDs.
 * - game: Represents the primary game instance this controller manages.
 * - gh: The game handler instance managing multiple games.
 * - flightQueue: Queue for managing flight mode actions.
 * - flightThread: Thread associated with flight mode operations.
 * - flightMode: Indicates whether flight mode is active.
 * - flightCount: Tracks the number of flight-related actions.
 * - buildingCount: Tracks the number of building-related actions.
 * - GameOver: Indicates if the game is over.
 * - started: Indicates if the game has started.
 * - firstflight: Indicates the status of the first flight event.
 * - concurrent: Tracks concurrent actions in the game.
 * - color: Tracks color assignments for players.
 * - lv: Level or difficulty setting of the game.
 * - finished: Indicates if the game has finished.
 * - maxPlayer: Maximum number of players allowed in the game.
 * - lock: Synchronization lock for concurrency control.
 * - prepThread: Preparation thread for pre-flight operations.
 * - lobbyListener: Listener for lobby events.
 * - gameLobbyListeners: Tracks all game lobby listeners.
 *
 * Methods:
 * - setLobbyListener(LobbyListener lobbyListener): Sets a listener for the game lobby.
 * - isStarted(): Checks if the game has started.
 * - getVirtualViewMap(): Retrieves the map of player tokens to their virtual views.
 * - GameController(String idGame, Game game, GamesHandler gh, int lv, int maxPlayer): Constructor to initialize the game controller.
 * - getControllerMap(): Returns the map of players to their controllers.
 * - NewPlayer(Player p, VirtualView vv, String token): Adds a new player to the game with their corresponding virtual view and token.
 * - changeState(): Changes the game state.
 * - addCommand(Command command): Adds a command to the processing queue.
 * - removePlayer(String token, Command command): Removes a player from the game.
 * - stopGame(): Stops the game.
 * - setControllerMap(Player player, Controller controller): Maps a player to a specific controller.
 * - startFlightMode(): Activates flight mode for the game.
 * - checkGameOver(): Checks if the game is over.
 * - setGameOver(): Marks the game as over.
 * - stopAllPlayerThreads(): Stops all threads associated with players.
 * - setFlightCount(int count): Sets the flight count for the game.
 * - setBuildingCount(int count): Sets the building count for the game.
 * - getGame(): Retrieves the current game instance.
 * - stopPlayer(String token): Stops a specific player's thread.
 * - startPlayer(String token): Starts a specific player's thread.
 * - updatePlayers(): Updates all players with the latest game state.
 * - sendGameLobbyUpdate(GameLobbyEvent event): Sends updates to game lobby listeners.
 * - onConcurrentCard(boolean phase): Handles a concurrent card event.
 * - getlv(): Retrieves the level or difficulty of the game.
 * - check(Command command): Checks the validity or status of a command.
 * - sendMessage(LogEvent event): Sends a log message to players or spectators.
 * - onReady(): Handles events signaling readiness.
 * - onEndGame(boolean success, String playerId, String message, ScoreboardEvent event): Handles the end-game process.
 * - getConnection(String playerId): Checks the connection status of a player by ID.
 * - getTokenToPlayerId(): Returns the map of tokens to player IDs.
 */
public class GameController implements ConcurrentCardListener, ReadyListener, FinishListener {
    /**
     * Represents the unique identifier for a game.
     * This variable is used to distinguish one game instance from another.
     */
    String idGame;
    /**
     * A private final HashMap that serves as a mapping between string keys
     * and their corresponding Controller objects.
     *
     * This map is used to manage and retrieve Controller instances associated
     * with specific string identifiers. Once initialized, the map cannot be reassigned.
     */
    private final HashMap<String, Controller> ControllerMap;
    /**
     * A map that keeps track of connected players and their connection statuses.
     * The keys represent player identifiers (e.g., usernames), and the values are booleans
     * indicating whether the player is currently connected (true) or not (false).
     */
    private final HashMap<String, Boolean> connectedPlayers = new HashMap<>();
    /**
     * A map that holds command queues identified by their string keys.
     * Each key associates with a {@link BlockingQueue} that stores commands of type {@link Command}.
     * This structure is used to organize and manage command processing queues in a thread-safe manner.
     */
    private final HashMap<String, BlockingQueue<Command>> commandQueues = new HashMap<>();
    /**
     * A map that holds a collection of threads, where the key is a string identifier
     * associated with each thread, and the value is the corresponding thread instance.
     * This map is used to manage and retrieve threads by their identifiers.
     */
    private final HashMap<String, Thread> threads = new HashMap<>();
    /**
     * A map that associates a string identifier with a corresponding VirtualView object.
     * This map is used to manage and store relationships between string keys and their associated virtual views.
     * It is implemented as a final HashMap, ensuring that the reference to the map itself cannot be reassigned.
     */
    private final HashMap<String, VirtualView> VirtualViewMap = new HashMap<>();
    /**
     * A mapping between tokens and player IDs.
     *
     * This HashMap is used to associate unique tokens with corresponding player IDs,
     * enabling identification and management of players based on their assigned tokens.
     * The key represents a unique token (String), and the value is the corresponding
     * player ID (String).
     */
    private final HashMap<String, String> tokenToPlayerId = new HashMap<>();
    /**
     * Represents an instance of a Game.
     * This variable holds a reference to the current game object.
     * It is marked as final, meaning the reference cannot be reassigned
     * once it has been initialized.
     */
    final Game game;
    /**
     * A private instance of the GamesHandler class, responsible for
     * managing and controlling operations related to games within
     * the application. This variable serves as a central access
     * point for game-related functionalities and operations.
     */
    private GamesHandler gh;
    /**
     * A thread-safe queue for managing Command objects related to flight operations.
     * The {@code flightQueue} is implemented as a {@code LinkedBlockingQueue} to ensure
     * proper synchronization and safe operations in a multi-threaded environment.
     * It is used to store and retrieve commands that control or monitor flight activities.
     * Commands are processed in the order they are added, following FIFO (First-In-First-Out) principle.
     */
    //private BlockingQueue<Command> prepQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Command> flightQueue = new LinkedBlockingQueue<>();
    /**
     * A {@code Thread} instance named {@code flightThread}, which is used to handle
     * operations related to flight execution processes asynchronously.
     *
     * This thread may be utilized to perform tasks such as monitoring flight data,
     * updating flight statuses, or managing related concurrent operations. The exact
     * functionality depends on the context in which the {@code flightThread} is invoked.
     *
     * It is expected that this thread will operate independently, facilitating efficient
     * handling of flight-related tasks without blocking the main processing workflow.
     */
    private Thread flightThread;
    /**
     * Represents the flight mode status of a system or device.
     * This variable indicates whether the flight mode is enabled or disabled.
     * When set to {@code true}, the flight mode is active, disabling certain functionalities like network connections.
     * When set to {@code false}, the flight mode is inactive, allowing normal operation.
     */
    private boolean flightMode = false;
    /**
     * Represents the total number of flights recorded or stored.
     * This variable is initialized to 0 and can be updated as new flight data is added.
     */
    int flightCount = 0;
    /**
     * Represents the total number of buildings.
     * This variable is used to store the count of buildings
     * in a given context such as a city, neighborhood,
     * or a specific project.
     */
    int buildingCount = 0;
    /**
     * A boolean variable indicating whether the game has ended.
     *
     * This variable is set to {@code true} when the game concludes, and remains
     * {@code false} while the game is ongoing. It serves as a flag for controlling
     * the game's life cycle or state.
     */
    boolean GameOver = false;
    /**
     * A flag indicating whether the process or operation has been started.
     * The value is {@code true} if it has started, otherwise {@code false}.
     */
    private boolean started = false;
    /**
     * Represents a flag to indicate whether it is the first flight.
     * The value is set to true by default.
     */
    private boolean firtflight = true;
    /**
     * Indicates whether the process or operation should be executed concurrently.
     * The default value is {@code false}, meaning concurrent execution is disabled.
     * When set to {@code true}, it allows tasks to run simultaneously,
     * ensuring potential efficiency in multi-threaded environments.
     */
    private boolean concurrent = false;
    /**
     * Represents the color value as an integer.
     * The value typically corresponds to a specific color defined by its
     * integer representation, which might be encoded as an ARGB, RGB, or
     * another color format depending on the application's usage.
     */
    private int color = 153;
    /**
     * Represents a local variable initialized to the value 0.
     * This variable can be used to store an integer value
     * within its scope.
     */
    int lv = 0;
    /**
     * A variable representing the completion status of a process or task.
     * The value is typically used as a flag or counter:
     * - 0 indicates the process or task is incomplete.
     * - A non-zero value may indicate completion or progress, depending on the context.
     */
    int finished = 0;
    /**
     * Represents the maximum number of players allowed.
     * This variable defines the upper limit for the number of players
     * that can participate in a game or session.
     */
    private int maxPlayer = 4;
    /**
     * A final object used as a synchronization lock to manage thread-safe operations.
     * This lock ensures that only one thread can execute a block of code or access
     * a shared resource at a time when synchronized on this object.
     */
    private final Object lock = new Object();
    /**
     * A private Thread instance used for preparation tasks.
     * This thread is likely utilized for executing background operations
     * or tasks related to preparing certain processes or resources.
     */
    private Thread prepThread;

    /**
     * An instance of the LobbyListener interface used to handle
     * events or interactions occurring within a lobby context.
     * The specific functionality and behavior of this listener are
     * defined by the implementation of the LobbyListener interface.
     */
    private LobbyListener lobbyListener;
    /**
     * A collection of listeners that observe and react to events occurring in the game lobby.
     * This list holds instances of objects implementing the GameLobbyListener interface.
     * Listeners in this collection are notified about relevant updates or changes
     * within the game lobby, allowing them to respond accordingly.
     */
    private ArrayList<GameLobbyListener> gameLobbyListeners = new ArrayList<>();

    /**
     * Sets the listener for handling lobby-related events in the game controller.
     *
     * @param lobbyListener the LobbyListener instance to be set. This listener will handle
     *                      lobby events, such as sending lobby updates or handling lobby-specific
     *                      events during the game lifecycle.
     */
    public void setLobbyListener(LobbyListener lobbyListener) {
        this.lobbyListener = lobbyListener;
    }

    /**
     * Checks whether the game has been started.
     *
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Retrieves the map of virtual views associated with their corresponding identifiers.
     *
     */
    public synchronized HashMap<String, VirtualView> getVirtualViewMap() {
        return VirtualViewMap;
    }

    /**
     * Constructs*/
    public GameController(String idGame, Game game, GamesHandler gh, int lv, int maxPlayer) {
        System.out.println(this);
        this.idGame = idGame;
        ControllerMap = new HashMap<>();
        this.game = game;
        this.gh = gh;
        this.flightQueue = new LinkedBlockingQueue<>();
        this.lv = lv;
        this.maxPlayer = maxPlayer;
//        this.prepThread = new Thread(() -> {
//            while(true){
//                Command cmd = prepQueue.poll();
//            }
//        });

    }


    /**
     * Retrieves the map of controllers associated with specific strings (e.g., player IDs or roles).
     */
    public HashMap<String, Controller> getControllerMap() {
        return ControllerMap;
    }


    /**
     * Adds a new player to the game, initializes necessary components and listeners,
     * and starts a*/
    public void NewPlayer(Player p, VirtualView vv, String token) {
        if (ControllerMap.keySet().contains(p.GetID())) {
            vv.sendEvent(new ConnectionRefusedEvent("Player ID " + p.GetID() + " already exists in game " + idGame));
            //throw new IllegalArgumentException("Player ID " + p.GetID() + " already exists in game "+idGame);
        } else if (maxPlayer == ControllerMap.size()) {
            vv.sendEvent(new ConnectionRefusedEvent(idGame + " is full!"));
        } else {
            vv.setLv(lv);
            String playerId = p.GetID();
            System.out.println("Player ID: " + playerId);
            System.out.println("Token: " + token.toString());
            Controller controller = new LoginController(p, idGame);
            controller.setExceptionListener(vv);
            ControllerMap.put(playerId, controller);
            sendMessage(new LogEvent("New player: " + playerId, -1, -1, -1, -1));

            System.out.println("New player " + playerId + " in " + this);
            tokenToPlayerId.put(token, playerId);
            BlockingQueue<Command> queue = new LinkedBlockingQueue<>();
            commandQueues.put(playerId, queue);
            game.NewPlayer(p);
            VirtualViewMap.put(playerId, vv);

            p.getmyPlayerBoard().setRewardsListener(vv);
            p.getmyPlayerBoard().setListener(vv);
            p.getCommonBoard().getCardStack().addListener(p.GetID(), vv);
            Tile mainCockpitTile = new Tile(new MainCockpitComp(), UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE, UNIVERSAL.INSTANCE);
            mainCockpitTile.setId(color);
            color++;
            p.setReadyListener(this);
            p.setFinishListener(this);
            p.setHandListener(vv);
            p.getCommonBoard().setListeners(vv);
            p.getCommonBoard().getTilesSets().setListeners(vv);
            p.setCardListner(vv);
            gameLobbyListeners.add(vv);
            vv.setEventMatrix(game.getGameBoard().getLevel());
            for (VirtualView v : VirtualViewMap.values()) {

                if (!vv.getPlayerName().equals(v.getPlayerName())) {
                    v.setPlayersPBListeners(vv);
                    //System.out.println("vv "+vv.getPlayerName()+" v "+v.getPlayerName());
                    vv.setPlayersPBListeners(v);
                }
            }
            p.getmyPlayerBoard().insertTile(mainCockpitTile, 6, 6, false);
            updatePlayers();
            synchronized (connectedPlayers) {
                connectedPlayers.put(playerId, true);
            }

            Thread t = new Thread(() -> {
                while (getConnection(playerId)) {
                    synchronized (ControllerMap) {
                        Controller current = ControllerMap.get(playerId);
//                        if(current != null && !getConnection(playerId)){
//                            System.out.println("Building command "+ current.getClass()+ " "+ current.getDisconnected());
//                            current.DefaultAction(this);
//                        }
//                        else{
                        Command cmd = queue.poll();
                        if (cmd != null) {
                            System.out.println("Bulding command connected");
                            current.action(cmd, this);
                        }

                    }
                }
            });
            t.start();
            threads.put(playerId, t);
            ArrayList<String> players = new ArrayList<>(VirtualViewMap.keySet());
            if (lobbyListener != null)
                lobbyListener.sendEvent(new LobbyEvent(game.getGameID(), game.getLv(), players, maxPlayer));
        }


    }


    /**
     * Synchronizes and transitions the game state based on player readiness.
     * If all players in the game are marked as ready,*/
    public synchronized void changeState() {
        System.out.println("change state called");

        long readyCount = game.getPlayers().values().stream()
                .filter(Player::GetReady)
                .count();

        if (readyCount == game.getPlayers().size()) {
            System.out.println("change state successful, ready count " + readyCount);
            synchronized (game) {
                for (Player p : game.getPlayers().values()) {
                    String playerId = p.GetID();
                    ControllerMap.get(playerId).nextState(this);
                }
                started = true;
                game.getPlayers().values().forEach(p -> p.SetReady(false));
            }
        }
    }

    /**
     * Adds a command to the appropriate queue for the respective player or flight queue based on the current game state.
     * The command is processed differently depending on whether the game is in flight mode or not.
     *
     * @param command the {@link Command} instance to be added. It contains information about the player
     *                and the action to be executed. The method ensures that the command is added to the
     *                correct queue if the player is not disconnected and the queue for the player exists.
     */
    public void addCommand(Command command) {
        if (!flightMode) {
            BlockingQueue<Command> queue = commandQueues.get(command.getPlayerId());
            if (queue != null && !VirtualViewMap.get(command.getPlayerId()).getDisconnected()) {
                System.out.println("adding command for player " + command.getPlayerId() + " " + command.getClass());
                queue.offer(command);
            } else {
                System.out.println("Empty queue for: " + command.getPlayerId());
            }
        } else {
            if (!VirtualViewMap.get(command.getPlayerId()).getDisconnected()) {
                this.flightQueue.offer(command);
            }
        }
    }

    /**
     * Removes a player from the game based on the given token and command. This method ensures
     * that the player is in a valid state to be removed, handles any associated cleanup, and
     * removes the player from the game board if allowed. If the player cannot be removed
     * due to their current state, an exception event is sent.
     *
     * @param token the unique identifier used to look up the player to be removed.
     * @param command the command associated with removing the player, used to validate
     *                the player's current state.
     * @throws IllegalArgumentException if the player ID corresponding to the token does
     *                                  not exist or is not valid.
     */
    public void removePlayer(String token, Command command) {
        String playerId = tokenToPlayerId.get(token);
        if (playerId == null || !ControllerMap.containsKey(playerId)) {
            throw new IllegalArgumentException("Player ID " + playerId + " non found");
        }
        if (!command.allowedIn(game.getPlayers().get(playerId).getPlayerState())) {
            getVirtualViewMap().get(playerId).sendEvent(new ExceptionEvent("You can't quit in this state!"));
        } else {
            try {
                System.out.println("Player removed: " + playerId);

                game.getGameBoard().abandonRace(game.getPlayers().get(playerId), "Abandoned race", started);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Stops the game and performs the necessary cleanup operations.
     *
     * This method ensures that all resources and data structures associated
     * with the game are cleared and prepares the system for proper termination
     * of the game session.
     *
     * Operations performed by this method include:
     * - Clearing all active threads related to the game.
     * - Emptying the command queues for game commands.
     * - Clearing the controller map (associations between players and controllers).
     * - Removing all players from the current game session.
     * - Removing the game instance from the game handler.
     */
    public void stopGame() {

        //threads.values().forEach(Thread::interrupt);
        threads.clear();
        commandQueues.clear();
        ControllerMap.clear();
        game.getPlayers().clear();
        gh.removeGame(idGame);
    }

    /**
     * Updates the controller map by associating a specified player with a given controller.
     * If an association already exists for the given player, it will be replaced with the new controller.
     * Additionally, triggers specific game state transitions if certain conditions related
     * to building or flight modes are met.
     *
     * @param player     the Player instance to be mapped to the provided controller.
     * @param controller the Controller instance to be associated with the specified player.
     */
    public void setControllerMap(Player player, Controller controller) {
        synchronized (ControllerMap) {
            System.out.println(player.GetID() + " : " + controller.getClass());
            ControllerMap.remove(player.GetID());
            ControllerMap.put(player.GetID(), controller);

            if (buildingCount == ControllerMap.size()) {
                sendMessage(new LogEvent("Building started", -1, -1, -1, -1));
                try {
                    game.getGameBoard().StartHourglass();
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

                buildingCount = -1;
            }

            if (flightCount == ControllerMap.size()) {
                for (Player p : game.getPlayers().values()) {
                    p.SetReady(false);
                }

                flightMode = true;
                startFlightMode();
                flightCount = 0;
            }
            for (Controller controller1 : ControllerMap.values()) {
                System.out.println(controller1.getClass());
            }
        }

    }

    /**
     * Initiates the flight mode for the game, transitioning the game logic to a state where players must
     * act according to the drawn card effects in a concurrent or sequential manner based on the card's configuration.
     * During flight mode activation:
     *
     * - If this is the first flight, the card decks will be merged.
     * - A new card is drawn from the deck, and its effects are applied to the players.
     * - The flight mode operates with either concurrent or sequential player actions, depending on the card's setup.
     * - If a player is disconnected, a default action is executed on their behalf.
     * - Player states are updated after actions or disconnection handling.
     * - All players' readiness is reset at the end of the flight mode.
     *
     * The method utilizes threads to manage the flight phase, ensuring asynchronous handling of game flow.
     * It logs key events, such as the start and end of the flight, and interactions during card effects.
     */
    public void startFlightMode() {

//client si riconnete ma non può inviare input fino a che non si ricambia il controller
        if (firtflight) {
            System.out.println("MERGE");
            game.getGameBoard().getCardStack().mergeDecks();
        }
        firtflight = false;
        ArrayList<Player> players = game.getGameBoard().getPlayers();
        sendMessage(new LogEvent("Flight started", -1, -1, -1, -1));
        flightThread = new Thread(() -> {
            System.out.println("PESCO CARTA!");

            Card card = game.getGameBoard().NewCard();
            sendMessage(new LogEvent("New card drawn", -1, -1, -1, -1));


            card.setConcurrentCardListener(this);
            for (String player : VirtualViewMap.keySet()) {
                card.setRandomCardEffectListeners(player, VirtualViewMap.get(player));
            }
            card.sendTypeLog();
            try {
                card.CardEffect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int index = 0;

            while (!card.isFinished()) {
                System.out.println(players.size());
                index = 0;
                int k = 0;
                Player currentPlayer = players.get(index);
                while (index < players.size() && !card.isFinished()) {
                    if (k >= 100000001) {
                        System.out.println("CURRENT: " + currentPlayer.GetID() + " " + currentPlayer.getPlayerState().getClass().getSimpleName());
                        for (Player p : game.getPlayers().values()) {
                            System.out.println("PLAYER: " + p.GetID() + " " + p.getPlayerState().getClass().getSimpleName() + " " + ControllerMap.get(p.GetID()));
                        }
                        k = 0;
                    }
                    k++;
                    for (int j = 0; j < players.size(); j++) {
                        if (!players.get(j).GetHasActed()) {
                            currentPlayer = players.get(j);
                            break;
                        }
                    }

                    //Player currentPlayer = players.get(index);

                    Controller cur = ControllerMap.get(currentPlayer.GetID());

                    /// probabilmente da errore con meteoriti per la concorrenzialità e perché current non è molto deterministiFFco, potrebbe essere che vada spostato dentro al controllo di current
                    if (cur != null && !getConnection(currentPlayer.GetID())) { // se è disconnesso chiamo il comando di default
                        System.out.println("Player disconnected " + cur.getClass());
                        try {
                            cur.DefaultAction(this);
                        } catch (Exception e) {
                            //System.exit(-1);
                            //throw new ImpossibleActionException("errore nell'azione di default, che dio ci aiuti");
                        }
                        ///  credo ci vada una thìry ctch ma non la stava lanciando :)

                        ///  ready ce lomette la carta quando sa che il player deve smettere di dar input
                        if (currentPlayer.GetHasActed()) {
                            //System.out.println("aggiorno index");
                            index++;
                        }

                    } else { // se il player  non è disconneso prendo icommand dalla queue

                        try {
                            Command cmd = flightQueue.poll();
                            if (cmd != null) {
                                if (concurrent) {
                                    Controller controller = ControllerMap.get(cmd.getPlayerId());
                                    controller.action(cmd, this);
                                } else if (cmd.getPlayerId().equals(currentPlayer.GetID())) {
                                    Controller controller = ControllerMap.get(cmd.getPlayerId());
                                    controller.action(cmd, this);

                                    ///  ready ce lomette la carta quando sa che il player deve smettere di dar input

                                    //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>index "+ index);
                                    if (currentPlayer.GetHasActed()) {
                                        //System.out.println("aggiornmo index");
                                        index++;
                                    }
                                } else {
                                    flightQueue.offer(cmd);
                                }
                            }


                        } catch (Exception e) {
                            System.out.println("Exception: " + e.getMessage());
                            break;
                        }
                    }
                }


                System.out.println("PRIMO WHILE FINITO");


            }

            System.out.println("USCITO DAL SECONDO WHILE");
            Controller ReadySetter;
            //System.out.println("players "+ game.getPlayers().size());
            for (Player p : game.getPlayers().values()) {
                System.out.println("-------------------------------------------------FORCED");
                System.out.println(p.GetID() + " is in this state: " + p.getPlayerState().getClass());
                ReadySetter = ControllerMap.get(p.GetID());
                ReadyCommand readyCommand = new ReadyCommand(game.getID(), p.GetID(), game.getLv(), "Ready", true, "placeholder");
                ReadySetter.action(readyCommand, this);
                p.SetHasActed(false);
                ///  senno invece che mettere tutti a ready posso frli direttamente andare nell'altro contrpoller??

//                p.SetReady(true);
            }
            sendMessage(new LogEvent("Flight finished", -1, -1, -1, -1));
            flightMode = false;
            for (Player p : game.getPlayers().values()) {
                System.out.println("PLAYER: " + p.GetID() + " " + p.getPlayerState().getClass().getSimpleName() + " " + ControllerMap.get(p.GetID()));
            }
        });

        flightThread.start();
        //THREAD NON FINISCE SEMPLCIMENTE STO CREANDO OGNI VOLTA UNO NUOVO NEL CASO DI CARTA NON SPECIALE

//        flightMode = false;
//        changeState();

    }

    /**
     * Checks if the game has reached its end and is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean checkGameOver() {
        return GameOver;
    }

    /**
     * Sets the game state to "Game Over" when a particular condition is met.
     * This method increments the `finished` counter, which tracks the number of
     * players or components that have completed their part of the game. Once the
     * `finished` counter equals the total number of entries in the `ControllerMap`,
     * the gameboard's `finishGame` method is invoked to conclude the game.
     */
    public void setGameOver() {
        GameOver = true;
        finished++;
        if (finished == ControllerMap.size()) {
            getGame().getGameBoard().finishGame();
        }

        //System.out.println("Game over the winner is: " + game.getGameBoard().getPlayers().getFirst().GetID());
    }

    /**
     * Stops all threads associated with player actions in the game controller.
     *
     * This method iterates over the collection of player threads, interrupts each thread to halt its execution,
     * and then clears the collection to remove any references to the stopped threads.
     */
    public void stopAllPlayerThreads() {

        for (Thread t : threads.values()) {
            t.interrupt();
        }
        threads.clear();
    }

    /**
     * Updates the total count of flights by adding the specified value.
     *
     * @param count the number to be added to the current flight count. A positive value
     *              increments the flight count, while a negative value decrements it.
     */
    public void setFlightCount(int count) {
        flightCount += count;
        System.out.println("FLIGHTCOUNT " + flightCount);
    }

    /**
     * Updates the total count of buildings by adding the specified value.
     *
     * @param count the number to be added to the current building count.
     *              A positive value increments the building count,
     *              while a negative value decrements it.
     */
    public void setBuildingCount(int count) {
        buildingCount += count;
    }


    /**
     * Retrieves the current game instance.
     *
     * @return the {@link Game} instance associated with this controller.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Disconnects a player and handles the associated cleanup and thread management.
     *
     * This method:
     * 1. Retrieves the player ID associated with the given token.
     * 2. Marks the player as disconnected in the controller map and connectedPlayers map.
     * 3. Interrupts and removes any active thread associated with the player.
     * 4. Starts a new thread to handle the disconnection state. This thread manages the player's
     *    default actions if disconnected and ensures proper flow until certain conditions are met,
     *    such as game over or reconnection.
     *
     * @param token the unique identifier used to locate the player to be stopped.
     */
    public void stopPlayer(String token) {
        String playerId = tokenToPlayerId.get(token);
        Controller curr = ControllerMap.get(playerId);
        curr.setDisconnected(true);
        synchronized (connectedPlayers) {
            connectedPlayers.put(playerId, false);
        }
        //setto booleano del controller

        //if (!flightMode) {
        System.out.println("Player ID " + playerId + " not in flight mode, interrupting thread");
        threads.get(playerId).interrupt();
        threads.remove(playerId);
        Thread t = new Thread(() -> {
            while (!GameOver && !getConnection(playerId)) {
                if (!GameOver && !game.getPlayers().get(playerId).GetHasActed() && !getConnection(playerId) && !flightMode) {
                    System.out.println("disconnection Thread");
                    Controller current = ControllerMap.get(playerId);
                    synchronized (lock) {
                        current.DefaultAction(this);
                    }
                }
                if (!GameOver && game.getPlayers().get(playerId).GetHasActed() && !flightMode) {
                    //System.out.println("disconnection Thread: has acted");
                }
            }
            System.out.println("disconnection Thread finished");
        });
        ;
        t.start();
        threads.put(playerId, t);
        //}

    }

    /**
     * Starts the player session and initializes the necessary components for the player to resume gameplay.
     * This method handles player reconnection, thread management, and command processing setup.
     *
     * @param token the unique identifier associated with the player session. This token
     *              is used to retrieve the player ID and manage the player's session state.
     */
    public void startPlayer(String token) {
        String playerId = tokenToPlayerId.get(token);
        System.out.println("STARTING " + playerId);
        //VirtualViewMap.get(playerId).sendEvent(new ReconnectedEvent(token,game.getGameID(),playerId, lv));
        synchronized (lock) {
            threads.get(playerId).interrupt();
        }
        Controller curr = ControllerMap.get(playerId);
        curr.setDisconnected(false);
        synchronized (connectedPlayers) {
            connectedPlayers.put(playerId, true);
        }
        //setto booleano del controler
        threads.remove(playerId);

        //if (!flightMode){
        System.out.println("Player ID " + playerId + " not in flight mode, starting thread");

        BlockingQueue<Command> queue = commandQueues.get(playerId);
        Thread t = new Thread(() -> {
            while (getConnection(playerId)) {
                synchronized (ControllerMap) {
                    Controller current = ControllerMap.get(playerId);

                    Command cmd = queue.poll();
                    if (cmd != null) {
                        System.out.println("Bulding command connected");
                        current.action(cmd, this);
                    }

                }
            }
        });
        t.start();
        threads.put(playerId, t);

        //}

    }

    /**
     * Updates the list of players and their readiness status and sends a game lobby update event.
     *
     * This method retrieves the current list of players and their readiness state
     * using the game's internal data structures. It then constructs and sends a
     * {@link GameLobbyEvent} to notify listeners of any changes in the game lobby.
     *
     * The method performs the following steps:
     * 1. Retrieves the list of player identifiers from the VirtualViewMap.
     * 2. Fetches the readiness state of each player from the game's player map.
     * 3. Constructs a {@link GameLobbyEvent} containing the list of players and their readiness states.
     * 4. Invokes {@link #sendGameLobbyUpdate(GameLobbyEvent)} to dispatch the event to all registered listeners.
     */
    public void updatePlayers() {
        ArrayList<Boolean> ready = new ArrayList<>();
        ArrayList<String> players = new ArrayList<>(VirtualViewMap.keySet());
        for (String player : players) {
            ready.add(game.getPlayers().get(player).GetReady());
        }
        sendGameLobbyUpdate(new GameLobbyEvent(players, ready));
    }

    /**
     * Sends an updated game lobby event to all registered listeners.
     * The method notifies each listener of changes in the game lobby state
     * by invoking their {@code GameLobbyChanged} method with the provided event.
     *
     * @param event the {@link GameLobbyEvent} instance containing the updated
     *              state of the game lobby, including information about players
     *              and their readiness status.
     */
    public void sendGameLobbyUpdate(GameLobbyEvent event) {
        for (GameLobbyListener listener : gameLobbyListeners) {
            listener.GameLobbyChanged(event);
        }
    }

    /**
     * Handles the behavior when a concurrent card is drawn or activated during the game.
     * This method sets the game's state to either operate in concurrent mode or not,
     * based on the provided parameter.
     *
     * @param phase a boolean indicating the mode of operation:
     *              true to enable concurrent mode,
     *              false to disable concurrent mode.
     */
    @Override
    public void onConcurrentCard(boolean phase) {
        this.concurrent = phase;
    }

    /**
     * Retrieves the level (lv) value associated with this GameController instance.
     *
     * @return the integer value of the level (lv) for this GameController.
     */
    public int getlv() {return this.lv;}


    /**
     * Checks various conditions for a game and returns appropriate messages.
     *
     * @param command the command object containing game and player details to be checked
     * @return a string message indicating the result of the check;
     *         returns an empty string if all conditions pass
     */
    public String check(Command command) {
        if (isStarted()) {
            return "Game already stated!";
        }
        if (command.getLv() != lv) {
            return "Game level doesn't match!";
        }
        if (ControllerMap.containsKey(command.getPlayerId())) {
            return "Player " + command.getPlayerId() + " already exists in game " + game.getID();
        }
        if (ControllerMap.size() == maxPlayer) {
            return "This game is full";
        }
        return "";
    }


    /**
     * Sends a message to all registered VirtualViews.
     *
     * @param event the log event containing the message to be sent
     */
    public void sendMessage(LogEvent event) {
        System.out.println("sending message " + event.message());
        for (VirtualView vv : VirtualViewMap.values()) {
            vv.sendLogEvent(event);
        }
    }

    /**
     * This method is called when the system or component is ready.
     * It overrides the onReady method to perform necessary actions
     * upon readiness. In this implementation, it updates the state
     * of players by invoking the updatePlayers method.
     */
    @Override
    public void onReady() {
        updatePlayers();
    }

    /**
     * Handles the end-of-game procedures for a player, including removing listeners,
     * interrupting player threads, and updating the game state. Sends appropriate events
     * and updates connected players accordingly.
     *
     * @param success Indicates if the game ended successfully or if the player finished successfully.
     * @param playerId The unique identifier of the player who is ending their participation in the game.
     * @param message An optional message providing context or details about the end game scenario.
     * @param event An optional ScoreboardEvent detailing the outcome of the player's game session.
     */
    @Override
    public void onEndGame(boolean success, String playerId, String message, ScoreboardEvent event) {
        try {
            System.out.println("Player removed: " + playerId);


            //REMOVING LISTENERS

            Player p = game.getPlayers().remove(playerId);
            p.getmyPlayerBoard().removeListener();
            p.removeCardListener();
            p.removeHandListener();
            p.removeReadyListener();
            p.removeFinishListener();

            VirtualView vv = getVirtualViewMap().remove(playerId);
            p.getCommonBoard().getCardStack().removeListener(p.GetID());
            p.getCommonBoard().removeListener(vv);
            p.getCommonBoard().getTilesSets().removeListeners(vv);
            gameLobbyListeners.remove(vv);

            for (VirtualView vv2 : getVirtualViewMap().values()) {
                if (vv2 != vv) {
                    vv2.removeListener(vv);
                }
            }
            Thread t = threads.remove(playerId);
            if (t != null) {
                synchronized (lock) {
                    t.interrupt();
                }
            }
            commandQueues.remove(playerId);
            ControllerMap.remove(playerId);
            game.RemovePlayer(playerId);
            if (game.getPlayers().isEmpty()) {
                System.out.println("Stop game");
                lobbyListener.sendEvent(new LobbyEvent(game.getGameID(), -1, null, maxPlayer));
                stopGame();
            } else {
                ArrayList<String> players = new ArrayList<>(ControllerMap.keySet());
                if (lobbyListener != null)
                    lobbyListener.sendEvent(new LobbyEvent(game.getGameID(), game.getLv(), players, maxPlayer));
            }
            //VirtualView vv2 = VirtualViewMap.remove(playerId);
            sendMessage(new LogEvent(playerId + "quit", -1, -1, -1, -1));
            if (event != null) {
                vv.sendEvent(event);
            } else {
                vv.sendEvent(new FinishGameEvent(success, message));
            }
            vv.removeListeners();
            vv.setDisconnected(true);
            updatePlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Retrieves the connection status of a player based on their unique identifier.
     *
     * @param playerId the unique identifier of the player whose connection status is to be retrieved
     * @return true if the player is connected, false otherwise
     */
    public boolean getConnection(String playerId) {
        synchronized (connectedPlayers) {
            return connectedPlayers.get(playerId);
        }
    }

    /**
     * Retrieves the mapping of tokens to player IDs.
     *
     * @return A HashMap where the keys are tokens (String) and the values are corresponding player IDs (String).
     */
    public HashMap<String, String> getTokenToPlayerId() {
        return tokenToPlayerId;
    }
}
