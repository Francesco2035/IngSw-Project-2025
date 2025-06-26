package org.example.galaxy_trucker.View.TUI;

import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.View.ClientModel.PlayerClient;
import org.example.galaxy_trucker.View.ViewPhase;
import org.jline.jansi.Ansi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Out class manages the output and state representation of a game,
 * enabling interactions and updates to game visuals and player data.
 * This class serves as a bridge for rendering the game state, facilitating communication
 * between backend game components and user-facing interfaces.
 */
public class Out {

    /**
     * Represents the unique identifier for a card entity.
     * This variable is used to uniquely distinguish cards within a system.
     * The default value is set to -1, indicating that the identifier is uninitialized.
     */
    private int CardId = -1;
    /**
     * Represents the client component responsible for managing player interactions
     * and communication with the server or underlying game logic.
     */
    private PlayerClient playerClient;
    /**
     * Represents a flag to determine visibility or whether a specific element
     * or feature should be shown.
     *
     * When set to {@code true}, the element or feature is visible or shown.
     * When set to {@code false}, the element or feature is hidden or not shown.
     */
    private Boolean show = true;

    /**
     * Represents a placeholder or container for information related to PB.
     * The specific purpose or usage of this variable may depend on the context in which it is used.
     * By default, this variable is initialized as an empty string.
     */
    String PBInfo = "";
    /**
     * Represents a string variable intended to store an exception message or identifier.
     * This variable can be used for capturing or handling information related to exceptions.
     */
    String exception = "";
    /**
     * Represents the outcome or influence that may result from a specific action,
     * occurrence, or condition. This variable is designed to hold descriptive
     * text that conveys the nature or consequence of an effect.
     */
    String effect = "";
    /**
     * Represents a title card, typically used to display a title or label
     * in an application or user interface.
     */
    String titleCard = "";
    /**
     * Represents the result or conclusion of an operation, decision, or process.
     * This variable is initialized as an empty string and can hold a textual
     * representation of the outcome.
     */
    String outcome = "";
    /**
     * Represents a collection of strings, typically used to model a deck of items such as playing cards.
     * The deck is implemented as an ArrayList, allowing dynamic resizing and efficient access.
     * Each element of the deck represents an individual card or item as a string.
     */
    ArrayList<String> deck = new ArrayList<>();
    /**
     * Indicates whether the hourglass feature is enabled or not.
     * The variable is a boolean flag that can be set to true to enable
     * the hourglass functionality or false to disable it.
     */
    private boolean hourglass = false;
    /**
     * A list used for storing log messages or entries as strings.
     * This ArrayList can be dynamically updated to include new log messages,
     * allowing for sequential or chronological storage of log data.
     */
    ArrayList<String> log = new ArrayList<>();


    /**
     * An {@code ExecutorService} instance initialized as a single-threaded executor.
     * This executor ensures that tasks are executed sequentially, one at a time,
     * in the order they are submitted. It is suitable for scenarios where tasks
     * must not run concurrently and require serialization of execution.
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * A thread-safe flag indicating whether an update has been scheduled.
     * This variable is immutable and defaults to false, serving as a controlled
     * mechanism to coordinate and track the state of update scheduling in
     * concurrent environments.
     */
    private final AtomicBoolean updateScheduled = new AtomicBoolean(false);

    /**
     * Represents a cache identifier or storage mechanism for a card-related entity.
     * This variable is utilized to store or manage information related to a card,
     * potentially for caching purposes. The exact functionality and purpose
     * depend on the context in which it is used in the application logic.
     */
    private String CacheCard = "";
    /**
     * Represents a list of players.
     * This variable is used to store the names of players as strings.
     * It is initialized as an empty list and can be modified to add or remove player names.
     */
    private ArrayList<String> players = new ArrayList<>();
    /**
     * A list that holds Boolean values indicating the readiness status.
     * Each element in the list represents the ready state of a specific entity or process.
     */
    private ArrayList<Boolean> ready = new ArrayList<>();


