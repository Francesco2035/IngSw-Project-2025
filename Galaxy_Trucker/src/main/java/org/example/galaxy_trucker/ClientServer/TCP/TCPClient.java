package org.example.galaxy_trucker.ClientServer.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.ReconnectedEvent;
import org.example.galaxy_trucker.Controller.Messages.TokenEvent;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.NotBoundException;

/**
 * The TCPClient class handles the communication between a client application and a TCP server.
 * It manages connection, data exchange, and threading for network interactions.
 * The class is designed to handle events, ping-pong logic to maintain connectivity,
 * and interpret commands issued by the client.
 */
public class TCPClient{

    private boolean connected = false;
    private Socket echoSocket;
    private PrintWriter out = null;
    private BufferedReader in;
    private BufferedReader stdIn = null;
    private long lastPongTime = 0;
    private Client client = null;
    private CommandInterpreter commandInterpreter = null;
    private String token = null;
    private Thread eventThread = null;
    private Thread pingThread = null;
    //private Thread clientLoop = null;

    /**
     * Creates an instance of TCPClient associated with the given Client object.
     *
     * @param c the Client object to associate with this TCPClient instance
     */
    public TCPClient(Client c) {
        this.client = c;
    }

    /**
     * Sets the command interpreter for the TCPClient instance.
     *
     * @param commandInterpreter the CommandInterpreter instance to be set,
     *                           responsible for handling command processing logic.
     */
    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    /**
     * Handles incoming messages from the server in a continuous loop, parsing and processing
     * them accordingly. This method implements the core event listening functionality
     * for the client-server communication.
     *
     * The method operates in the following steps:
     * 1. Reads lines of messages from the input stream (`in`) until the stream ends or an
     *    exception occurs.
     * 2. Identifies the type of the message and performs appropriate actions:
     *    - If the message is a "pong" response from the server, updates the `lastPongTime`
     *      to the current time.
     *    - If the message starts with a "Token: " prefix, extracts the token value, notifies
     *      the client with a `TokenEvent`, sets the token in the command interpreter, and
     *      updates the gameboard view with the level obtained from the command interpreter.
     *    - For any other messages, deserializes the message into an `Event` object using
     *      the ObjectMapper, then processes the event using the client's `receiveEvent` method.
     * 3. Catches and handles exceptions occurring during the execution, including:
     *    - `SocketException` when the socket connection is closed.
     *    - `EOFException` when the end of the input stream is reached.
     *    - `IOException` and all other general exceptions, logging them accordingly.
     *
     * This method is private and intended to be executed as part of a thread to enable
     * asynchronous communication with the server.
     *
     * Note:
     * - The method assumes the `in` field is initialized as a `BufferedReader` for reading
     *   messages.
     * - The method interacts with various client and command interpreter components to
     *   handle parsed messages and events.
     * - This is a blocking method that reads indefinitely from the input stream until its
     *   termination or an error disrupts its execution.
     */
    private void EventListener() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equals("pong")) {
                    lastPongTime = System.currentTimeMillis();

                    //System.out.println("Pong");
                }
                 else if (msg.startsWith("Token: ")) {

                    String token = msg.substring(7);
                    //System.out.println("Token received: " + token);
                    this.client.receiveEvent(new TokenEvent(token));
                    this.token = token;
                    this.client.getView().setGameboard(commandInterpreter.getLv());
                    commandInterpreter.setToken(token);
            }
            else {
                   //System.out.println("Received msg: " + msg);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Event event = objectMapper.readValue(msg, Event.class);
                    //System.out.println(">>>>>>>>>"+event.getClass());
                    client.receiveEvent(event);
            }
            }
        } catch (SocketException e) {
            System.out.println("Socket closed, stopping EventListener: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("End of stream reached: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in EventListener: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Starts two separate threads for the TCPClient.
     *
     * The first thread is responsible for listening to incoming events.
     * This is achieved by invoking the EventListener method in a thread-safe manner.
     *
     * The second thread is responsible for sending periodic "ping" messages
     * to maintain the connection with the server and validate the connection's health.
     * This is done by invoking the PingLoop method.
     *
     * Each thread operates independently and is crucial for the functionality
     * of the client-server communication in the TCPClient.
     */
    private void startThread(){
        eventThread = new Thread(this::EventListener);
        pingThread = new Thread(this::PingLoop);
        eventThread.start();
        pingThread.start();
    }


    /**
     * Establishes a connection to the server and initializes
     * input/output streams for communication.
     *
     * @return true if the connection and streams are successfully established,
     *         false otherwise.
     */
    private boolean setup(){
        try {
            echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT);
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            return false;
        }

        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            return false;
        }

        System.out.println("Connected to " + Settings.SERVER_NAME + ":" + Settings.TCP_PORT + ", starting Threads");
        connected = true;
        return true;

    }


    /**
     * Handles a continuous "ping" communication loop to ensure that the connection remains active.
     *
     * The method sends a "ping" message repeatedly at fixed intervals and monitors the last received
     * "pong" response. If the time elapsed since the last "pong" exceeds a defined timeout duration,
     * the connection is deemed inactive, and the method initiates a disconnection process.
     *
     * The loop runs until the socket is closed or the thread is interrupted.
     *
     * Exceptions:
     * - InterruptedException: Thrown if the thread running this method is interrupted during its sleep cycle.
     * - IOException: Thrown if there are any issues during the disconnection process.
     */
    private void PingLoop() {
        while (!echoSocket.isClosed()) {
            out.println("ping");
            //System.out.println("ping");
            try {
                Thread.sleep(3500);

                if (System.currentTimeMillis() - lastPongTime > 10000) {
                    System.out.println("Connection timed out. Disconnecting...");
                    disconnect();
                    break;
                }

            } catch (InterruptedException e) {
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Disconnects the client from the current session or server.
     * <ul>
     * This method performs the following tasks:
     * - Ensures that the connection status is reset.
     * - Attempts to close the active socket safely.
     * - Interrupts and terminates any active threads associated with event handling or pinging mechanisms.
     * - Disconnects and reconnects the client's view to refresh its state.
     * - Prompts the user to either reconnect to the server or exit the application.
     * </ul>
     *
     * This process is critical for the proper cleanup of resources, preventing resource leaks
     * and ensuring a reliable mechanism for handling disconnections and reconnections.
     *
     * @throws IOException If an I/O operation error occurs during the disconnection process or when trying to reconnect.
     */
    public void disconnect() throws IOException {
        if (connected){
            client.getView().disconnect();
            connected = false;
            try {
                if (echoSocket != null && !echoSocket.isClosed()) {
                    echoSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Socket close error: " + e.getMessage());
            }

            if (eventThread != null) {
                eventThread.interrupt();
                try {
                    eventThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (pingThread != null) {
                pingThread.interrupt();
                try {
                    pingThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }


            client.getView().connect();

            try {
                System.out.println("\nServer Disconnected.");

                while (true) {
                    String whatNow = client.getView().askInput("<Reconnect> | <Exit> :");
                    switch (whatNow) {
                        case "Reconnect":
                            reconnect();
                            return;
                        case "Exit":
                            System.exit(0);
                            return;
                        default:
                            System.out.println("Invalid input: " + whatNow);
                            break;
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Handles the main client loop for interaction and communication between the client
     * and the server. This method continuously listens and processes user input, sending
     * appropriate commands to the server and managing client-side features such as
     * login, game creation, joining lobbies, and refreshing the interface.
     *
     * The loop runs until the client disconnects or explicitly ends the interaction.
     * Various supported commands include:
     * - "SeeBoards": Displays the boards when the client is logged in.
     * - "Log": Shows the activity log.
     * - "Bg": Handles background updates and refreshes the client view.
     * - "MainTerminal": Triggers a view refresh.
     * - "Reconnect": Allows reconnection using a token if not already logged in.
     * - "Lobby": Connects to or verifies connection to a lobby.
     * - "Create": Enables the creation of a new game with specified parameters.
     * - "Join": Allows joining an existing game using a valid game ID.
     * - "end": Terminates the client loop.
     * - "ChangeConnection": Provides feedback regarding connection changes.
     * - Commands that are interpreted and sent to the server via a command interpreter.
     *
     * Error handling mechanisms ensure the robustness of user interactions, and
     * invalid inputs or improperly handled scenarios are managed gracefully.
     * The method terminates when disconnection occurs, an input is invalid, or the
     * "end" command is invoked.
     *
     * Implementation details include the use of an {@code ObjectMapper} for JSON serialization
     * and parsing, client-side state checks, and command interpretation.
     *
     * @throws IOException if communication-related exceptions occur during the interaction loop.
     */
    public void clientLoop() {
        String userInput = "";
        String jsonLogin;
        ObjectMapper mapper = new ObjectMapper();


        while (true) {
            if (!connected) {
                System.out.println("Client is disconnected. Exiting client loop.");
                break;
            }

            try {
                userInput = client.getView().askInput("Enter command <TCP>: ");
                if (userInput == null) {
                    System.out.println("Null input, closing connection...");
                    return;
                }
                else if (userInput.equals("SeeBoards")){
                    if (client.getLogin()){
                        client.getView().seeBoards();
                    }
                    else{
                        System.out.println("CHOOSE A GAME");
                    }
                }
                else if(userInput.equals("Log")){
                    client.getView().seeLog();
                }
                else if (userInput.equals("BG")){
                    client.getView().background();
                    client.getView().refresh();
                }
                else if (userInput.equals("MainTerminal")){
                    client.getView().refresh();
                }
                else if (userInput.equals("Reconnect") && client.getLogin()) {
                    System.out.println("No need to reconnect!");
                }
                else if (userInput.equals("Reconnect") && !client.getLogin()) {
                    String token = client.getView().askInput("Token: ");
                    ReconnectCommand command = new ReconnectCommand(token,"","",-1,"Reconnect");
                    mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(command);
                    out.println(json);
                }
                else if (userInput.equals("Lobby")) {
                    if (!this.client.getLobby()) {
                        this.client.setLobby(true);
                        LobbyCommand lobbyCommand = new LobbyCommand("Lobby");
                        //System.out.println("invio lobby");
                        try{
                            jsonLogin = mapper.writeValueAsString(lobbyCommand);
                            out.println(jsonLogin);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (client.getLogin()){
                        System.out.println("You are already logged in! [quit?]");
                    }
                    else{
                        System.out.println("Lobby is already connected");
                    }


                }
                else if (userInput.equals("Create")) {
                    if (!client.getLogin()) {
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

                        int gameLevel = Integer.parseInt(client.getView().askInput("Game level: "));

                        int maxPlayers = Integer.parseInt(client.getView().askInput("Insert number of players [1-4]: "));
                        if (maxPlayers < 1){
                            maxPlayers = 1;
                        }
                        if (maxPlayers > 4){
                            maxPlayers = 4;
                        }

                        LoginCommand loginCommand = new LoginCommand(gameId, playerId, gameLevel, "Login", maxPlayers);


                        commandInterpreter = new CommandInterpreter(playerId, gameId);
                        commandInterpreter.setlv(gameLevel);
                        mapper = new ObjectMapper();
                        client.setLogin(true);
                        jsonLogin = mapper.writeValueAsString(loginCommand);

                        out.println(jsonLogin);
                    }
                }

                else if (userInput.equals("Join")) {

                    if(!client.getLogin()) {

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

                            int gameLevel = Integer.parseInt(client.getView().askInput("Game level: "));

                            LoginCommand loginCommand = new LoginCommand(gameId, playerId, gameLevel, "Login", -1);
//                            commandInterpreter.setPlayerId(playerId);
//                            commandInterpreter.setGameId(gameId);
                            commandInterpreter = new CommandInterpreter(playerId, gameId);
                            commandInterpreter.setlv(gameLevel);
                            mapper = new ObjectMapper();
                            jsonLogin = mapper.writeValueAsString(loginCommand);

                            out.println(jsonLogin);
                        }
                        else{
                            System.out.println("Invalid GameId");
                        }


                    }
                    else{
                        System.out.println("You are already logged in! [quit?]");
                    }
                }

                else if (userInput.equals("end")) {
                    break;
                }

                else if (userInput.equals("ChangeConnection")) {
                    System.out.println("No need to changeConnection!");
                    break;
                }

                else if (userInput.equals("")){
                    break;
                }

                else{
                    Command command = commandInterpreter.interpret(userInput);
                    mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(command);

                    out.println(json);
                    //System.out.println("CommandSent: " + json);
                }



            } catch (Exception e) {
                System.out.println("Error interpreting or sending command: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Initializes and starts the client. Manages the connection setup, thread creation,
     * and main client communication loop.
     *
     * This method first attempts to establish and configure the connection using the
     * {@code setup()} method. If the setup fails, it initiates disconnection using
     * {@code disconnect()} to ensure proper cleanup.
     *
     * Once the connection is successfully established, it starts the relevant background
     * threads for event and ping handling using {@code startThread()}.
     *
     * The method prints a confirmation of the connection and begins the main client
     * interaction loop via the {@code clientLoop()} method.
     *
     * @throws IOException if an I/O error occurs during setup or disconnection.
     */
    public void startClient() throws IOException {

        if (!setup()){
            disconnect();
        }
        startThread();
        System.out.println("Connection started\n");
        clientLoop();
        }


    /**
     * Attempts to re-establish a connection to the server. This includes creating
     * a new socket connection, initializing input and output streams, and sending
     * a handshake request to verify connectivity.
     *
     * Behavior:
     * - Opens a socket to the specified server using the configuration in the
     *   {@link Settings} class.
     * - Initializes communication streams to interact with the server.
     * - Sends a "ping" message to the server and expects a "pong" response to
     *   confirm the connection is active.
     * - Based on the client state, sends appropriate commands:
     *   - If the client is in the lobby and not logged in, it sends a lobby
     *     command.
     *   - If the client is logged in, it sends a reconnect command.
     * - Starts threads for handling events and maintaining a connection through
     *   periodic pings.
     * - If reconnection fails at any step, the connection is terminated by
     *   invoking the {@code disconnect()} method.
     *
     * Note:
     * - The method handles exceptions during socket creation, stream initialization,
     *   and communication with the server. Upon encountering an error, it ensures
     *   proper cleanup by calling {@code disconnect()}.
     * - Uses Jackson's {@code ObjectMapper} to serialize commands to JSON format.
     *
     * Throws:
     * - IOException if an error occurs during the connection process or
     *   communication with the server.
     */
    private void reconnect() throws IOException {
        connected = true;
        try {
            try {
                echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT);
            } catch (IOException e) {
                System.err.println(e.toString() + " " + Settings.SERVER_NAME);
                disconnect();
            }
            try {
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                stdIn = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                System.err.println(e.toString() + " " + Settings.SERVER_NAME);
                disconnect();
            }
            if (echoSocket.isConnected()) {
                System.out.println("Reconnected to the server.");

                out.println("ping");
                String response = in.readLine();
                if (response != null && response.equals("pong")) {
                    System.out.println("Server responded to ping. Connection is active.");
                    //connected = true; qui

                    ObjectMapper mapper = new ObjectMapper();
                    if (client.getLobby() && !client.getLogin()) {
                        LobbyCommand lobbyCommand = new LobbyCommand("Lobby");
                        //System.out.println("invio lobby");
                        try{
                            String jsonLogin = mapper.writeValueAsString(lobbyCommand);
                            out.println(jsonLogin);
                            this.client.visit(new ReconnectedEvent("lobby", "placeholder", "placeholder", -1));
                        } catch (JsonProcessingException e) {
                            System.err.println(e.toString());
                        }

                    }


                    if (client.getLogin()){
                        String reconnect = mapper.writeValueAsString(commandInterpreter.interpret("Reconnect"));
                        out.println(reconnect);
                        this.client.visit(new ReconnectedEvent(token, commandInterpreter.getGame(), commandInterpreter.getPlayerId(), commandInterpreter.getLv()));
                    }
                    eventThread  = new Thread(this::EventListener);
                    pingThread = new Thread(this::PingLoop);
                    eventThread.start();
                    pingThread.start();
//                    clientLoop = new Thread(this::clientLoop);
//                    clientLoop.start();
                    clientLoop();
                } else {
                    System.out.println("No response to ping, reconnect failed.");
                    disconnect();
                }
            } else {
                System.out.println("Failed to reconnect, socket is not connected.");
                disconnect();
            }
        } catch (IOException e) {
            System.out.println("Failed to reconnect: " + e.getMessage());
            disconnect();
        }
    }
}


