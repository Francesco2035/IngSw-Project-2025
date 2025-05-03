package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Commands.ReconnectCommand;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CardEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.CoveredTileSetEvent;
import org.example.galaxy_trucker.Controller.Messages.TileSets.UncoverdTileSetEvent;
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
import java.util.ArrayList;

import static java.lang.System.exit;

public class RMIClient extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface server;
    private Player me;
    private Game myGame;
    private CommandInterpreter commandInterpreter;
    private Client client;
    Boolean running = false;
    private Thread inputLoop = null;



    public RMIClient(Client client) throws RemoteException{
        me =  new Player();
        myGame = null;
        this.client = client;

    }

    public RMIClient(Client client, CommandInterpreter commandInterpreter) throws RemoteException, NotBoundException, InterruptedException {
        this.client = client;
        this.commandInterpreter = commandInterpreter;
        reconnect();

    }

    private void reconnect() throws RemoteException, InterruptedException {
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
            System.out.println("Starting Client");
            Registry registry;
            registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.RMI_PORT);

            this.server = (ServerInterface) registry.lookup("CommandReader");

            System.out.println(server);
            return true;
        }

        catch(RemoteException | NotBoundException e){
            return false;
        }
    }




    @Override
    public void StartClient() throws IOException, NotBoundException {
        System.out.println("Starting Client");

        if (!setup()){
            exit(1);
        }


        String playerId = client.getView().askInput("Insert player ID: ");
        String gameId = client.getView().askInput("Insert game ID: ");
        int level = Integer.parseInt(client.getView().askInput("Insert game level: "));

        //String fullCommand = "Login " + playerId + " " + gameId + " " + level;

        commandInterpreter = new CommandInterpreter(playerId, gameId);
        commandInterpreter.setlv(level);
        commandInterpreter.setClient(this);
        LoginCommand loginCommand = new LoginCommand(gameId,playerId,level,"Login");
        loginCommand.setClient(this);

        System.out.println(loginCommand);
        server.command(loginCommand);
        System.out.println("Sent login command");
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



    @Override
    public void receiveMessage(Event event) {

        client.receiveEvent(event);
    }

    @Override
    public void receivePing() throws RemoteException {

        //lastPingTime = System.currentTimeMillis();

    }

    @Override
    public void receiveToken(String token) throws RemoteException {
        this.commandInterpreter.setToken(token);
        System.out.println(token);
    }


    public void inputLoop(boolean fromConsole) throws IOException, InterruptedException {
        String cmd = "";

        if (running) {
            while (running && !cmd.equals("end")) {
                try {
                    cmd = client.getView().askInput("Command <RMI>: ");
                    if (cmd.equals("ChangeConnection")) {
                        System.out.println("No need to change connection!");
                    }
                    if (cmd.equals("Reconnect")) {
                        System.out.println("No need to reconnect!");
                    }
                    else {
                        Command command = commandInterpreter.interpret(cmd);
                        server.command(command);
                    }

                }catch (RemoteException ex) {
                    synchronized (running){
                        if(running){
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
//            InputStream commandFile = getClass().getClassLoader().getResourceAsStream("commands_output.txt");
//            assert commandFile != null;
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(commandFile))) {
//                while ((cmd = br.readLine()) != null && !cmd.equals("end")) {
//                    try {
//                        Command command = commandInterpreter.interpret(cmd);
//                        server.command(command);
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
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
                                    } catch (InterruptedException ex) {
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

    private void handleDisconnection() throws InterruptedException {
        running = false;
        client.getView().disconnect();
        if (inputLoop != null && inputLoop.isAlive()) {
            inputLoop.interrupt();
            //inputLoop.join();
        }

        client.getView().connect();
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





}