    /**
     * A final object used as a synchronization lock to ensure thread safety
     * when accessing or modifying shared resources.
     * This lock object is used in synchronized blocks or methods to control
     * concurrent access in a multi-threaded environment.
     */
    private final Object lock = new Object();
    /**
     * A map that associates unique integer identifiers with their corresponding names.
     * The keys represent the unique IDs and the values are the associated names as strings.
     * This map is designed to store and retrieve names based on their integer ID efficiently.
     */
    private final HashMap<Integer, String> idToNameMap = new HashMap<>();
    /**
     * A mapping of other players' boards in a game, where the key is the player's identifier
     * and the value is a 3-dimensional array representing the state or layout of their board.
     * Used to store and track the boards of players other than the current player.
     */
    private final HashMap<String,  String[][][]> otherPlayersBoard  = new HashMap<>();
    /**
     * Represents the fixed width of the content area.
     * This value is immutable and specifies the width in units.
     * It is used to configure or enforce layout constraints.
     */
    private final int contentWidth = 33;
    /**
     * A multi-dimensional array used to cache the state of a game board.
     * The array represents the board's state across different dimensions
     * and layers, which can vary depending on the specific implementation
     * or use case. This can improve performance when repeatedly accessing
     * or modifying the board's current or previous states.
     */
    private String[][][] cachedBoard;
    /**
     * A private array of strings used to cache hand-related data or operations.
     *
     * This variable may store intermediate or temporary values for processing
     * purposes related to a specific hand-based functionality. It is initialized
     * to null, meaning it does not hold any data by default.
     */
    private String[] cacheHand = null;
    /**
     * The `border` variable represents a constant decorative string
     * used to create a visual boundary or separation in the user interface or output.
     * It consists of a plus symbol followed by a series of horizontal lines and ends with another plus symbol.
     * This formatting is typically used for styling purposes to enhance readability.
     */
    private final String border = "+━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━+";
    /**
     * A list that stores the IDs of tiles that have been uncovered.
     * The IDs are stored in the order in which the tiles are uncovered.
     */
    private ArrayList<Integer> uncoveredTilesId = new ArrayList<>(); //ordine, quindi contiene gli ID in ordine di come arrivano
    /**
     * A cache that stores uncovered tile sets, using an integer as the key and
     * an array of strings to represent the tile set values.
     *
     * The key represents an identifier for the tile set, while the value is
     * an array of strings containing the details about each tile in the set.
     *
     * This cache is primarily used to avoid redundant operations by storing
     * previously uncovered tile sets for quick retrieval during processing.
     */
    private HashMap<Integer, String[]> uncoverdTileSetCache = new HashMap<>();
    /**
     * A HashMap that stores descriptions of cards.
     * The key is an Integer representing the card ID, and the value is a String containing the description of the card.
     */
    private HashMap<Integer, String> CardsDescriptions = new HashMap<>();
    /**
     * Represents a three-dimensional game board.
     *
     * The Gameboard variable is a 3D array that may be used to store the state
     * or configuration of a game. Each element can represent a cell in a
     * multi-dimensional grid, potentially holding specific game-related data.
     *
     * Example use cases could include implementing a 3D game or tracking
     * game-related data in a three-dimensional space.
     */
    private String[][][] Gameboard;
    /**
     * Represents an accumulator for reward-related information.
     * This variable is of type StringBuilder, allowing dynamic
     * and mutable storage of string data related to rewards.
     * It is designed to handle operations such as appending,
     * modifying, or retrieving reward information efficiently.
     */
    private StringBuilder Rewards;
    /**
     * Represents a level or value that is stored as an integer.
     * This variable may be used to track or denote a specific
     * level, grade, or numeric value in a class or application.
     */
    private int lv;
    /**
     * Represents the configuration or initialization parameter with a default value of 102.
     * This variable might be used to control or signify a specific setup state or configuration in the application.
     */
    private int setup = 102;
    /**
     * Represents the status or state of a particular entity or process.
     * The variable 'fase' is a boolean that indicates whether a specific
     * condition or operation is active or inactive.
     * It defaults to false, implying an inactive or initial state.
     */
    private boolean fase = false;
    /**
     * Represents the number of tiles covered in a specific set or context.
     * This variable holds an integer value that defines the size of a tile set
     * that is considered or accounted for, possibly in a grid or mapping system.
     * The value is initialized to 152.
     */
    private int CoveredTileSet = 152;
    /**
     * A thread-safe blocking queue designed to store and manage a collection of strings
     * that are intended to be processed in a producer-consumer scenario. This queue allows
     * threads to safely add and retrieve elements, effectively handling concurrent access.
     *
     * The queue operates with a first-in-first-out (FIFO) order, ensuring that elements
     * are processed in the order they were added. Threads attempting to retrieve an element
     * from an empty queue will be blocked until an element becomes available, while threads
     * attempting to add to a full queue (if bounded) will be blocked until space is available.
     *
     * This queue implementation is well-suited for scenarios where data needs to be safely
     * passed between multiple threads and synchronization is required.
     */
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    /**
     * Handles input reading operations for the application.
     * This variable is an instance of the InputReader class, which facilitates
     * reading and processing input data, such as from standard input or a file.
     */
    private InputReader inputReader;
    /**
     * A private instance of the {@link Thread} class used to handle input-related operations.
     * This thread is designed to run tasks associated with receiving or processing inputs
     * independently of the main application thread, ensuring asynchronous and non-blocking behavior.
     */
    private Thread inputThread;
    /**
     * Represents the connection status.
     * This variable indicates whether a connection is established.
     * The value is {@code true} if connected, and {@code false} otherwise.
     */
    private Boolean connected = false;
    /**
     * A mapping of integer positions to corresponding gameboard coordinates.
     * The HashMap uses an Integer key to represent a position and maps it to an
     * IntegerPair value, which contains the respective x and y coordinates on the gameboard.
     * This is used to relate linear positions to 2D coordinates within the game logic.
     */
    private HashMap<Integer, IntegerPair> positionToGameboard = new HashMap<>();
    /**
     * A mapping of player names to their respective positions.
     * The key is a String representing the player's name,
     * and the value is an Integer representing the position of the player.
     */
    private HashMap<String,Integer > PlayerToPosition = new HashMap<>();
    /**
     * A private HashMap representing the lobby structure.
     * The keys are Strings representing unique identifiers,
     * and the values are arrays of Strings containing associated data.
     */
    private HashMap<String, String[]> lobby = new HashMap<>();
    /**
     * Represents the current phase of the view in the application's lifecycle.
     * This variable is used to track and manage the specific state or stage
     * the view is currently in, such as initialization, rendering, or cleanup.
     */
    private ViewPhase phase;
    /**
     * A {@code StringBuilder} instance representing the scoreboard data.
     * This variable is used to store and modify the content of the scoreboard dynamically.
     * It allows for efficient appending, updating, or altering of the scoreboard details.
     * The initial value of this variable is {@code null}.
     */
    private StringBuilder scoreboard = null;



