package org.example.galaxy_trucker.ClientServer.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LobbyCommand;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.util.UUID;

//TODO: non possiamo far terminare l'input con end
public class TCPClient{
//TODO: settare lobby e login anche da fuori nel caso il client dovesse cambiare connessione
    private boolean lobby = false;

    private boolean connected = false;
    private Socket echoSocket;
    private PrintWriter out = null;
    private BufferedReader in;
    private BufferedReader stdIn = null;
    private long lastPongTime = 0;
    private Client client = null;
    private CommandInterpreter commandInterpreter = null;
    private UUID token = null;
    private Thread eventThread = null;
    private Thread pingThread = null;
    //private Thread clientLoop = null;
    //TODO: modificare input come in RMI

    public TCPClient(Client c) {
        this.client = c;
    }

    public TCPClient(Client c, CommandInterpreter commandInterpreter) throws IOException {
        this.client = c;
        this.commandInterpreter = commandInterpreter;
        if (!setup()){
            disconnect();
        }
        System.out.println("Connection TCP started\n");
        startThread();
        client.getView().connect();
        sendReconnect();
        clientLoop();

    }

    private void sendReconnect() throws JsonProcessingException {
        Command command = commandInterpreter.interpret("Reconnect");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(command);
        out.println(json);
        System.out.println("CommandSent: " + json);
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

                String tokenStr = msg.substring(7);
                UUID token = UUID.fromString(tokenStr);
                System.out.println("Token received: " + token);
                this.token = token;
                this.client.getView().setGameboard(commandInterpreter.getLv());
                commandInterpreter.setToken(tokenStr);
            }
            else {
                   System.out.println("Received msg: " + msg);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Event event = objectMapper.readValue(msg, Event.class);
                    client.receiveEvent(event);
            }
            }
        } catch (SocketException e) {
            System.out.println("Socket closed, stopping EventListener: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("End of stream reached: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in EventListener: " + e.getMessage());
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
                    String whatNow = client.getView().askInput("<Reconnect> | <ChangeConnection> | <Exit> :");
                    switch (whatNow) {
                        case "Reconnect":
                            reconnect();
                            return;
                        case "ChangeConnection":
                            changeConnection();
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
        String userInput;
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
                else if (userInput.equals("Lobby")) {
                    if (!lobby) {
                        lobby = true;
                        LobbyCommand lobbyCommand = new LobbyCommand("Lobby");

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
                        client.setLogin(true);

                        String playerId = client.getView().askInput("PlayerID: ");

                        String gameId = client.getView().askInput("GameID: ");

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
                        jsonLogin = mapper.writeValueAsString(loginCommand);

                        out.println(jsonLogin);
                    }
                }
                else if (userInput.equals("Join")) {

                    if(!client.getLogin()) {

                        String playerId = client.getView().askInput("PlayerID: ");
                        String gameId = client.getView().askInput("GameID: ");
                        if (client.containsGameId(gameId)) {
                            client.setLogin(true);

                            int gameLevel = Integer.parseInt(client.getView().askInput("Game level: "));

                            LoginCommand loginCommand = new LoginCommand(gameId, playerId, gameLevel, "Login", -1);

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
                    System.out.println("CommandSent: " + json);
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

                    String reconnect = mapper.writeValueAsString(commandInterpreter.interpret("Reconnect"));
                    out.println(reconnect);

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

    public void changeConnection() throws IOException {
        try {
            connected = false;

            if (pingThread != null && pingThread.isAlive()) pingThread.interrupt();
            if (eventThread != null && eventThread.isAlive()) eventThread.interrupt();

            if (echoSocket != null && !echoSocket.isClosed()) {
                echoSocket.close();
                System.out.println("Closing echo socket...");
                //Thread.sleep(5000);
            }

            //if (clientLoop != null && clientLoop.isAlive()) clientLoop.interrupt();


            System.out.println("Switching to RMI...");
            client.changeConnection("RMI", commandInterpreter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}


