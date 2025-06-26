package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.ClientServer.Messages.*;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.*;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Controller.Listeners.*;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Model.Connectors.Connectors;
import org.example.galaxy_trucker.Model.Connectors.NONE;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The VirtualView class serves as a mediator between the client and the server, managing the
 * communication of game events and state changes. It implements various listener interfaces
 * to handle game-related updates, including player boards, hands, tiles, cards, game boards,
 * lobby changes, phase transitions, rewards, and exceptions.
 *
 * Fields:
 * - lv: Represents the game's current level or state.
 * - Disconnected: Indicates whether the player is disconnected.
 * - eventMatrix: Manages events associated with specific game levels.
 * - playerName: Stores the player's name.
 * - idGame: Identifier for the game session.
 * - client: The client interface used to communicate with the client.
 * - out: PrintWriter object for server-client communication.
 * - coveredTiles: Tiles that are currently covered in the game.
 * - uncoveredTilesMap: Stores information about uncovered tiles.
 * - hand: Represents the player's current hand of tiles or cards.
 * - token: Token for client authentication or re-connection.
 * - card: Represents a current or active card in the game.
 * - lobby: Represents data about the current game lobby.
 * - board: Represents the game board status.
 * - phase: Tracks the current phase of the game.
 * - rewardsEvent: Holds reward-related events in the game.
 * - playersPBListeners: Listeners for updates to players' personal boards.
 * - pbInfoEvent: Event representing information related to personal boards.
 * - logEvents: Log or record of received events.
 * - otherPlayerTileEvents: Tile-related events for other players.
 * - hourglassEvent: Represents time-related events such as a timer.
 *
 * Methods:
 * - setLv(int lv): Sets the current game level.
 * - VirtualView(String playerName, String idGame, ClientInterface client, PrintWriter echoSocket):
 *   Constructor to initialize VirtualView with essential details.
 * - setEventMatrix(int lv): Updates the event matrix for the specified level.
 * - sendEvent(HandEvent event): Sends hand-related events.
 * - sendEvent(PlayerTileEvent event): Sends tile-related events for a player.
 * - sendEvent(TileEvent event): Sends generic tile-related events.
 * - playerBoardChanged(TileEvent event): Handles updates to a player's board.
 * - PBInfoChanged(PBInfoEvent event): Handles changes in personal board information.
 * - handChanged(HandEvent event): Handles changes to the hand state.
 * - tilesSetChanged(CoveredTileSetEvent event): Handles changes to covered tile sets.
 * - tilesSetChanged(UncoverdTileSetEvent event): Handles changes to uncovered tile sets.
 * - seeDeck(DeckEvent event): Handles events related to seeing the deck.
 * - newCard(CardEvent event): Handles events when a new card appears.
 * - gameBoardChanged(GameBoardEvent event): Handles events related to game board updates.
 * - setDisconnected(boolean disconnected): Sets the disconnected status.
 * - getDisconnected(): Checks if the player is disconnected.
 * - reconnect(): Handles reconnection of a player to the server.
 * - setToken(String token): Sets the token used for client authentication.
 * - getToken(): Retrieves the authentication token.
 * - setPrintWriter(PrintWriter printWriter): Sets the PrintWriter for communication.
 * - setClient(ClientInterface client): Sets the client interface.
 * - GameLobbyChanged(GameLobbyEvent event): Handles changes in the game lobby.
 * - PhaseChanged(PhaseEvent event): Handles events for phase updates.
 * - sendHourglass(HourglassEvent event): Sends hourglass-related events.
 * - sendEvent(Event event): Sends a general event.
 * - rewardsChanged(RewardsEvent event): Handles changes in rewards for a player.
 * - exceptionOccured(ExceptionEvent event): Handles occurrences of exceptions.
 * - getPBlistener(): Retrieves the personal board listener for events.
 * - setPlayersPBListeners(PlayersPBListener listener): Sets the listener for personal board updates.
 * - updateOtherPlayers(TileEvent event): Sends updates about tiles for other players.
 * - receivePBupdate(PlayerTileEvent event): Handles updates to personal boards from other players.
 * - getPlayerName(): Retrieves the player's name.
 * - Effect(LogEvent event): Handles the effect of a random card or game event.
 * - removeListeners(): Removes all registered event listeners.
 * - removeListener(PlayersPBListener listener): Removes a specific personal board listener.
 * - sendLogEvent(LogEvent event): Sends log events for tracking purposes.
 *
 * Inheritance:
 * - Inherits from Object.
 * - Implements multiple listener interfaces for handling specific categories of game events.
 */