    /**
     * Updates the board of other players with specific data at a given position.
     *
     * @param playerId The unique identifier of the player whose board is being updated.
     * @param x The x-coordinate on the player's board where the update is applied.
     * @param y The y-coordinate on the player's board where the update is applied.
     * @param cell The data to be inserted at the specified position on the board.
     */
    public void setOthersPB(String playerId, int x, int y, String[] cell){
        if (!otherPlayersBoard.containsKey(playerId)){
            otherPlayersBoard.put(playerId, new String[10][10][7]);
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    otherPlayersBoard.get(playerId)[i][j] = new String[7];
                    for (int m = 0; m < 7; m++) {
                        otherPlayersBoard.get(playerId)[i][j][m] = "";
                    }
                }
            }
        }
        otherPlayersBoard.get(playerId)[x][y] = cell;
    }

    /**
     * Updates or initializes the game board for another player identified by playerId at a specific
     * position (x, y) with the given value in a specific layer (k).
     *
     * @param playerId the unique identifier of the player whose game board is being updated
     * @param x the x-coordinate of the cell on the board
     * @param y the y-coordinate of the cell on the board
     * @param k the layer index within the cell of the board
     * @param s the value to be set in the specified cell and layer
     */
    public void setOthersPB(String playerId, int x, int y,int k, String s){
        if (!otherPlayersBoard.containsKey(playerId)){
            otherPlayersBoard.put(playerId, new String[10][10][7]);
            for (int i = 0; i < 10; i++){
                for (int j = 0; j < 10; j++){
                    otherPlayersBoard.get(playerId)[i][j] = new String[7];
                    for (int m = 0; m < 7; m++) {
                        otherPlayersBoard.get(playerId)[i][j][m] = "";
                    }
                }
            }
        }
        otherPlayersBoard.get(playerId)[x][y][k] = s;
    }

    /**
     * Constructs an instance of the Out class, initializing necessary components and caches.
     *
     * @param inputReader   an InputReader instance used to receive input for the game.
     * @param playerClient  a PlayerClient instance used to manage player interactions.
     */
    public Out(InputReader inputReader, PlayerClient playerClient) {
        this.inputReader = inputReader;
        this.lobby = new HashMap<>();
        cachedBoard = new String[10][10][7];
        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                cachedBoard[k][l] = new String[7];
                for (int m = 0; m < 7; m++) {
                    cachedBoard[k][l][m] = "";
                }
            }
        }
        cacheHand = new String[7];
        for (int i = 0; i < 7; i++) {
            cacheHand[i] = "";
        }
        this.playerClient = playerClient;
    }


    /**
     * Provides access to the lock object associated with the current instance.
     * This method typically returns an object that can be used for synchronization purposes.
     *
     * @return the lock object used for synchronization
     */
    public Object getLock(){
        return lock;
    }

    /**
     * Retrieves the mapping of player IDs to their corresponding positions.
     *
     * @return a HashMap where the keys are player IDs (as Strings) and the values are their positions (as Integers)
     */
    public HashMap<String, Integer> getPlayerToPosition() {
        return PlayerToPosition;
    }

    /**
     * Updates the card ID and sets the corresponding title card based on the card ID.
     *
     * @param cardId the unique identifier for the card. Based on its value, the method assigns
     *               a specific title from the ASCII_ART collection to the titleCard field.
     *               Acceptable ranges and their corresponding title categories are:
     *               - 1, 2: TitleSlavers
     *               - 3, 4: TitleSmugglers
     *               - 5, 6: TitlePirates
     *               - 7-10: TitleAbandonedShip
     *               - 11-14: TitleAbandonedStation
     *               - 15-20: TitleMeteorSwarm
     *               - 21-28: TitlePlanets
     *               - 29-35: TitleOpenSpace
     *               - 36, 37: TitleCombatZone
     *               - 38, 39: TitleStardust
     *               - 40: TitleEpidemic
     */
    public void setCardId(int cardId) {
        CardId = cardId;
        switch (cardId){
            case 1, 2: {
                titleCard = ASCII_ART.TitleSlavers;
                break;
            }
            case 3, 4: {
                titleCard = ASCII_ART.TitleSmugglers;
                break;
            }
            case 5, 6: {
                titleCard = ASCII_ART.TitlePirates;
                break;
            }
            case 7, 8,9,10: {
                titleCard = ASCII_ART.TitleAbandonedShip;
                break;
            }
            case 11, 12,13,14: {
                titleCard = ASCII_ART.TitleAbandonedStation;
                break;
            }
            case 15,16,17,18,19,20: {
                titleCard = ASCII_ART.TitleMeteorSwarm;
                break;
            }
            case 21,22,23,24,25,26,27,28: {
                titleCard = ASCII_ART.TitlePlanets;
                break;
            }

            case 29,30,31,32,33,34,35: {
                titleCard = ASCII_ART.TitleOpenSpace;
                break;
            }

            case 36, 37: {
                titleCard = ASCII_ART.TitleCombatZone;
                break;
            }
            case 38, 39: {
                titleCard = ASCII_ART.TitleStardust;
                break;
            }
            case 40: {
                titleCard = ASCII_ART.TitleEpidemic;
                break;
            }

        }
    }

    /**
     * Sets the list of player IDs and updates the `otherPlayersBoard` map by
     * removing entries for players that are no longer in the provided list.
     *
     * @param players an ArrayList of player IDs to set as the current players
     */
    public void setPlayers(ArrayList<String> players) {
        this.players = players;
        for (String playerId : otherPlayersBoard.keySet()) {
            if (!players.contains(playerId)) {
                otherPlayersBoard.remove(playerId);
            }
        }
    }

    /**
     * Sets the readiness status for the players.
     *
     * @param ready an ArrayList of Boolean values indicating the readiness
     *              status of each player. Each Boolean represents whether
     *              a specific player is ready (true) or not (false).
     */
    public void setReady(ArrayList<Boolean> ready) {
        this.ready = ready;
    }

    /**
     * Updates the cached board at the specified coordinates with the given cell data.
     *
     * @param x    the x-coordinate on the cached board.
     * @param y    the y-coordinate on the cached board.
     * @param cell the array of strings representing the cell data to be set at the specified coordinates.
     */
    public void setCachedBoard(int x, int y, String[] cell) {
        cachedBoard[x][y] = cell;
    }

    /**
     * Updates the cached board by setting the specified value at the given coordinates.
     *
     * @param x the x-coordinate of the board.
     * @param y the y-coordinate of the board.
     * @param k the specific layer or level in the board structure.
     * @param cell the value to set in the specified position on the board.
     */
    public void setCachedBoard(int x, int y,int k, String cell) {
        cachedBoard[x][y][k] = cell;
    }

    /**
     * Sets the cache for the player's hand.
     *
     * @param cacheHand An array of strings representing the cached hand of the player.
     */
    public void setCacheHand(String[] cacheHand) {
        this.cacheHand = cacheHand;
    }


    /**
     * Updates the list of uncovered tile IDs by adding or removing the specified ID.
     * If the ID already exists in the list, it will be removed. Otherwise, it will be added.
     *
     * @param id the unique identifier of the tile to be added or removed from the*/
    public void setUncoveredTilesId(int id) {
        if (uncoveredTilesId.contains((Integer)id)) {
            uncoveredTilesId.remove(Integer.valueOf(id));
        }
        else{
            uncoveredTilesId.add(id);
        }

    }

    /**
     * Updates the uncovered tile set cache for a given index. If the cache parameter
     * is not null, it stores the cache array in the uncoverdTileSetCache map using the index.
     * If the cache parameter is null, it removes the entry for the specified index
     * from the uncoverdTileSetCache map.
     *
     * @param i the index at which the tile set cache is updated or removed
     * @param cache the array representing the uncovered tile set cache; if null, the existing entry is removed
     */
    public void setUncoverdTileSetCache(int i, String[] cache) {
        if (cache != null) {
            uncoverdTileSetCache.put((Integer)i, cache);
        }
        else{

            this.uncoverdTileSetCache.remove((Integer) i);
        }

    }

    /**
     * Sets the descriptions of the cards.
     *
     * @param cardsDescriptions a HashMap where the key is an Integer representing the card ID
     *                          and the value is a String containing the description of the card.
     */
    public void setCardsDescriptions(HashMap<Integer, String> cardsDescriptions) {
        CardsDescriptions = cardsDescriptions;
    }

    /**
     * Sets the gameboard at the specified coordinates with the provided cell data.
     *
     * @param x the x-coordinate of the gameboard.
     * @param y the y-coordinate of the gameboard.
     * @param cell the data to set at the specified coordinates on the gameboard.
     */
    public void setGameboard(int x, int y, String[] cell) {
        Gameboard[x][y] = cell;
    }

    /**
     * Sets the value of a specific cell in the gameboard.
     *
     * @param x   The x-coordinate of the cell on the gameboard.
     * @param y   The y-coordinate of the cell on the gameboard.
     * @param k   The depth or layer index of the cell in the gameboard.
     * @param cell The value to be assigned to the specified cell.
     */
    public void setGameboard(int x, int y, int k, String cell) {
        Gameboard[x][y][k] = cell;
    }

    /**
     * Sets the level value for the game or a particular component.
     *
     * @param lv the level value to be set.
     */
    public void setLv(int lv) {
        this.lv = lv;
    }

    /**
     * Sets the setup value for this instance.
     *
     * @param setup the integer value representing the setup configuration
     */
    public void setSetup(int setup) {
        this.setup = setup;
    }

