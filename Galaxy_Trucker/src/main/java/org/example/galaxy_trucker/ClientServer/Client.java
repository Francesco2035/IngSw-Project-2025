package org.example.galaxy_trucker.ClientServer;

import org.example.galaxy_trucker.ClientServer.Messages.*;
import org.example.galaxy_trucker.ClientServer.Messages.TileSets.*;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.ClientServer.RMI.RMIClient;
import org.example.galaxy_trucker.ClientServer.TCP.TCPClient;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.PlayerTileEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RewardsEvent;
import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.View.ClientModel.States.LoginClient;
import org.example.galaxy_trucker.View.GUI.GuiRoot;
import org.example.galaxy_trucker.View.TUI.TUI;
import org.example.galaxy_trucker.View.View;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.HashMap;

/**
 * The Client class is responsible for handling the communication between the client
 * and the server, managing the player's view and events, and coordinating game logic
 * on the client side. It implements the EventVisitor interface allowing the client
 * to react to various events.
 *
 * It supports both RMI and TCP communication protocols and provides functionalities
 * such as updating the board, managing game states, and processing events from the server.
 *
 * The Client also initializes different types of views (GUI or TUI) and orchestrates
 * the game flow and interactions between the user interface, the server, and the game logic.
 */
public class Client implements EventVisitor {

    /**
     * Represents a visual component or an element in the user interface.
     * This variable is used to reference and interact with a specific UI view element.
     */
    private View view;
    /**
     * Represents an instance of the RMIClient, used to establish
     * and manage remote method invocation communication.
     * This variable typically facilitates interaction with a remote server
     * by invoking methods and transferring data.
     */
    private RMIClient rmiClient;
    /**
     * An instance of the TCPClient class responsible for managing
     * TCP connections and communication. This variable is used to
     * handle interactions over a TCP network, including sending
     * and receiving data. It encapsulates the behavior and functionality
     * required for establishing and maintaining a TCP connection.
     */
    private TCPClient tcpClient;
    /**
     * A 2D array representing the game board. Each element is a {@code TileEvent},
     * which defines the state or event associated with a specific tile on the board.
     * The first dimension represents the rows, and the second dimension represents the columns.
     */
    private TileEvent[][] board;
    /**
     * Represents a token string used for authentication or identification purposes.
     * This field typically stores encoded or raw token data.
     */
    private String token;
    /**
     * Represents the login status of a user.
     * This variable indicates whether the user is currently logged in.
     * A value of {@code true} means the user is logged in, while
     * {@code false} indicates the user is not logged in.
     */
    private boolean login = false;
    /**
     * A boolean variable indicating the state of the lobby.
     * When set to true, it signifies that the lobby is active or in use.
     * When set to false, it indicates that the lobby is inactive or not in use.
     */
    private boolean lobby = false;
    /**
     * An instance of the LoginClient class used to handle client login functionality.
     * This variable is declared as final, meaning its reference cannot be changed
     * after initialization. It is intended to encapsulate all operations and interactions
     * related to user authentication and login management within the application.
     */
    private final LoginClient loginClient = new LoginClient();
    /**
     * A mapping of game IDs to their corresponding level values.
     *
     * This HashMap is used to associate a unique game identifier (String)
     * with an integer value representing the level or rank of the game.
     * It provides a way to track or query the level of a game by its ID.
     */
    HashMap<String, Integer> gameidToLV = new HashMap<>();
    /**
     * The commandInterpreter is responsible for interpreting and executing commands
     * within the application. It serves as a central processing unit for parsing
     * commands, determining their intent, and invoking the appropriate functionality
     * based on the input provided.
     */
    CommandInterpreter commandInterpreter;


    /**
     * Retrieves the login status of the client.
     *
     * @return true if the client is logged in; false otherwise.
     */
    public boolean getLogin(){
        return login;
    }

