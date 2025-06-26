package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.ClientServer.Messages.Event;
import org.example.galaxy_trucker.ClientServer.Messages.TokenEvent;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

/**
 * Represents a Remote Method Invocation (RMI) client that interacts with a remote server and
 * interfaces with a game client. This class handles establishing connections, managing ping
 * monitoring with the server, receiving commands and events, and enabling interactive
 * client-to-server communication for an RMI-based application.
 *
 * This class extends the {@code UnicastRemoteObject} to allow communication over RMI and
 * implements the {@code ClientInterface} for client-side functionalities.
 */
public class RMIClient extends UnicastRemoteObject implements ClientInterface {

    /**
     * Represents the remote server interface used by the RMIClient for communication.
     *
     * This variable holds a reference to the server-side implementation of the
     * ServerInterface, enabling the client to invoke remote methods for sending
     * commands, monitoring connection status, and interacting with server-side
     * functionalities. It serves as the primary link between the client and the
     * server in an RMI-based architecture.
     *
     * The ServerInterface instance referenced by this variable must be properly
     * initialized during the client's setup process to ensure seamless interaction
     * with the server. If the server interface is not bound or unavailable, the
     * client will fail to execute remote operations.
     *
     * This variable is private to encapsulate the server reference and prevent
     * unauthorized access or manipulation of the remote interface from outside
     * the RMIClient class.
     */
    private ServerInterface server;
    /**
     * Represents the local player instance of the client within the RMIClient context.
     *
     * This field is used to store and manage the state and actions
     * of the player associated with this client session.
     * It provides access to player-specific data and methods needed
     * to interact with the game environment and server.
     */
    private Player me;
    /**
     * Represents the current game instance for the RMIClient.
     *
     * This variable holds a reference to a {@code Game} object, which encapsulates the state
     * and logic of the game session that the client is participating in. The {@code myGame}
     * variable is used to interact with various aspects of the game, including players,
     * game board, card decks, and tiles.
     *
     * The {@code Game} object includes functionalities such as adding or removing players,
     * retrieving game-specific components, and managing the overall state of the game.
     *
     * This field is used within the RMIClient to perform operations related to the game session,
     * such as updating the game state, interacting with the game's board, or processing
     * game-related events. It is typically initialized when a game is created or joined
     * through user interaction or server responses.
     *
     * Access to this variable may be synchronized depending on the operations being performed
     * to ensure thread safety during concurrent access.
     */
    private Game myGame;
    /**
     * Represents the command interpreter for the RMIClient.
     *
     * The `commandInterpreter` field is responsible for handling and processing commands
     * issued by the client or received from the server. It acts as the interface for interpreting
     * various user inputs or system commands and coordinating the appropriate logic for execution.
     *
     * This field is initialized externally and must be explicitly set using the
     * {@link #setCommandInterpreter(CommandInterpreter)} method before it can be used.
     * It plays a critical role in enabling the functionality of the client for interpreting actions,
     * managing tokens, and interacting with the game environment.
     */
    private CommandInterpreter commandInterpreter;
    /**
     * Represents the client instance utilized by the RMIClient.
     *
     * This variable serves as the core component to facilitate communication
     * between the RMIClient and the server or other remote objects. It is
     * expected to implement the required client-side functionality, enabling
     * the RMIClient to process incoming data, send requests, and handle events.
     *
     * The `client` variable is a private field within the RMIClient and plays
     * a key role in delegating tasks such as event reception, token processing,
     * and interaction with the server interface. It must adhere to the appropriate
     * contracts and interfaces to ensure proper interaction within the RMI framework.
     */
    private Client client;
    /**
     * Tracks the timestamp of the last received ping from the server.
     *
     * This variable is updated each time a ping is successfully received,
     * and its value represents the timestamp in milliseconds since the epoch
     * (as returned by {@code System.currentTimeMillis()}).
     *
     * It is used to monitor the connection status between the client and server.
     * If the time elapsed since the last ping exceeds a predefined threshold, the
     * connection is considered lost, and appropriate reconnection or disconnection
     * handling routines are triggered.
     *
     * The variable is declared as {@code volatile} to ensure visibility across
     * multiple threads, as it is accessed and modified in a concurrent environment.
     */
    private volatile long lastPingTime = System.currentTimeMillis();