//    public void setFase(boolean fase) {
//        this.fase = fase;
//    }

    /**
     * Sets the value of the covered tile set.
     *
     * @param coveredTileSet an integer representing the new value for the covered tile set
     */
    public void setCoveredTileSet(int coveredTileSet) {
        CoveredTileSet = coveredTileSet;
    }

//    public void setInputReader(InputReader inputReader) {
//        this.inputReader = inputReader;
//    }
//
//    public void setInputThread(Thread inputThread) {
//        this.inputThread = inputThread;
//    }
//
//    public void setConnected(Boolean connected) {
//        this.connected = connected;
//    }
//
//    public void setPositionToGameboard(int i, IntegerPair pair) {
//        positionToGameboard.put(i, pair);
//    }
//
//    public void setPlayerToPosition(HashMap<String, Integer> playerToPosition) {
//        PlayerToPosition = playerToPosition;
//    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    /**
     * Sets or updates the lobby for a specified game. If the provided cell data is null,
     * the game is removed from the lobby. Otherwise, the game ID and its associated cell data
     * are added or updated in the lobby.
     *
     * @param gameid The unique identifier for the game.
     * @param cell   An array of strings representing the cell data associated with the game.
     *               If null, the game will be removed from the lobby.
     */
    public void setLobby(String gameid, String[]cell) {
        if(cell == null){
            lobby.remove(gameid);
        }
        else{
            lobby.put(gameid,cell);
        }
    }


    /**
     * Sets the current phase of the view.
     *
     * @param phase the current phase to be set, represented as a {@code ViewPhase} enum.
     */
    public void setPhase(ViewPhase phase) {
        this.phase = phase;
    }


    /**
     * Retrieves the player's cached game board data.
     *
     * @return a three-dimensional array of Strings representing the cached board of the player.
     */
    public String[][][] getPlayerBoard() {
        return cachedBoard;
    }


    /**
     * Generates a StringBuilder representation of the current lobby state.
     * If the lobby is empty, a predefined ASCII art message indicating no games is appended.
     * Otherwise, it iterates through the lobby's contents and formats them into a structured output.
     *
     * @return A StringBuilder object containing the formatted representation of the lobby state.
     */
    public StringBuilder showLobby(){
        StringBuilder sb = new StringBuilder();

        int last = 0;
        int begin = 0;

        sb.append("\n\n");
        if (lobby.isEmpty()){
            sb.append(ASCII_ART.noGame);
        }

        else{

            int index = 0;
            while (index < lobby.size()) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 8 && index + j < lobby.size(); j++) {
                        sb.append(lobby.get(lobby.keySet().stream().toList().get(index + j))[i]).append("   ");
                    }
                    sb.append("\n");
                }
                sb.append("\n\n\n");
                index += 8;
            }


        }

        sb.append("\n\n");


        return sb;

    }


    /**
     * Generates a visual representation of the game board and returns it as a StringBuilder object.
     *
     * The method iterates through a predefined 2D cached board structure to construct a formatted
     * string representation of the board. Additional ASCII art elements, such as a border or header,
     * are appended to enhance visualization.
     *
     * @return a StringBuilder containing the formatted string representation of the game board.
     */
    public StringBuilder printBoard() {
        StringBuilder toPrint = new StringBuilder();
        int rows = 10;
        int cols = 10;
        toPrint.append("\n"+ASCII_ART.Board+"\n");
        toPrint.append("\n\n");
        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = cachedBoard[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    if (y > 2 && x > 1){
                        toPrint.append(formattedRow[x][i]);
                    }
                }
                if (y > 2 ){
                    toPrint.append("\n");
                }

            }
        }

        toPrint.append(ASCII_ART.Border);
        return toPrint;
    }


    /**
     * Formats and prints a three-dimensional board as a string representation.
     * Processes the given board array and appends formatted text to a StringBuilder
     * which is then returned. The output includes specified rows and columns of
     * the board formatted for display along with additional ASCII art borders.
     *
     * @param board a three-dimensional array representing the board, where the
     *              first dimension is rows, the second dimension is columns, and
     *              the third dimension contains string elements to be printed
     * @return a StringBuilder object containing the formatted board representation
     */
    public StringBuilder printBoard(String[][][] board) {
        StringBuilder toPrint = new StringBuilder();
        int rows = 10;
        int cols = 10;

        toPrint.append("\n\n");
        for (int y = 0; y < rows; y++) {


            String[][] formattedRow = new String[cols][];
            for (int x = 0; x < cols; x++) {
                formattedRow[x] = board[y][x];
            }

            for (int i = 0; i < 7; i++) {
                for (int x = 0; x < cols; x++) {
                    if (y > 2 && x > 1){
                        toPrint.append(formattedRow[x][i]);
                    }
                }
                if (y > 2 ){
                    toPrint.append("\n");
                }

            }
        }

        toPrint.append(ASCII_ART.Border);
        return toPrint;
    }


    /**
     * Constructs a visual representation of uncovered tiles using ASCII art.
     * The uncovered tiles are displayed in groups of eight, each tile formatted
     * with its respective position and visual representation from a cache.
     *
     * @return A StringBuilder containing the ASCII art representation of the uncovered tiles.
     */
    public StringBuilder showUncoveredTiles() {


        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.UncoveredTiles);

        int index = 0;
        while (index < uncoveredTilesId.size()) {
            StringBuilder topLine = new StringBuilder();

            for (int j = 0; j < 8 && index + j < uncoveredTilesId.size(); j++) {
                topLine.append("Position ").append(index + j).append("                          ");
            }
            toPrint.append(topLine).append("\n");


            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 8 && index + j < uncoveredTilesId.size(); j++) {
                    int id = uncoveredTilesId.get(index + j);
                    toPrint.append(uncoverdTileSetCache.get(id)[i]).append(" ");
                }
                toPrint.append("\n");
            }
            toPrint.append("\n\n\n");
            index += 8;
        }

        toPrint.append(ASCII_ART.Border);
        return toPrint;


    }


    /**
     * Builds and returns a StringBuilder object that represents the player's hand.
     * The method compiles the ASCII representation of the hand, along with a border
     * and any cached hand data, into a formatted structure suitable for display.
     *
     * @return a StringBuilder object containing the ASCII representation of the hand.
     */
    public StringBuilder printHand(){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.Hand);
        for (String l : cacheHand) toPrint.append("\n"+l);
        toPrint.append("\n"+border + "\n");
        toPrint.append(ASCII_ART.Border);
        return toPrint;
    }


    /**
     * Constructs and retrieves the visual representation of the game board, including ASCII art
     * and the current state of the game board based on the specified level and dimensions.
     *
     * @return A StringBuilder object containing the formatted visual representation of the game board.
     */
    public  StringBuilder printGameBoard(){

        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.GameBoard);
        if(Gameboard!= null){
            if (lv == 2){
                for (int i = 0; i < 6; i++) {

                    for (int k = 0; k < 7; k++) {
                        for (int j = 0; j < 12; j++) {
                            toPrint.append(Gameboard[i][j][k]);
                        }
                        toPrint.append("\n");
                    }

                }

            }
            else{
                for (int i = 0; i < 5; i++) {

                    for (int k = 0; k < 7; k++) {
                        for (int j = 0; j < 11; j++) {
                            toPrint.append(Gameboard[i][j][k]);
                        }
                        toPrint.append("\n");
                    }

                }
            }
        }


        toPrint.append(ASCII_ART.Border);
        return toPrint;


    }


    /**
     * Generates a formatted string that displays the list of players
     * and their readiness status.
     *
     * @return A StringBuilder containing the list of players along with
     *         their "Ready" or "Not Ready" status.
     */
    public StringBuilder showPlayers() {
        StringBuilder line = new StringBuilder();
                int i = 0;
                line.append("\n\n");
                while(i < players.size()){
                    line.append(players.get(i) + " : ");
                    if (ready.get(i)){
                        line.append("Ready");
                    }
                    else{
                        line.append("Not Ready");
                    }
                    i++;
                    line.append("          ");
                }
        return line;
    }


    /**
     * Displays the current game state to the player. This method toggles
     * the internal visibility state of the game, ensuring it is temporarily
     * hidden and then shown to the player via the player's client interface.
     * The player's client interface handles the rendering or processing
     * of the game display.
     */
    public void showGame(){
        show  = false;
        this.playerClient.showGame(this);
        show = true;

    }


    /**
     * Retrieves the mapping of positions to their corresponding gameboard coordinates.
     *
     * @return A HashMap where the key represents a position as an Integer
     *         and the value is an IntegerPair representing the coordinates on the gameboard.
     */
    public HashMap<Integer, IntegerPair> getPositionToGameboard() {
        return positionToGameboard;
    }


    /**
     * Initializes the gameboard and sets up the mapping of positions
     * to their corresponding gameboard coordinates based on the specified level.
     *
     * @param lv the level of the gameboard to initialize.
     *           If lv is 2, a 6x12x7 gameboard is created.
     *           If lv is 1, a 5x11x7 gameboard is created.
     */
    public void initGameBoard(int lv){
        this.lv = lv;
        if (lv == 2){
            Gameboard = new String[6][12][7];
            positionToGameboard.put(-1, new IntegerPair(-1,-1));
            positionToGameboard.put(0, new IntegerPair(0,2));
            positionToGameboard.put(1, new IntegerPair(0,3));
            positionToGameboard.put(2, new IntegerPair(0,4));
            positionToGameboard.put(3, new IntegerPair(0,5));
            positionToGameboard.put(4, new IntegerPair(0,6));
            positionToGameboard.put(5, new IntegerPair(0,7));
            positionToGameboard.put(6, new IntegerPair(0,8));
            positionToGameboard.put(7, new IntegerPair(0,9));
            positionToGameboard.put(8, new IntegerPair(1,10));
            positionToGameboard.put(9, new IntegerPair(2,11));
            positionToGameboard.put(10, new IntegerPair(3,11));
            positionToGameboard.put(11, new IntegerPair(4,10));
            positionToGameboard.put(12, new IntegerPair(5,9));
            positionToGameboard.put(13, new IntegerPair(5,8));
            positionToGameboard.put(14, new IntegerPair(5,7));
            positionToGameboard.put(15, new IntegerPair(5,6));
            positionToGameboard.put(16, new IntegerPair(5,5));
            positionToGameboard.put(17, new IntegerPair(5,4));
            positionToGameboard.put(18, new IntegerPair(5,3));
            positionToGameboard.put(19, new IntegerPair(5,2));
            positionToGameboard.put(20, new IntegerPair(4,1));
            positionToGameboard.put(21, new IntegerPair(3,0));
            positionToGameboard.put(22, new IntegerPair(2,0));
            positionToGameboard.put(23, new IntegerPair(1,1));
        }
        else if (lv == 1){
            Gameboard = new String[5][11][7];
            positionToGameboard.put(-1, new IntegerPair(-1,-1));
            positionToGameboard.put(0, new IntegerPair(0,3));
            positionToGameboard.put(1, new IntegerPair(0,4));
            positionToGameboard.put(2, new IntegerPair(0,5));
            positionToGameboard.put(3, new IntegerPair(0,6));
            positionToGameboard.put(4, new IntegerPair(0,7));
            positionToGameboard.put(5, new IntegerPair(0,8));
            positionToGameboard.put(6, new IntegerPair(1,9));
            positionToGameboard.put(7, new IntegerPair(2,10));
            positionToGameboard.put(8, new IntegerPair(3,9));
            positionToGameboard.put(9, new IntegerPair(4,8));
            positionToGameboard.put(10, new IntegerPair(4,7));
            positionToGameboard.put(11, new IntegerPair(4,6));
            positionToGameboard.put(12, new IntegerPair(4,5));
            positionToGameboard.put(13, new IntegerPair(4,4));
            positionToGameboard.put(14, new IntegerPair(4,3));
            positionToGameboard.put(15, new IntegerPair(3,2));
            positionToGameboard.put(16, new IntegerPair(2,1));
            positionToGameboard.put(17, new IntegerPair(1,0));

        }
    }