    /**
     * Sets the login status of the client.
     *
     * @param login the new login status to be set. True indicates the client is logged in,
     *              while false indicates the client is logged out.
     */
    public void setLogin(boolean login){
        this.login = login;
    }

    /**
     * Retrieves the current status of the lobby.
     *
     * @return a boolean indicating the state of the lobby.
     *         Returns true if the lobby is active, otherwise false.
     */
    public boolean getLobby(){
        return lobby;
    }

    /**
     * Sets the lobby status for the client.
     *
     * @param lobby a boolean value indicating the lobby state.
     *              If {@code true}, the client is in the lobby;
     *              if {@code false}, the client is not in the lobby.
     */
    public void setLobby(boolean lobby){
        this.lobby = lobby;
    }


    /**
     * Checks whether a specific game ID is present in the gameidToLV map.
     *
     * @param gameId the ID of the game to check for in the map.
     * @return {@code true} if the game ID is present in the map, {@code false} otherwise.
     */
    public synchronized boolean containsGameId(String gameId) {
        //System.out.println(this);

        return gameidToLV.containsKey(gameId);
    }

    /**
     * Retrieves the level associated with the specified game ID.
     * This method is thread-safe and ensures synchronized access to the underlying data structure.
     *
     * @param gameId the unique identifier of the game whose level is to be retrieved
     * @return the level associated with the given game ID, or null if the game ID does not exist in the mapping
     */
    public synchronized int getLevel(String gameId) {
        //System.out.println(this);

        return gameidToLV.get(gameId);
    }

    /**
     * Associates a game ID with a specific level in the internal mapping structure.
     * If the game ID is not already present in the mapping, it will be added along with the specified level.
     *
     * @param gameid the unique identifier of the game to associate with a level.
     * @param lv the level to be associated with the specified game ID.
     */
    public synchronized void setGameIdToLV(String gameid, int lv) {
        //System.out.println(this);

        //System.out.println(gameid + " " + lv);
        this.gameidToLV.putIfAbsent(gameid, lv);
        //System.out.println(gameid + " " + gameidToLV.get(gameid)+" size: "+gameidToLV.size());
    }

    /**
     * Retrieves the mapping of game IDs to their associated levels.
     * This method provides a thread-safe access to the internal data structure.
     *
     * @return a HashMap where the keys are game IDs (strings) and the values are the corresponding levels (integers)
     */
    public synchronized HashMap<String, Integer> getGameidToLV() {
        return gameidToLV;
    }

    /**
     * Default constructor for the Client class.
     * Initializes the game board represented as a 10x10 array of {@link TileEvent}.
     */
    public Client() {
        board = new TileEvent[10][10];
    }

    /**
     * Initializes and starts the RMI client for the application.
     * The method sets up the RMIClient instance and invokes its `StartClient` method to establish
     * the RMI connection necessary for client-server communication.
     *
     * @throws IOException if an I/O error occurs during the RMI client setup.
     * @throws NotBoundException if the RMI registry does not contain a binding for the expected remote object.
     * @throws InterruptedException if the thread executing the method is interrupted.
     */
    public void startRMIClient() throws IOException, NotBoundException, InterruptedException {
        //this.commandInterpreter = new CommandInterpreter();
        //String ip = NetworkUtils.getLocalIPAddress();
        //System.setProperty("java.rmi.server.hostname", ip);
        //System.out.println("RMI hostname set to: " + ip);
        rmiClient = new RMIClient(this);
        rmiClient.StartClient();
    }

    /**
     * Initializes and starts the TCP client for the application.
     * This method creates a new instance of the TCPClient using the current client instance
     * and invokes its startClient method to establish and manage the connection.
     *
     * @throws IOException if an I/O error occurs while starting the TCP client.
     */
    private void startTCPClient() throws IOException {
        //this.commandInterpreter = new CommandInterpreter();
        tcpClient = new TCPClient(this);
        tcpClient.startClient();
    }