    /**
     * Represents the running state of the RMIClient.
     *
     * This variable acts as a flag to indicate whether the client is currently operating or not.
     * It is used throughout various methods to control execution of processes such as ping monitoring,
     * input handling, and disconnection routines. When set to false, the client operations are halted.
     */
    Boolean running = false;
    /**
     * Represents a thread responsible for continuously handling user input or server communication.
     *
     * The input loop is intended to run in a separate thread and is primarily used to manage
     * the input mechanism asynchronously. It facilitates interaction between the user and
     * the client, delegating commands and actions to their respective handlers. This thread
     * serves an essential role in maintaining the responsiveness of the client while processing
     * input or communication independently.
     *
     * The thread lifecycle is managed explicitly within the client, ensuring it is started
     * and terminated as needed during the client operations.
     */
    private Thread inputLoop = null;

    /**
     * A single-threaded scheduled executor service responsible for managing scheduled tasks
     * within the RMIClient class.
     *
     * This scheduler is used to run periodic or delayed tasks in a separate thread, ensuring
     * that all scheduled operations are executed in a serialized and thread-safe manner.
     *
     * Key Use Cases:
     * - Supporting background tasks such as connection monitoring and handling pings.
     * - Coordinating periodic events required by the client, such as automated tasks
     *   that do not interfere with the main application workflow.
     *
     * Thread Management:
     * The scheduler operates independently of other threads in the application
     * and maintains a single thread for executing submitted tasks in sequence.
     *
     * Lifecycle:
     * The lifecycle of the scheduler is managed by the RMIClient class. It is started
     * when tasks are scheduled and can be explicitly terminated during disconnection
     * or application shutdown to release resources properly.
     */
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    /**
     * Handles the reception of a ping message from the server.
     *
     * Updates the last known ping time to the current system time
     * and notifies the server by sending a pong response.
     *
     * @throws RemoteException if a remote communication error occurs while sending the pong response.
     */
    @Override
    public void receivePing() throws RemoteException {

        lastPingTime = System.currentTimeMillis();
        server.receivePong(this);

    }