//    private void printTilesSet(){
//        showUncoveredTiles();
//    }


    /**
     * Sets the value of the cache card.
     *
     * @param s the new value to set for CacheCard
     */
    public void setCacheCard(String s){
        CacheCard = s;
    }


    /**
     * Sets the rewards information.
     *
     * @param rewards A StringBuilder object containing the rewards details to be set.
     */
    public void setRewards(StringBuilder rewards){
        Rewards = rewards;
    }


    /**
     * Generates a StringBuilder object that contains the ASCII art representation
     * of covered tiles and the size of the CoveredTileSet.
     *
     * @return a StringBuilder containing the ASCII art and the CoveredTileSet size
     */
    public StringBuilder showCovered(){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(ASCII_ART.CoveredTiles);
        toPrint.append("\n CoveredTileSet size: "+ CoveredTileSet);
        return toPrint;
    }


    /**
     * Displays the currently cached card along with associated ASCII art if available.
     * If the CacheCard is empty, the method will return an empty StringBuilder.
     *
     * @return a StringBuilder containing the ASCII art of a card followed by the cached card details,
     *         or an empty StringBuilder if no card is cached.
     */
    public StringBuilder showCard(){
        StringBuilder toPrint = new StringBuilder();

        if (!CacheCard.equals("")){
            toPrint.append("\n\n");
            toPrint.append(ASCII_ART.Card);
            toPrint.append("\n\n");
            toPrint.append("\n\n"+ CacheCard+ "\n\n");
        }

        return toPrint;
    }