    /**
     * Launches the client application, prompting the user to configure server connection
     * settings and interface preferences. This method interacts with the user through the
     * console for input, establishes the appropriate connection type (TCP or RMI), and
     * initializes the selected user interface (GUI or TUI).
     *
     * @throws Exception if any error occurs during the execution of the method,
     *                   including IO operations or client setup.
     */
    public  void run() throws Exception {
        Terminal terminal = TerminalBuilder.builder().build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        //view = new TUI();
        Settings.setIp(reader.readLine("Please enter server ip address: "));
        String Connection = "";
        while(!Connection.equals("TCP") && !Connection.equals("RMI")) {

            Connection = reader.readLine("Please enter type of connection <TCP|RMI> [exit]: ").toUpperCase();
            if (Connection.equals("EXIT")) {
                System.exit(1);
            }

        }
        String view1 = "";
        while(!view1.equals("GUI") && !view1.equals("TUI")) {
            view1 = reader.readLine("Choose GUI | TUI [exit]: ").toUpperCase();
            if (view1.equals("EXIT")) {
                System.exit(1);
            }
        }
        terminal.close();


        Client client = this;

        if (view1.equals("TUI")) {
            TUI tui = new TUI(loginClient);
            tui.setClient(client);
            client.setView(tui);
        } else if (view1.equals("GUI")){
            GuiRoot gui = new GuiRoot(loginClient);
            client.setView(gui);
        }

        if (Connection.equals("RMI")) {

            client.startRMIClient();
        } else if (Connection.equals("TCP")) {
             client.startTCPClient();
        }
    }


    /**
     * Retrieves the current view associated with the client.
     *
     * @return the current {@code View} instance representing the client's interface.
     */
    public View getView() {
        return view;
    }


    /**
     * Sets the view associated with the client.
     * This method updates the internal reference to the view to the provided instance.
     *
     * @param view the view instance to be associated with the client
     */
    public void setView(View view) {
        this.view = view;
    }


    /**
     * Receives and processes an event by allowing it to invoke its corresponding
     * visitor method on this client instance. The event is expected to implement
     * the Visitor design pattern, invoking its {@code accept} method with the
     * current object as the visitor.
     *
     * @param event the event to be processed. This event must be a subclass of
     *              {@code Event} and is expected to handle its behavior using
     *              the Visitor design pattern by delegating control to the
     *              appropriate visit method on the current object.
     */
    public void receiveEvent(Event event) {
        event.accept(this);
    }

    /**
     * Handles the visit for a {@code LobbyEvent} instance.
     * This method displays the lobby associated with the provided event
     * using the client's view.
     *
     * @param event the {@code LobbyEvent} instance to be processed,
     *              containing data about the lobby to display.
     */
    @Override
    public void visit(LobbyEvent event){this.view.showLobby(event);}

    /**
     * Handles the visit for a {@code PhaseEvent}.
     * This method is invoked when a {@code PhaseEvent} is processed,
     * and it delegates the phase change handling to the client's view.
     *
     * @param event the {@code PhaseEvent} to be handled,
     *              containing details regarding the phase change.
     */
    @Override
    public void visit(PhaseEvent event) {
        //System.out.println("------------------------------------------------------------------------------------nuove phase "+event.getStateClient().getClass());
        this.view.phaseChanged(event);
    }

    /**
     * Handles the visit operation for a {@code RewardsEvent} instance.
     * This method updates the client's view to reflect the changes indicated by the rewards event.
     *
     * @param rewardsEvent the {@code RewardsEvent} instance containing the details
     *                     of the rewards that have changed or been updated.
     */
    @Override
    public void visit(RewardsEvent rewardsEvent) {this.view.rewardsChanged(rewardsEvent);}

