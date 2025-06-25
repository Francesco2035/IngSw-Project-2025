package org.example.galaxy_trucker.ClientServer.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.TokenEvent;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.NotBoundException;

//TODO: non possiamo far terminare l'input con end
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

    public TCPClient(Client c) {
        this.client = c;
    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

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



    private void startThread(){
        eventThread = new Thread(this::EventListener);
        pingThread = new Thread(this::PingLoop);
        eventThread.start();
        pingThread.start();
    }


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
                else if (userInput.equals("Bg")){
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



    public void startClient() throws IOException {

        if (!setup()){
            disconnect();
        }
        startThread();
        System.out.println("Connection started\n");
        clientLoop();
        }

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
                    if (client.getLogin()){
                        String reconnect = mapper.writeValueAsString(commandInterpreter.interpret("Reconnect"));
                        out.println(reconnect);
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


