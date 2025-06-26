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

public class RMIClient extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface server;
    private Player me;
    private Game myGame;
    private CommandInterpreter commandInterpreter;
    private Client client;
    private volatile long lastPingTime = System.currentTimeMillis();


    Boolean running = false;
    private Thread inputLoop = null;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void receivePing() throws RemoteException {

        lastPingTime = System.currentTimeMillis();
        server.receivePong(this);

    }


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




    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }


    public RMIClient(Client client) throws RemoteException{
        //System.setProperty("java.rmi.server.hostname", "192.168.1.145");
        me =  new Player();
        myGame = null;
        this.client = client;

    }


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
    public void receiveToken(String token) throws RemoteException {

        this.commandInterpreter.setToken(token);
        this.client.getView().setGameboard(commandInterpreter.getLv());
        this.client.receiveEvent(new TokenEvent(token));
        //System.out.println(token);
        //sendPongs();

    }

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



    public void sendPongs(){
        new Thread(()->{
            //System.out.println("PONG");
            while(running){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    new Thread(() -> {
                        try {
                            server.receivePong(this);
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