    /**
     * Handles the visit for an {@code ExceptionEvent} instance.
     * This method delegates the exception handling to the client's view.
     *
     * @param exceptionEvent the {@code ExceptionEvent} instance to be processed,
     *                       containing details about the exception that occurred.
     */
    @Override
    public void visit(ExceptionEvent exceptionEvent) {
        this.view.exceptionOccurred(exceptionEvent);
    }

    /**
     * Handles the visit for a {@code PlayerTileEvent}.
     * This method updates the view to reflect changes related to other players' progress
     * as indicated by the {@code PlayerTileEvent}.
     *
     * @param playerTileEvent the {@code PlayerTileEvent} instance containing
     *                        data about updates to other players' progress or tiles.
     */
    @Override
    public void visit(PlayerTileEvent playerTileEvent) {
        this.view.updateOthersPB(playerTileEvent);
    }

    /**
     * Handles the visit operation for a {@code LogEvent} instance.
     * This method triggers the client's view to apply the effect associated
     * with the provided log event.
     *
     * @param event the {@code LogEvent} instance containing the details
     *              of the log message or action to be displayed or executed.
     */
    @Override
    public void visit(LogEvent event) {
        this.view.effectCard(event);
    }

    /**
     * Handles the visit for a {@code ConnectionRefusedEvent} instance.
     * This method sets the client's login status to {@code false} and
     * delegates the exception handling to the client's view.
     *
     * @param event the {@code ConnectionRefusedEvent} instance to be processed,
     *              containing details about the connection refusal.
     */
    @Override
    public void visit(ConnectionRefusedEvent event) {
        login = false;
        this.view.exceptionOccurred(new ExceptionEvent(event.message()));
    }

    /**
     * Handles the visit operation for a {@code PBInfoEvent} instance.
     * This method updates the client's view with information encapsulated in the {@code PBInfoEvent}.
     *
     * @param event the {@code PBInfoEvent} instance containing the relevant information
     *              to be presented or processed by the client's view.
     */
    @Override
    public void visit(PBInfoEvent event) {
        this.view.updatePBInfo(event);
    }

    /**
     * Handles the visit operation for a {@code QuitEvent} instance.
     * This method updates the client's login and lobby status to reflect
     * that the user has quit, and notifies the view of the phase change.
     *
     * @param quitEvent the {@code QuitEvent} instance to be processed,
     *                  indicating that the user has quit.
     */
    @Override
    public void visit(QuitEvent quitEvent) {
        this.login = false;
        this.lobby = false;
        this.view.phaseChanged(new PhaseEvent(loginClient));
    }

    /**
     * Handles the visit for an {@code HourglassEvent} instance.
     * This method updates the client's view to reflect the state or changes indicated by the hourglass event.
     *
     * @param event the {@code HourglassEvent} instance to be processed,
     *              containing information about the hourglass state or update.
     */
    @Override
    public void visit(HourglassEvent event) {
        this.view.updateHourglass(event);
    }