public class VirtualView implements PlayerBoardListener, HandListener, TileSestListener, CardListner, GameBoardListener, GameLobbyListener, PhaseListener, RewardsListener, ExceptionListener, PlayersPBListener, RandomCardEffectListener{

    /**
     * Represents the level or value associated with a specific context.
     * This variable is used to store an integer value which could denote
     * a level, stage, or any other numerical measurement depending on the
     * context in which it is utilized.
     */
    private int lv;
    /**
     * Represents the connection status of a system or component.
     * The value is {@code true} if disconnected, and {@code false} if connected.
     */
    private boolean Disconnected = false;
    /**
     * A two-dimensional array used to store TileEvent objects.
     * Represents a matrix where each element corresponds to an event
     * associated with specific tiles in a grid or map-like structure.
     * This is typically used to handle interactions or behaviors
     * associated with specific locations.
     */
    private TileEvent[][] eventMatrix;
    /**
     * Represents the name of a player in the system.
     * This variable is used to store and identify the player's name.
     */
    private String playerName;
    /**
     * Represents the unique identifier for a game.
     * This variable is used to distinguish one game from another within the system.
     */
    private String idGame;
    /**
     * Represents the client interface used to define and manage
     * client-specific operations and interactions.
     * This variable provides an abstraction for client handling,
     * enabling communication with the client-dependent systems
     * or services.
     */
    private ClientInterface client;
    /**
     * A PrintWriter object used for writing output data, typically to a file, socket, or other text-output streams.
     * This variable handles character-based output, allowing formatted text to be written efficiently.
     * It provides methods to write strings, characters, and other data types.
     */
    private PrintWriter out ;
    /**
     * Represents the number of tiles that have been covered, typically in a game or a grid-based application.
     * This variable is initialized to 0 and can be incremented as more tiles are covered.
     */
    private int coveredTiles= 0;
    /**
     * A map that associates an integer key with a list of Connectors.
     * This map is used to maintain information about tiles that
     * have not yet been covered or processed in the current context.
     *
     * The key represents an identifier for a specific grouping or category
     * of uncovered tiles, while the value is an ArrayList containing
     * Connectors associated with that key.
     */
    private HashMap<Integer, ArrayList<Connectors>> uncoveredTilesMap = new HashMap<>();
    /**
     * Represents an event or action associated with a hand.
     * This variable is likely used to capture or manage state
     * or interactions related to hand-based events in the application.
     */
    private HandEvent hand ;
    /**
     * A variable to store a token, which could represent an authentication credential,
     * access key, or identifier used for secure communication or access control in an application.
     */
    private String token;
    /**
     * Represents the card event associated with this context.
     * This variable holds the current state or action related to a card.
     * It is initialized to null and later assigned when a valid CardEvent is provided.
     */
    private CardEvent card = null;
    /**
     * Represents a game lobby event associated with the current state or interactions
     * within a game lobby. It may hold information about the event or act as a reference
     * point for handling specific actions within the lobby.
     */
    private GameLobbyEvent lobby = null;
    /**
     * Represents the game board, which is a list of events that occur on the board.
     * Each event is stored as an instance of {@code GameBoardEvent}.
     * Used to track and manage the state or actions occurring on the game board.
     */
    private ArrayList<GameBoardEvent> board = new ArrayList<>();
    /**
     * Represents the current phase event in a lifecycle or process.
     * This variable holds the state of the phase which can be used
     * to track or manipulate the ongoing phase of the system.
     * It is initialized to null by default.
     */
    private PhaseEvent phase = null;
    /**
     * Represents an instance of a RewardsEvent object which may be used to track or manage
     * events related to rewards within the system. This variable is initialized as null
     * and should be assigned with a valid RewardsEvent object before use.
     */
    private RewardsEvent rewardsEvent = null;
    /**
     * A list of listeners that are interested in player personal best (PB) events.
     * This collection stores instances of classes implementing the PlayersPBListener interface,
     * allowing them to receive updates or notifications regarding changes or updates to player
     * personal best data.
     */
    private ArrayList<PlayersPBListener> playersPBListeners = new ArrayList<>();
    /**
     * An instance of PBInfoEvent that stores event-related information.
     * This variable is initialized to null and can be set or accessed
     * to manage or retrieve specific PBInfoEvent data when required.
     */
    private PBInfoEvent pbInfoEvent = null;
    /**
     * A list that stores log events of type LogEvent.
     * This list is used to collect and manage instances of log events,
     * which can represent various logging information or activities
     * within an application.
     */
    private ArrayList<LogEvent> logEvents = new ArrayList<>();
    /**
     * Represents a collection of tile events associated with other players.
     * Each event in the list is an instance of PlayerTileEvent, which likely contains
     * information related to a specific interaction or occurrence on a game tile
     * caused by or involving other players.
     */
    private ArrayList<PlayerTileEvent> otherPlayerTileEvents = new ArrayList<>();
    /**
     * Represents an instance of HourglassEvent which is initialized to null.
     * This variable may be used to store or manage an event related to the "hourglass" concept
     * in the application. The specific use or manipulation of this variable is determined
     * by the context in which it is employed.
     */
    private HourglassEvent hourglassEvent = null;