//    public void printMessage(String s){
//        inputReader.printGraphicMessage(s);
//    }


    /**
     * Renders the content to the provided StringBuilder by delegating
     * the rendering task to the inputReader's renderScreen method.
     *
     * @param sb the StringBuilder instance to which the rendered content is appended
     */
    public void render(StringBuilder sb){
        inputReader.renderScreen(sb);
    }


    /**
     * Displays the current rewards information.
     *
     * @return a StringBuilder object containing the rewards details
     */
    public StringBuilder showRewards() {
        return Rewards;
    }


    /**
     * Sets the exception message.
     *
     * @param exception the exception message to be set
     */
    public void setException(String exception) {
        this.exception = exception;
    }


    /**
     * Handles and formats exception messages for output.
     * If an exception message is present, it wraps the message in a formatted
     * style and resets the exception to an empty state.
     *
     * @return A StringBuilder containing the formatted exception message, or an empty StringBuilder if no exception message is available.
     */
    public StringBuilder showException() {
        StringBuilder sb = new StringBuilder();
        if (!exception.equals("")){
            sb.append(Ansi.ansi().fgRed().a("[ " + exception + " ]").reset());
            sb.append("\n\n");
            exception = "";
        }
        return sb;
    }


//    public StringBuilder printSystemException(String s){
//        StringBuilder sb = new StringBuilder();
//
//
//        return sb;
//    }


    /**
     * Displays the game boards of all players, including the current player's board.
     *
     * The method iterates through the boards of other players and appends their
     * board representation with relevant details to a string. It includes a border
     * and player name for context. Once all other players' boards are displayed,
     * the current player's board is appended in a similar format. The complete
     * result is then rendered to the screen using the provided input reader.
     *
     * Uses ASCII art for board decoration and separation.
     */
    public void seeBoards() {
        StringBuilder sb = new StringBuilder();


        for (String player : otherPlayersBoard.keySet()){
            sb.append(ASCII_ART.Border);
            sb.append("\n\nPlayer: " +player);
            sb.append(printBoard(otherPlayersBoard.get(player)));
            sb.append(ASCII_ART.Border);
        }
        sb.append(printBoard());
        inputReader.renderScreen(sb);
    }


    /**
     * Sets a log message and adds it to the log list in a formatted manner.
     *
     * @param message the log message to set and add to the log list
     */
    public void setLog(String message) {
        this.effect = message;
        StringBuilder sb = new StringBuilder();
        sb.append(Ansi.ansi().fgYellow().a("[ " + effect + " ]").reset()+ "                                                                                                                                                                                                                                                         ");
        sb.append("\n\n");
        log.add(sb.toString());
    }


    /**
     * Displays the effect of a card if present. The effect is formatted with
     * ANSI yellow color and returned as a StringBuilder. Once the effect
     * is displayed, it is cleared.
     *
     * @return A StringBuilder containing the formatted effect text, or an empty
     *         StringBuilder if no effect is present.
     */
    public StringBuilder showCardEffect(){
        StringBuilder sb = new StringBuilder();
        if (!effect.equals("")){
            sb.append(Ansi.ansi().fgYellow().a("[ " + effect + " ]").reset());
            sb.append("\n\n");
            effect = "";
        }
        return sb;
    }


    /**
     * Retrieves the title card as a StringBuilder.
     *
     * @return a StringBuilder object containing the title card.
     */
    public StringBuilder getTitleCard(){
        return new StringBuilder(titleCard);
    }


    /**
     * Sets the PBInfo property with the specified value.
     *
     * @param s the new value to set for PBInfo
     */
    public void setPBInfo(String s) {
        PBInfo = s;
    }


    /**
     * Returns a StringBuilder object containing the PBInfo data.
     *
     * @return a StringBuilder object representing the PBInfo data
     */
    public StringBuilder showPbInfo(){
        return new StringBuilder(PBInfo);
    }


    /**
     * Sets the deck with the given list of IDs.
     *
     * @param ids the list of card IDs to set as the deck
     */
    public void setDeck(ArrayList<String> ids) {
        deck = ids;
    }


    /**
     * Displays the deck of cards in a formatted manner by aligning the contents of each card.
     * Each card is split into lines, and cards are displayed side-by-side until all lines are exhausted.
     * The deck is cleared after the operation.
     *
     * @return a StringBuilder containing the formatted representation of the deck. If the deck is empty, returns an empty StringBuilder.
     */
    public StringBuilder showDeck() {
        if (deck.isEmpty()){
            return new StringBuilder("");
        }
        ArrayList<String[]> splittedCards = new ArrayList<>();
        int maxLines = 0;

        for (String card : deck) {
            String[] lines = card.split("\n");
            splittedCards.add(lines);
            if (lines.length > maxLines) {
                maxLines = lines.length;
            }
        }

        StringBuilder combined = new StringBuilder();
        for (int i = 0; i < maxLines; i++) {
            for (int j = 0; j < splittedCards.size(); j++) {
                String[] cardLines = splittedCards.get(j);
                String line = i < cardLines.length ? cardLines[i] : "";
                combined.append(String.format("%-30s", line));
                if (j < splittedCards.size() - 1) {
                    combined.append("         ");
                }
            }
            combined.append("\n");


        }
        deck.clear();
        return combined;


    }


    /**
     * Sets the hourglass state with specified parameters.
     *
     * @param start   a boolean indicating whether to start or stop the hourglass effect
     * @param message a string message associated with the hourglass effect
     */
    public void setHourglass(boolean start, String message) {
        hourglass = start;
        effect = message;
    }


    /**
     * Generates a textual representation of an hourglass in ASCII art format.
     * The representation depends on the current state of the hourglass.
     *
     * @return A StringBuilder object containing the ASCII art representation
     * of either the starting or ending state of the hourglass.
     */
    public StringBuilder showHorglass(){
        StringBuilder sb = new StringBuilder();

        if (hourglass){
            sb = new StringBuilder(ASCII_ART.hourglassStart);
        }
        else{
            sb = new StringBuilder(ASCII_ART.hourglassEnd);
        }


        return sb;
    }


    /**
     * Compiles all log entries into a single StringBuilder object,
     * separating each entry with a newline character.
     *
     * @return a StringBuilder containing all log entries, with each entry separated by a newline.
     */
    public StringBuilder showLog() {
        StringBuilder sb = new StringBuilder();
        for (String s : log){
            sb.append(s);
            sb.append("\n");
        }
        return sb;
    }


    /**
     * Clears the contents of the lobby and log.
     * This method resets the state by removing all elements
     * from both the lobby and the log, ensuring they are empty.
     */
    public void clearOut() {
        lobby.clear();
        log.clear();
    }


    /**
     * Generates and returns a StringBuilder object containing the outcome.
     *
     * @return a StringBuilder instance that holds the outcome value.
     */
    public StringBuilder showOutcome() {
        return new StringBuilder(outcome);
    }


    /**
     * Sets the outcome message based on the provided parameters.
     *
     * @param message A string message (not used within the method logic).
     * @param outcome A boolean value representing the outcome;
     *                true for success ("hai vinto") and false for failure ("hai perso").
     */
    public void setOutcome(String message, boolean outcome) {
        if (outcome){
            this.outcome = ASCII_ART.win;
        }
        else{
            this.outcome = ASCII_ART.lose;
        }
    }

    /**
     * Sets the scoreboard with the provided StringBuilder.
     *
     * @param sb the StringBuilder object containing the scoreboard data
     */
    public void setScoreBoard(StringBuilder sb){
        scoreboard = sb;
    }

    /**
     * Displays the current scoreboard. If the scoreboard is null, an empty StringBuilder is returned.
     *
     * @return a StringBuilder object representing the scoreboard, or an empty StringBuilder if the scoreboard is null
     */
    public StringBuilder showScoreboard(){
        return Objects.requireNonNullElseGet(scoreboard, StringBuilder::new);
    }
}