    /**
     * Starts the ping monitor that periodically checks the time of the last received ping
     * and detects connection loss if no ping is received within a defined threshold.
     *
     * If a scheduler is already running, it is terminated before starting a new one.
     * The monitoring process is executed at fixed intervals using a single-threaded scheduled executor.
     *
     * If the time since the last ping exceeds 10 seconds, the connection is considered lost,
     * and a disconnection handling routine is triggered. This might include cleaning up resources
     * and attempting reconnection.
     *
     * The ping monitor operates autonomously in a separate thread provided by the executor service.
     */
    public void startPingMonitor() {
        System.out.println("Start monitor pings");


        // Se esiste un scheduler attivo, spegnilo prima
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            //System.out.println(lastPingTime);
            if (now - lastPingTime > 10000) {
                System.out.println("Connection lost");
                try {
                    handleDisconnection();
                } catch (InterruptedException | IOException e) {
                    System.out.println("Error while disconnecting: "+e.getMessage());
                }

            }
        }, 0, 2, TimeUnit.SECONDS);
    }


    /**
     * Sets the command interpreter for the RMIClient.
     *
     * @param commandInterpreter the CommandInterpreter instance to be set for handling commands
     */
    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }


    /**
     * Constructs a new RMIClient instance.
     *
     * @param client the client instance that this RMIClient will use for communication.
     * @throws RemoteException if a remote communication error occurs.
     */
    public RMIClient(Client client) throws RemoteException{
        //System.setProperty("java.rmi.server.hostname", "192.168.1.145");
        me =  new Player();
        myGame = null;
        this.client = client;

    }


    /**
     * Initializes the RMI client by connecting to the RMI registry and retrieving the server interface.
     *
     * This method attempts to locate a remote server object using the provided server name and
     * RMI port from the Settings class. If successful, it assigns the server object to the local
     * field and sets the client to a "running" state. If there are connection issues, an appropriate
     * exception message is printed, and the method will return false.
     *
     * @return true if the setup process completes successfully and the client is running;
     *         false if an exception occurs during connection to the server or registry.
     */
    private boolean setup(){
        try{
            System.out.println("Starting Client");
            Registry registry;
            System.out.println(Settings.SERVER_NAME + " " + Settings.RMI_PORT);

            registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.RMI_PORT);

            this.server = (ServerInterface) registry.lookup("CommandReader");

            System.out.println(server);
            running = true;
            System.out.println("running "+ running);
            return true;
        }

        catch(RemoteException | NotBoundException e){
            System.out.println("error reconnecting "+ e.getMessage());
            return false;
        }
    }


    /**
     * Starts the client by initializing necessary components and entering an input loop.
     *
     * This method performs the following steps:
     * 1. Calls the `setup` method to establish initial connections and configurations.
     *    If the setup fails, the application exits with a status code of 1.
     * 2. Initializes and starts a daemon thread to handle user input and server communication.
     *    The input handling loop is executed in a separate thread to allow asynchronous
     *    processing of user commands and server responses.
     *
     * @throws IOException if any I/O error occurs during setup or input processing.
     * @throws NotBoundException if the expected server interface is not bound
     *         in the RMI registry during the setup process.
     */
    @Override
    public void StartClient() throws IOException, NotBoundException {

        if (!setup()){
            exit(1);
        }

        inputLoop = new Thread(() -> {
            try {
                this.inputLoop(true);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        inputLoop.setDaemon(true);
        inputLoop.start();
    }


    /**
     * Receives and handles an incoming event.
     *
     * This method delegates the processing of the event to the client by invoking
     * the {@code receiveEvent} method on the client instance. If an exception occurs
     * during the event processing, it is caught and an error message is logged
     * indicating the issue.
     *
     * @param event the event to be received and processed. The event must implement
     *              the {@code Event} interface and comply with the Visitor design
     *              pattern.
     */
    @Override
    public void receiveMessage(Event event) {
        try{
            client.receiveEvent(event);
        }
        catch (Exception e){
//            e.printStackTrace();
            System.out.println("Error receiving event " + e.getMessage());
        }
    }


    /**
     * Receives a token and processes it by updating the command interpreter,
     * gameboard view, and triggering a token-related event.
     *
     * This method sets the token in the associated {@code CommandInterpreter},
     * updates the client's gameboard to reflect the current level provided
     * by the command interpreter, and generates a {@code TokenEvent} containing
     * the token for further handling.
     *
     * @param token the token string to be processed. It serves as a unique identifier
     *              or access token within the system.
     * @throws RemoteException if a remote communication error occurs during the method execution.
     */
    @Override
    public void receiveToken(String token) throws RemoteException {

        this.commandInterpreter.setToken(token);
        this.client.getView().setGameboard(commandInterpreter.getLv());
        this.client.receiveEvent(new TokenEvent(token));
        //System.out.println(token);
        //sendPongs();

    }


    /**
     * Handles the main input loop for user commands. Depending on the input, various actions
     * such as reconnecting, creating or joining a game, viewing logs, or interacting with boards
     * can be performed.
     *
     * @param fromConsole Specifies whether the input should be read from the console or handled differently.
     * @throws IOException If an input or output exception occurs during the loop's operations.
     * @throws InterruptedException If the thread running the input loop is interrupted.
     */
    public void inputLoop(boolean fromConsole) throws IOException, InterruptedException {
        String cmd = "";

        if (running) {
            while (running && !cmd.equals("end")) {
                try {
                    cmd = client.getView().askInput("Command <RMI>: ");
                    if (cmd.equals("")){

                    }

                    else if (cmd.equals("Reconnect") && client.getLogin()) {
                        System.out.println("No need to reconnect!");
                    }
                    else if (cmd.equals("Reconnect") && !client.getLogin()) {
                        String token = client.getView().askInput("Token: ");
                        ReconnectCommand command = new ReconnectCommand(token,"","",-1,"Reconnect");
                        command.setClient(this);
                        server.command(command);
                    }
                    else if(cmd.equals("Log")){
                        client.getView().seeLog();
                    }
                    else if (cmd.equals("BG")){
                        client.getView().background();
                        client.getView().refresh();
                    }
                    else if (cmd.equals("Lobby")){
                        if (!client.getLobby()){
                            client.setLobby(true);
                            LobbyCommand Lobby = new LobbyCommand("Lobby");
                            Lobby.setClient(this);
                            server.command(Lobby);
                            lastPingTime = System.currentTimeMillis();
                            startPingMonitor();
                        }
                        else{
                            if (client.getLogin()){
                                System.out.println("You are already logged in! [quit?]");
                            }
                            else{
                                System.out.println("No need to reconnect lobby!");
                            }
                        }

                    }
                    else if (cmd.equals("Create")){

                         if (!client.getLogin()){
                            boolean aborted = false;
                            String playerId = "";
                            while (playerId.equals("") || playerId.length() > 20){
                                playerId = client.getView().askInput("Insert player ID [max 20 characters || abort]: ");
                                if (playerId.equals("abort")){
                                    aborted = true;
                                    break;
                                }
                            }
                            if (aborted){
                                client.getView().refresh();
                                continue;
                            }

                            String gameId = "";
                            while (gameId.equals("") || gameId.length() > 20){
                                gameId = client.getView().askInput("Insert game ID [max 20 characters || abort]: ");
                                if (gameId.equals("abort")){
                                    aborted = true;
                                    break;
                                }
                            }
                             if (aborted){
                                 client.getView().refresh();
                                 continue;
                             }


                            int level = Integer.parseInt(client.getView().askInput("Insert game level: "));
                            int maxPlayers = Integer.parseInt(client.getView().askInput("Insert number of players [1-4]: "));
                            if (maxPlayers < 1){
                                maxPlayers = 1;
                            }
                            if (maxPlayers > 4){
                                maxPlayers = 4;
                            }

                            commandInterpreter = new CommandInterpreter(playerId, gameId);
                            commandInterpreter.setlv(level);

                            commandInterpreter.setClient(this);
                            client.setLogin(true);
                            LoginCommand loginCommand = new LoginCommand(gameId,playerId,level,"Login", maxPlayers);
                            loginCommand.setClient(this);

                            server.command(loginCommand);
                            if (scheduler != null && scheduler.isShutdown()){
                                lastPingTime = System.currentTimeMillis();
                                startPingMonitor();
                            }

                        }
                        else{
                            System.out.println("You are already logged in! [quit?]");
                        }

                    }
                    else if (cmd.equals("Join")) {
                        if (!client.getLogin()){
                            boolean aborted = false;
                            String playerId = "";
                            while (playerId.equals("") || playerId.length() > 20){
                                playerId = client.getView().askInput("Insert player ID [max 20 characters || abort]: ");
                                if (playerId.equals("abort")){
                                    aborted = true;
                                    break;
                                }
                            }
                            if (aborted){
                                client.getView().refresh();
                                continue;
                            }

                            String gameId = "";
                            while (gameId.equals("") || gameId.length() > 20){
                                gameId = client.getView().askInput("Insert game ID [max 20 characters || abort]: ");
                                if (gameId.equals("abort")){
                                    aborted = true;
                                    break;
                                }
                            }
                            if (aborted){
                                client.getView().refresh();
                                continue;
                            }
                            if (client.containsGameId(gameId)) {

                                client.setLogin(true);
                                int level = client.getLevel(gameId);

                                commandInterpreter = new CommandInterpreter(playerId, gameId);
                                commandInterpreter.setlv(level);

                                commandInterpreter.setClient(this);
                                LoginCommand loginCommand = new LoginCommand(gameId, playerId, level, "Login", -1);
                                loginCommand.setClient(this);

                                System.out.println(loginCommand);
                                server.command(loginCommand);
                                lastPingTime = System.currentTimeMillis();
                                startPingMonitor();
                            }
                            else {
                                System.out.println("Invalid game: "+gameId+"\n games:");
                                for (String id : client.getGameidToLV().keySet()) {
                                    System.out.println(id);
                                }
                            }

                        }
                        else{
                            System.out.println("You are already logged in! [quit?]");
                        }

                    }
                    else if (cmd.equals("SeeBoards")){
                        if (client.getLogin()){
                            client.getView().seeBoards();
                        }
                        else{
                            System.out.println("CHOOSE A GAME");
                        }
                    }
                    else if (cmd.equals("MainTerminal")){
                        client.getView().refresh();
                    }
                    else {
                        if (client.getLogin()){
                            Command command = commandInterpreter.interpret(cmd);
                            server.command(command);
                        }
                        else{
                            System.out.println("CHOOSE A GAME");
                        }

                    }

                }catch (RemoteException ex) {
                    synchronized (running){
                        if(running){
                            System.out.println("Exception: ");
                            System.out.println(ex.getMessage());
                            running = false;
                            handleDisconnection();
                        }

                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    cmd = "";
                }
            }
        }
        else {
            handleDisconnection();
        }
        System.out.println("Fine input.");
    }


    /**
     * Handles the disconnection process for the client and provides options for reconnection or exiting.
     *
     * This method is invoked when the client loses its connection to the server or needs to explicitly
     * disconnect. It performs the following tasks:
     *
     * 1. Stops the current client operations by setting the `running` flag to false and interrupting
     *    the input loop thread if it is active.
     * 2. Shuts down the scheduler service if it is not already terminated.
     * 3. Disconnects the client's view from the current session.
     * 4. Prompts the user with options to either reconnect or exit:
     *    - "Exit": Terminates the application.
     *    - "Reconnect": Attempts to re-establish the connection by invoking the `setup` method. If the
     *       setup is successful, it reinitializes the client state, starts necessary threads, and
     *       resumes normal operation.
     *    - Any other input is treated as an unknown command and prompts the user again for valid input.
     * 5. Handles any errors or exceptions encountered during the reconnection process, including failed
     *    server communication or setup attempts.
     *
     * This method ensures the client properly cleans up resources and provides a seamless process
     * for reconnection or closure.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting or performing
     *                              reconnection-related operations.
     * @throws IOException if an I/O error occurs during client operations, such as input handling
     *                     or reconnection processes.
     */
    private void handleDisconnection() throws InterruptedException, IOException {
        System.out.println("handle disconnection");
        running = false;
        if (inputLoop != null && inputLoop.isAlive()) {
            inputLoop.interrupt();
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        client.getView().disconnect();
        String whatNow = "";
        while(whatNow.isEmpty()|| !running) {
            whatNow = client.getView().askInput("<Reconnect> | <Exit>: ");
            switch (whatNow) {
                case "Exit": {
                    System.out.println("Exit");
                    exit(1);
                    return;
                }
                case "Reconnect": {
                    if (!setup()) {
                        System.out.println("Error reconnecting!");
                        break;
                    }
                    String command = "";
                    Command cmd = null;

                    if(client.getLobby() && !client.getLogin()){
                        LobbyCommand lobby = new LobbyCommand("Lobby");
                        lobby.setClient(this);
                        try {
                            server.command(lobby);
                            //System.out.println(lobby.getClass().getSimpleName());
                        } catch (Exception e) {
                            System.out.println("catch "+e.getMessage());
                            //e.printStackTrace();
                            //throw new RuntimeException(e);
                            break;
                        }
                    }
                    else if (client.getLogin()){
                        command = "Reconnect";
                    }
                    if (!command.isEmpty()){
                        try{
                             cmd = commandInterpreter.interpret(command);
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            server.command(cmd);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    running = true;
                    inputLoop = new Thread(() -> {
                        try {
                            this.inputLoop(true);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    inputLoop.setDaemon(true);
                    inputLoop.start();
                    //sendPongs();
                    if (client.getLogin() || client.getLobby()){
                        lastPingTime = System.currentTimeMillis();
                        startPingMonitor();
                    }
                    return;
                }
                default: {
                    System.out.println("Unknown command: " + whatNow);
                    break;
                }
            }
        }

    }

}