    /**
     * Handles the visit for a {@code FinishGameEvent} instance.
     * This method updates the client's status by setting the login and lobby states to false.
     * It displays the game outcome using the client's view and pauses execution briefly
     * before notifying about a phase change.
     *
     * @param event the {@code FinishGameEvent} instance to be processed,
     *              containing details about the game's conclusion and outcome.
     */
    @Override
    public void visit(FinishGameEvent event) {
        this.login = false;
        this.lobby = false;
        this.view.showOutcome(event);
        try{
            Thread.sleep(10000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        this.view.phaseChanged(new PhaseEvent(loginClient));
    }

    /**
     * Handles a {@code ReconnectedEvent} to manage client reconnection logic.
     * This method updates the client state based on the token and player information
     * provided by the event, initializes appropriate components for game interaction,
     * and invokes view updates to reflect the reconnection.
     *
     * @param event the {@code ReconnectedEvent} containing details of the reconnection,
     *              including the token, player ID, game ID, and other relevant data.
     */
    @Override
    public void visit(ReconnectedEvent event) {
        this.token = event.getToken();
        if (event.getToken().equals("lobby")){
            System.out.println("Reconnected to lobby");
            this.view.reconnect(null);
        }
        else {
            this.lobby = true;
            this.login = true;
            commandInterpreter = new CommandInterpreter(event.getPlayerId(), event.getGameId());
            commandInterpreter.setlv(event.getLv());
            commandInterpreter.setToken(token);
            this.view.setGameboard(event.getLv());
            this.view.reconnect(event);
            if (rmiClient != null){
                rmiClient.setCommandInterpreter(commandInterpreter);
            }
            if (tcpClient != null){
                tcpClient.setCommandInterpreter(commandInterpreter);
            }
        }

    }


    /**
     * Handles the visitation of a TokenEvent instance and delegates its processing to the view.
     *
     * @param tokenEvent the TokenEvent instance to be processed
     */
    @Override
    public void visit(TokenEvent tokenEvent) {
        this.view.Token(tokenEvent);
    }


    /**
     * Handles the given ScoreboardEvent by showing the score using the view and
     * performing phase transition operations. Pauses the thread temporarily
     * before notifying the phase change.
     *
     * @param event the ScoreboardEvent containing the information to be handled
     */
    @Override
    public void visit(ScoreboardEvent event) {
        this.login = false;
        this.lobby = false;
        this.view.showScore(event);
        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException e){
            System.out.println("Error receiving scoreboard event: " + e.getMessage());
        }
        this.view.phaseChanged(new PhaseEvent(loginClient));
    }


    /**
     * Processes a given DeckEvent by displaying the associated deck.
     *
     * @param event the DeckEvent to process
     */
    @Override
    public void visit(DeckEvent event) {
        this.view.showDeck(event);
    }


    /**
     * Processes the given CardEvent and updates the view to show the associated card.
     *
     * @param event the CardEvent to be handled, representing the event that triggers
     *              the display of a specific card in the view
     */
    @Override
    public void visit(CardEvent event) {
        this.view.showCard(event);
    }


    /**
     * Handles the visiting logic for a GameLobbyEvent.
     *
     * @param event the GameLobbyEvent object containing details about the game lobby
     */
    @Override
    public void visit(GameLobbyEvent event){
        this.view.showLobbyGame(event);
    }


    /**
     * Handles the visit action for the provided HandEvent.
     *
     * @param event the HandEvent object to be processed, containing the details of the hand action
     */
    @Override
    public void visit(HandEvent event) {
        this.view.updateHand(event);
    }


    /**
     * Processes the provided VoidEvent.
     *
     * @param event the VoidEvent instance to be processed
     */
    @Override
    public void visit(VoidEvent event) {
        System.out.println("non so a cosa serve, vediamo se arriva un void");
    }


    /**
     * Handles the visit action for a given TileEvent by updating the board view.
     *
     * @param event the TileEvent instance containing the details needed to update the board
     */
    @Override
    public void visit(TileEvent event) {
        this.view.updateBoard(event);
    }


    /**
     * Handles the visit operation for an UncoverdTileSetEvent.
     *
     * @param event the UncoverdTileSetEvent to be processed
     */
    @Override
    public void visit(UncoverdTileSetEvent event) {
        this.view.updateUncoveredTilesSet(event);
    }


    /**
     * Handles the CoveredTileSetEvent by updating the covered tiles in the view.
     *
     * @param event the CoveredTileSetEvent containing the details of the updated covered tiles
     */
    @Override
    public void visit(CoveredTileSetEvent event) {
        this.view.updateCoveredTilesSet(event);
    }


    /**
     * Handles the visit operation for a GameBoardEvent. This method is used to
     * update the game board view based on the details of the provided event.
     *
     * @param gameBoardEvent the event containing the game board update details
     */
    @Override
    public void visit(GameBoardEvent gameBoardEvent) {
        this.view.updateGameboard(gameBoardEvent);
    }

}