    /**
     * Sets the level for the VirtualView instance.
     *
     * @param lv the level to be set for this instance
     */
    public void setLv(int lv){
        this.lv = lv;
    }


    /**
     * Constructs a new VirtualView instance with the specified player name, game ID, client interface, and PrintWriter.
     *
     * @param playerName the name of the player associated with this virtual view
     * @param idGame the identifier of the game to which this virtual view belongs
     * @param client the client interface used for communication
     * @param echoSocket the PrintWriter used for sending messages or logs
     */
    public VirtualView(String playerName, String idGame, ClientInterface client, PrintWriter echoSocket) {
        this.playerName = playerName;
        this.idGame = idGame;
        this.client = client;
        eventMatrix = new TileEvent[10][10];
        this.out = echoSocket;
    }


    /**
     * Configures the event matrix based on the provided level parameter.
     * The method populates a predefined grid with `TileEvent` objects,
     * determining each event's specific properties based on the layout and the given level.
     * After setting the event matrix, the events are sent for further processing.
     *
     * @param lv the level used to determine the configuration of the event matrix.
     *           The values affect how events are mapped to the grid.
     */
    public void setEventMatrix(int lv) {
        ArrayList<Connectors> noneConnectors = new ArrayList<>();
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        noneConnectors.add(NONE.INSTANCE);
        if (lv == 2) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (x < 4 || y < 3 || (x == 4 && (y == 3 || y == 4 || y == 6 || y == 8 || y == 9)) ||(x == 5 && (y == 3 || y== 9)) || (x == 8 && y == 6) || x ==9) {

                        eventMatrix[x][y] = new TileEvent(158,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);

                    }

                    else {
                        eventMatrix[x][y] = new TileEvent(157,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);

                    }

                }
            }
        }
        else {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if(y <= 3 ||x == 9 || y == 9|| x <4 || ( x == 4 && (y != 6)) || (x== 5 &&(y <5 || y>7)) || (x==8 && y == 6))  {
                        eventMatrix[x][y] = new TileEvent(158,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);
                    }

                    else {
                        eventMatrix[x][y] = new TileEvent(157,x,y,null,0,false,false,0,0,noneConnectors);
                        //updateOtherPlayers(eventMatrix[x][y]);
                    }

                }
            }

        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                sendEvent(eventMatrix[x][y]);

            }
        }
    }


    /**
     * Sends a HandEvent to the connected client or writes it to the output stream.
     * If the client is not disconnected and an output stream or a client interface
     * is available, the event will be serialized and sent accordingly.
     *
     * @param event the HandEvent instance to be sent. Contains information about
     *              the hand including its ID and connectors.
     *               - If the output stream (`out`) is present, the event is serialized
     *                 to JSON and sent to the stream.
     *               - If the client interface (`client`) is present, the event is sent
     *                 using the `receiveMessage` method.
     */
    public void sendEvent(HandEvent event)  {
        hand = event;
        if (!Disconnected) {
            if (out != null ) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Sends a PlayerTileEvent to the appropriate destination. If the current
     * state indicates that the connection is not disconnected, it attempts to
     * send the event either via a PrintWriter or through a ClientInterface
     * method. Serializes the event into JSON format when using the PrintWriter.
     *
     * @param event The PlayerTileEvent object containing information about the event
     *              to be sent. It holds data such as player details and tile
     *              information.
     */
    public void sendEvent(PlayerTileEvent event){

        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Sends a TileEvent to the appropriate destination based on the connection state
     * and available communication channels. If the connection is active and valid
     * channels are present, the event is handled accordingly:
     * - If a PrintWriter (`out`) is available, the event is serialized into JSON and sent using the PrintWriter.
     * - If a ClientInterface (`client`) is available, the event is sent through the `receiveMessage`
     *   method of the client.
     *
     * @param event The TileEvent object containing information about the event to be sent.
     *              This includes details such as location, connectors, cargo, and specific
     *              properties related to the event's context.
     */
    public void sendEvent(TileEvent event) {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    /**
     * Handles updates to the player's board based on the provided {@code TileEvent}.
     * This method updates the internal event matrix with the new event, notifies
     * other players about the change, and sends the event for further processing.
     *
     * @param event the {@code TileEvent} representing the change on the player's board.
     *              It contains the updated tile's properties such as position, cargo,
     *              and other attributes.
     */
    @Override
    public void playerBoardChanged(TileEvent event) {
        eventMatrix[event.getX()][event.getY()] = event;
        updateOtherPlayers(event);
        sendEvent(event);
    }


    /**
     * Handles changes to the Player Board (PB) information and processes the corresponding event.
     * Updates the current PBInfoEvent instance and sends the event to its designated recipient.
     *
     * @param event the PBInfoEvent instance that contains updated player board information,
     *              such as credits, energy, damage, shield status, and other related data.
     */
    @Override
    public void PBInfoChanged(PBInfoEvent event) {
        this.pbInfoEvent = event;
        sendEvent(event);
    }


    /**
     * Updates the current hand state based on the provided HandEvent and triggers the corresponding event handling process.
     * The method replaces the current hand reference with the new HandEvent and ensures that the event is propagated.
     *
     * @param event the HandEvent instance containing information about the updated state of the hand,
     *              including its ID and connectors.
     */
    @Override
    public void handChanged(HandEvent event)  {
        hand = event;
        sendEvent(event);
    }


    /**
     * Handles changes to the covered tile set. When a change occurs, this method processes the
     * {@link CoveredTileSetEvent} and forwards the event to the appropriate destination based on
     * the connection state and available communication channels.
     *
     * If the connection is active:
     * - If a PrintWriter (`out`) is available, the event is serialized into JSON format and sent using the PrintWriter.
     * - If a ClientInterface (`client`) is available, the event is sent using the `receiveMessage` method of the client.
     *
     * @param event the {@link CoveredTileSetEvent} that represents the change to the covered tile set.
     *              It contains information such as the size of the tile set.
     */
    @Override
    public void tilesSetChanged(CoveredTileSetEvent event)  {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Handles changes in the uncovered tile set by processing the provided event.
     * Sends the serialized event to a connected client or writes it to an output stream.
     * If the client is not connected, no action is performed.
     *
     * @param event the {@link UncoverdTileSetEvent} representing changes in the uncovered tile set.
     *              Contains an identifier and a list of connectors.
     * @throws RemoteException if there is an error communicating with the client interface.
     */
    @Override
    public void tilesSetChanged(UncoverdTileSetEvent event) throws RemoteException {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Sends a DeckEvent to the appropriate destination based on the connection state
     * and available communication channel. If the connection is not marked as disconnected,
     * the event will be either serialized to JSON and written to an output stream, or
     * sent directly to a client interface.
     *
     * @param event the DeckEvent instance to be processed. The event contains a list
     *              of card IDs representing the current deck state.
     */
    @Override
    public void seeDeck(DeckEvent event) {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {
                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Handles the arrival of a new card event and processes it based on the current
     * state and available communication channels.
     *
     * If the connection is not disconnected (`Disconnected` is false) and appropriate
     * output channels are available, the method forwards the event:
     * - If a `PrintWriter` (`out`) is present, the event is serialized into JSON
     *   format and sent using the `PrintWriter`.
     * - If a `ClientInterface` (`client`) is present, the event is sent via the
     *   `receiveMessage` method of the client.
     *
     * @param event the {@code CardEvent} instance representing the new card information.
     *              This event contains attributes such as the card's identifier and
     *              implements the {@code Event} interface for compatibility with other
     *              system components.
     */
    @Override
    public void newCard(CardEvent event) {
        card = event;
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Handles changes to the game board when a {@code GameBoardEvent} occurs.
     * The method updates the game board state internally, logs or sends the event
     * via the appropriate communication channel based on the connection status and
     * availability of resources.
     *
     * @param event the {@code GameBoardEvent} containing information about the changes
     *              to the game board. Includes attributes such as position and player ID.
     */
    @Override
    public void gameBoardChanged(GameBoardEvent event)  {
        board.add(event);
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Sets the disconnected status of the object.
     *
     * @param disconnected a boolean value indicating the disconnected status;
     *                     true if the object is disconnected, false otherwise.
     */
    public void setDisconnected(boolean disconnected) {
        Disconnected = disconnected;
    }


    /**
     * Indicates whether the connection is currently disconnected.
     *
     * @return true if the connection is disconnected, false otherwise.
     */
    public boolean getDisconnected(){
        return Disconnected;
    }


    /**
     * Reconnects the client or player to the game session by re-synchronizing all relevant game
     * events and states. This method ensures that the game state is accurately restored
     * to the player upon reconnection.
     *
     * The following actions are performed in sequence to reconstruct the game state:
     * 1. A {@link ReconnectedEvent} is sent with essential player and game information.
     * 2. If the player currently holds a card, it triggers the handling of the new card.
     * 3. All previous log events are resent to recreate the game's historical timeline for the player.
     * 4. Other players' tile-related events are resent to ensure awareness of their states.
     * 5. The event matrix, representing game-specific updates on a 10x10 grid, is iterated through
     *    and all events are resent.
     * 6. If a pending pbInfoEvent exists, it is sent to update pending bonus or additional information.
     * 7. If the player has any hand data (e.g., cards or resources), it is resent.
     * 8. If a lobby exists, its state is updated and sent back to the client.
     * 9. Game board events are sent to restore the state of the game board as observed by the player.
     * 10. Any rewards-related updates or changes (if present) are synchronized and sent.
     * 11. The hourglass state (e.g., time-related mechanics) is resent if applicable.
     * 12. Finally, the current game phase event is sent to synchronize the player's understanding
     *     of the game's current stage or flow.
     *
     * Precondition:
     * - This method assumes that all relevant game state information (events and properties)
     *   has been initialized appropriately before invoking this function.
     *
     * Postcondition:
     * - The player or client receives all events necessary to reconstruct the game state and
     *   resume from the correct point within the game.
     *
     * Implementation Note:
     * - The method iterates through collections and matrices to resend events and should be
     *   optimized for performance in games with a large number of events or complex states.
     */
    public void reconnect() {

        sendEvent(new ReconnectedEvent(token,idGame,playerName,lv));

        if (card != null){
            newCard(card);
        }

        for (LogEvent log : logEvents){
            sendEvent(log);
        }

        for (PlayerTileEvent playerTileEvent : otherPlayerTileEvents){
            sendEvent(playerTileEvent);
        }

        for (int i = 0; i < 10; i ++){
            for(int j = 0; j < 10; j ++){
                sendEvent(eventMatrix[i][j]);
            }
        }

        if (pbInfoEvent != null){
            sendEvent(pbInfoEvent);
        }

        if(hand != null) {
            sendEvent(hand);
        }

        if (lobby != null){
            sendEvent(lobby);
        }

        for (GameBoardEvent gbEvent : board){
            sendEvent(gbEvent);
        }

        if (rewardsEvent != null){
            sendEvent(rewardsEvent);
        }
        if (hourglassEvent != null){
            sendEvent(hourglassEvent);
        }

        if (phase != null){
            sendEvent(phase);
        }

    }


    /**
     * Sets the value of the token.
     *
     * @param token the new value for the token
     */
    public void setToken(String token) {
        this.token = token;
    }


    /**
     * Retrieves the token.
     *
     * @return the token as a String
     */
    public String getToken() {
        return token;
    }


    /**
     * Sets the PrintWriter instance to be used for output operations.
     *
     * @param printWriter the PrintWriter object to set
     */
    public void setPrintWriter(PrintWriter printWriter) {
        this.out = printWriter;
    }


    /**
     * Sets the client instance for this object.
     *
     * @param client the client object to be set, implementing the ClientInterface
     */
    public void setClient(ClientInterface client){
        this.client = client;
    }


    /**
     * Handles changes to the game lobby by updating the local state and notifying the connected client.
     *
     * @param event The GameLobbyEvent object containing details about the changes in the game lobby.
     */
    @Override
    public void GameLobbyChanged(GameLobbyEvent event) {
        lobby = event;
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Handles the change of phase by updating the current phase and sending the event to listeners.
     *
     * @param event the PhaseEvent that contains details about the phase change
     */
    @Override
    public void PhaseChanged(PhaseEvent event) {
        phase = event;
        sendEvent(event);
    }

    /**
     * Sends the specified HourglassEvent.
     *
     * @param event the HourglassEvent to be sent
     */
    public void sendHourglass(HourglassEvent event){
        hourglassEvent = event;
        sendEvent(event);
    }

    /**
     * Sends an event to the appropriate output channel.
     * If the `out` stream is available, the event is serialized to JSON and sent through the stream.
     * Otherwise, if a `client` is available, the event is sent to the client via the `receiveMessage` method.
     *
     * @param event The event object to be sent. It should be a serializable object to allow conversion to JSON.
     *
     * Throws a {@link RuntimeException} if a `RemoteException` occurs when sending the event to the client.
     */
    public void sendEvent(Event event) {
        if (!Disconnected) {
            if (out != null) {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    out.println(objectMapper.writeValueAsString(event));
                    //System.out.println("Send: "+objectMapper.writeValueAsString(event));
                }
                catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(client!= null) {

                try {
                    client.receiveMessage(event);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * Handles the event when rewards are changed.
     *
     * @param event the rewards event containing details about the change
     */
    @Override
    public void rewardsChanged(RewardsEvent event) {
        rewardsEvent = event;
        sendEvent(event);
    }


    /**
     * Handles an exception event by processing it.
     *
     * @param event the exception event that occurred and needs handling
     */
    @Override
    public void exceptionOccured(ExceptionEvent event) {
        sendEvent(event);
    }


    /**
     * Retrieves the PlayersPBListener instance.
     *
     * @return the current instance of PlayersPBListener.
     */
    public PlayersPBListener getPBlistener(){
        return this;
    }


    /**
     * Registers a listener to receive player tile updates and immediately sends all current tile events to the listener.
     *
     * @param listener the PlayersPBListener that will be added and notified of updates.
     */
    public void setPlayersPBListeners(PlayersPBListener listener){
        this.playersPBListeners.add(listener);
        for (int i = 0; i < 10; i ++){
            for (int j = 0; j < 10; j ++){
                PlayerTileEvent newEvent = new PlayerTileEvent(playerName,eventMatrix[i][j].getId(),eventMatrix[i][j].getX(), eventMatrix[i][j].getY(),eventMatrix[i][j].getCargo(),eventMatrix[i][j].getHumans()
                        ,eventMatrix[i][j].isPurpleAlien(),eventMatrix[i][j].isBrownAlien(), eventMatrix[i][j].getBatteries(),eventMatrix[i][j].getRotation(),eventMatrix[i][j].getConnectors());
                listener.receivePBupdate(newEvent);
            }
        }
    }


    /**
     * Updates other players with the provided tile event information by transforming
     * the event into a PlayerTileEvent and notifying all registered listeners.
     *
     * @param event the tile event containing the details to be shared with other players,
     *              including position, cargo, humans, alien types, batteries, rotation,
     *              and connectors.
     */
    public void updateOtherPlayers(TileEvent event){
        PlayerTileEvent newEvent = new PlayerTileEvent(playerName,event.getId(),event.getX(), event.getY(),event.getCargo(),event.getHumans()
        ,event.isPurpleAlien(),event.isBrownAlien(), event.getBatteries(),event.getRotation(),event.getConnectors());
        for (PlayersPBListener listener : playersPBListeners){
            listener.receivePBupdate(newEvent);
        }
    }


    /**
     * Handles the receipt of a player tile update event by adding it to the collection
     * of other player tile events and sending the event further for processing.
     *
     * @param event the PlayerTileEvent instance containing information about the update
     */
    @Override
    public void receivePBupdate(PlayerTileEvent event){
        //System.out.println("adding player tile event "+ event.getPlayerName()+ " "+ event.getId());
        otherPlayerTileEvents.add(event);
        sendEvent(event);
    }


    /**
     * Retrieves the name of the player.
     *
     * @return the name of the player as a String
     */
    public String getPlayerName(){
        return playerName;
    }


    /**
     * Processes the given log event by adding it to the log event list
     * and sending it for further handling.
     *
     * @param event the log event to be processed
     */
    @Override
    public void Effect(LogEvent event) {
        logEvents.add(event);
        sendEvent(event);
    }


    /**
     * Removes all listeners from the playersPBListeners collection.
     * This method clears the list of listeners, ensuring no further actions
     * are triggered on this collection.
     */
    public void removeListeners(){
        this.playersPBListeners.clear();
    }


    /**
     * Removes the specified listener from the list of registered players' personal
     * best (PB) listeners. After removal, the listener will no longer receive updates
     * or notifications related to players' PB changes.
     *
     * @param listener the PlayersPBListener instance to be removed from the list of
     *                 registered listeners
     */
    public void removeListener(PlayersPBListener listener){
        this.playersPBListeners.remove(listener);
    }

    /**
     * Sends a log event by adding it to the internal log event list and forwarding the event.
     *
     * @param event the log event to be sent
     */
    public void sendLogEvent(LogEvent event) {
        logEvents.add(event);
        sendEvent(event);
    }


//    public void sendEvent(LogEvent event){
//        logEvents.add(event);
//        sendEvent(event);
//    }

}
