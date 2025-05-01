package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.UUID;


public class TCPClient{

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

    public TCPClient(Client c) {
        this.client = c;
    }

    public TCPClient(Client c, CommandInterpreter commandInterpreter) throws JsonProcessingException {
        this.client = c;
        this.commandInterpreter = commandInterpreter;
        try {
            echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT); //SOCKET CLIENT
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }



        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }


        PrintWriter finalOut = out;

        eventThread = new Thread(this::EventListener);
        pingThread = new Thread(this::PingLoop);
        //clientLoop = new Thread(this::clientLoop);
        System.out.println("Connected to " + Settings.SERVER_NAME + ":" + Settings.TCP_PORT + ", starting Threads");
        eventThread.start();
        pingThread.start();
        connected = true;


        Gson gson = new Gson();

        client.getView().connect();
        client.getView().askInput("Connection TCP started\n");
        Command command = commandInterpreter.interpret("Reconnect");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(command);
        out.println(json);
        System.out.println("CommandSent: " + json);
        clientLoop();

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
                commandInterpreter.setToken(tokenStr);
            }
            else {
                   // System.out.println("Received msg: " + msg);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Event event = objectMapper.readValue(msg, Event.class);
                    client.receiveEvent(event);
            }
            }
        } catch (IOException e) {
            System.out.println("IOException in PingListener " + e.getMessage());
            disconnect();
        }
    }


    private void PingLoop() {
        while (!echoSocket.isClosed()) {
            out.println("ping");
            //System.out.println("ping");
            try {
                Thread.sleep(5000);

                if (System.currentTimeMillis() - lastPongTime > 15000) {
                    System.out.println("Connection timed out. Disconnecting...");
                    disconnect();
                    break;
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }


    public void disconnect(){

        client.getView().disconnect();
        client.getView().connect();
        connected = false;
        pingThread.interrupt();
        eventThread.interrupt();
        //clientLoop.interrupt();
        try {
            echoSocket.close();
            client.getView().askInput("\nServer Disconnected.");
            switch (client.getView().askInput("<Reconnect> | <ChangeConnection> | <Exit>")){
                case "Reconnect":{reconnect();
                break;}
                case "ChangeConnection":{
                    changeConnection();
                    break;
                }
                case "Exit":{
                    System.exit(0);
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void clientLoop() {
        Gson gson = new Gson();
        String userInput;
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

                if (userInput.equals("end")) {
                    break;
                }

                if (userInput.equals("ChangeConnection")) {
                    changeConnection();
                    break;
                }

                Command command = commandInterpreter.interpret(userInput);
                String json = gson.toJson(command);
                out.println(json);
                System.out.println("CommandSent: " + json);

            } catch (IOException e) {
                System.out.println("IOException in clientLoop: " + e.getMessage());
                disconnect();
                break;
            } catch (Exception e) {
                System.out.println("Error interpreting or sending command: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }



    public void startClient() throws IOException {

        try {
            echoSocket = new Socket(Settings.SERVER_NAME, Settings.TCP_PORT); //SOCKET CLIENT
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }



        try {
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.err.println(e.toString() + " " + Settings.SERVER_NAME);
            System.exit(1);
        }


        PrintWriter finalOut = out;

        eventThread = new Thread(this::EventListener);
        pingThread = new Thread(this::PingLoop);
        //clientLoop = new Thread(this::clientLoop);
        eventThread.start();
        pingThread.start();
        connected = true;


        Gson gson = new Gson();

        System.out.println("Connection started\n");

        String playerId = client.getView().askInput("PlayerID: ");

        String gameId = client.getView().askInput("GameID: ");

        int gameLevel = Integer.parseInt( client.getView().askInput("Game level: "));

        LoginCommand loginCommand = new LoginCommand(gameId,playerId, gameLevel, "Login");

        commandInterpreter = new CommandInterpreter(playerId, gameId);
        commandInterpreter.setlv(gameLevel);

        String jsonLogin = gson.toJson(loginCommand);
        out.println(jsonLogin);
        //System.out.println("Comando di login inviato: " + jsonLogin);
//        clientLoop.start();
        clientLoop();

    }

    private void reconnect() {
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
                    connected = true;

                    Gson gson = new Gson();
                    String jsonLogin = gson.toJson(commandInterpreter.interpret("Reconnect"));
                    out.println(jsonLogin);

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
        } catch ( NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}


