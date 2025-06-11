package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.ClientServer.NetworkUtils;
import org.example.galaxy_trucker.Commands.LobbyCommand;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.ClientServer.Client;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Commands.Command;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import static java.lang.System.exit;

public class RMIClient extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface server;
    private Player me;
    private Game myGame;
    private CommandInterpreter commandInterpreter;
    private Client client;

    Boolean running = false;
    private Thread inputLoop = null;
    boolean lobby = false;




    public RMIClient(Client client) throws RemoteException{
        //System.setProperty("java.rmi.server.hostname", "192.168.1.145");
        me =  new Player();
        myGame = null;
        this.client = client;

    }

    public RMIClient(Client client, CommandInterpreter commandInterpreter) throws IOException, NotBoundException, InterruptedException {
        this.client = client;
        this.commandInterpreter = commandInterpreter;
        reconnect();

    }

    private void reconnect() throws IOException, InterruptedException {
        if (!setup()){
            handleDisconnection();
            return;
        }
        Command command = commandInterpreter.interpret("Reconnect");
        if (commandInterpreter.getClient() == null){
            System.out.println("Reconnecting failed client is null");
            handleDisconnection();
            return;
        }
        server.command(command);
        System.out.println("Server started");
        //monitorPings();
        running = true;

        sendPongs();
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

    private boolean setup(){
        try{
            //System.out.println("Starting Client");
            Registry registry;
            //System.out.println(Settings.SERVER_NAME + " " + Settings.RMI_PORT);

            registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.RMI_PORT);

            this.server = (ServerInterface) registry.lookup("CommandReader");

            //System.out.println(server);
            running = true;
            return true;
        }

        catch(RemoteException | NotBoundException e){
            return false;
        }
    }




    @Override
    public void StartClient() throws IOException, NotBoundException {
        //System.out.println("Starting Client");

        if (!setup()){
            exit(1);
        }



        //monitorPings();


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

    @Override
    public void receivePing() throws RemoteException {

        //lastPingTime = System.currentTimeMillis();

    }

    @Override
    public void receiveToken(String token) throws RemoteException {

        this.commandInterpreter.setToken(token);
        this.client.getView().setGameboard(commandInterpreter.getLv());
        System.out.println(token);
        sendPongs();

    }


    public void inputLoop(boolean fromConsole) throws IOException, InterruptedException {
        String cmd = "";

        if (running) {
            while (running && !cmd.equals("end")) {
                try {
                    cmd = client.getView().askInput("Command <RMI>: ");
                    if (cmd.equals("")){

                    }
                    else if (cmd.equals("ChangeConnection")) {
                        System.out.println("No need to change connection!");
                    }
                    else if (cmd.equals("Reconnect")) {
                        System.out.println("No need to reconnect!");
                    }
                    else if(cmd.equals("Log")){
                        client.getView().seeLog();
                    }
                    else if (cmd.equals("Lobby")){
                        if (!client.getLobby()){
                            client.setLobby(true);
                            LobbyCommand Lobby = new LobbyCommand("Lobby");
                            Lobby.setClient(this);
                            server.command(Lobby);
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
                            client.setLogin(true);
                            String playerId = client.getView().askInput("Insert player ID: ");
                            String gameId = client.getView().askInput("Insert game ID: ");
                            int level = Integer.parseInt(client.getView().askInput("Insert game level: "));
                            int maxPlayers = Integer.parseInt(client.getView().askInput("Insert number of players [1-4]: "));
                            if (maxPlayers < 1){
                                maxPlayers = 1;
                            }
                            if (maxPlayers > 4){
                                maxPlayers = 4;
                            }

                            String fullCommand = "Login " + playerId + " " + gameId + " " + level;
                            System.out.println(fullCommand);

                            commandInterpreter = new CommandInterpreter(playerId, gameId);
                            commandInterpreter.setlv(level);

                            commandInterpreter.setClient(this);
                            LoginCommand loginCommand = new LoginCommand(gameId,playerId,level,"Login", maxPlayers);
                            loginCommand.setClient(this);

                            System.out.println(loginCommand);
                            server.command(loginCommand);
                            System.out.println("Sent login command");

                        }
                        else{
                            System.out.println("You are already logged in! [quit?]");
                        }

                    }
                    else if (cmd.equals("Join")) {
                        if (!client.getLogin()){

                            String playerId = client.getView().askInput("Insert player ID: ");
                            String gameId = client.getView().askInput("Insert game ID: ");
                            if (client.containsGameId(gameId)) {
//                                System.out.println("Invalid Game!");
//                                break;
                                client.setLogin(true);
                                int level = client.getLevel(gameId);

                                //String fullCommand = "Login " + playerId + " " + gameId + " " + level;
                                //System.out.println(fullCommand);

                                commandInterpreter = new CommandInterpreter(playerId, gameId);
                                commandInterpreter.setlv(level);

                                commandInterpreter.setClient(this);
                                LoginCommand loginCommand = new LoginCommand(gameId, playerId, level, "Login", -1);
                                loginCommand.setClient(this);

                                System.out.println(loginCommand);
                                server.command(loginCommand);
                                System.out.println("Sent login command");
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


    public void changeConnection() throws NotBoundException, IOException, InterruptedException {
        try {
            //UnicastRemoteObject.unexportObject(this, true);
            System.out.println("RMI connection closed.");
        } catch (Exception e) {
            System.out.println("Error RMI: " + e.getMessage());
        }
        this.server = null;

        client.getView().disconnect();
        client.changeConnection("TCP", commandInterpreter);

    }


    public void sendPongs(){
        new Thread(()->{
            System.out.println("PONG");
            while(running){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    new Thread(() -> {
                        try {
                            server.receivePong();
                        } catch (RemoteException e) {
                            synchronized (running) {
                                if (running) {
                                    running = false;
                                    try {
                                        handleDisconnection();
                                    } catch (InterruptedException | IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                } else {
                                    System.out.println("Error receiving pongs");
                                }
                            }
                        }
                    }).start();

                } finally {

                }


            }
        }).start();

    }

    private void handleDisconnection() throws InterruptedException, IOException {
        running = false;
        client.getView().disconnect();
        if (inputLoop != null && inputLoop.isAlive()) {
            inputLoop.interrupt();
            //inputLoop.join();
        }

        //client.getView().connect();
        try{
            while(true) {
                String whatNow = client.getView().askInput("<Reconnect> | <Exit> | <ChangeConnection> :");
                switch (whatNow) {
                    case "Exit": {
                        exit(1);
                        return;
                    }
                    case "ChangeConnection": {
                        changeConnection();
                        return;
                    }
                    case "Reconnect": {
                        if (!setup()) {
                            System.out.println("Error reconnecting!");
                            break;
                        }
                        Command command = commandInterpreter.interpret("Reconnect");
                        server.command(command);
                        running = true;
                        inputLoop = new Thread(() -> {
                            try {
                                this.inputLoop(true);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        inputLoop.setDaemon(true);
                        //monitorPings();
                        sendPongs();
                        inputLoop.start();
                        return;
                    }
                    default: {
                        System.out.println("Unknown command: " + whatNow);
                        break;
                    }
                }
            }
        } catch (NotBoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
            handleDisconnection();

        }

    }



//TODO: usare metodo input reader per stamapre i messaggi di errore e usare [...] per capire se System o Server
    //perchè tanto potrei salvarmi l'ultimo StringBuilder usato per il render
}
