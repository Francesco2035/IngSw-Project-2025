package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Messages.Event;
import org.example.galaxy_trucker.Controller.Messages.HandEvent;
import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.TileEvent;
import org.example.galaxy_trucker.Controller.Messages.VoidEvent;
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
import java.util.Objects;

public class RMIClient extends UnicastRemoteObject implements ClientInterface {

    private ServerInterface server;
    private Player me;
    private Game myGame;
    private CommandInterpreter commandInterpreter;
    private Client client;

    public RMIClient(Client client) throws RemoteException{
        me =  new Player();
        myGame = null;
        this.client = client;

    }



    @Override
    public void StartClient() throws IOException, NotBoundException {

        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.RMI_PORT);

        this.server = (ServerInterface) registry.lookup("CommandReader");
//        this.server.login(this);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String playerId = client.getView().askInput("Insert player ID: ");
        String gameId = client.getView().askInput("Insert game ID: ");
        int level = Integer.parseInt(client.getView().askInput("Insert game level: "));

        //String fullCommand = "Login " + playerId + " " + gameId + " " + level;

        commandInterpreter = new CommandInterpreter(playerId, gameId);
        commandInterpreter.setlv(level);
        LoginCommand loginCommand = new LoginCommand(gameId,playerId,level,"Login");
        loginCommand.setClient(this);


        server.command(loginCommand);

        this.inputLoop(true);
    }


    @Override
    public void receiveMessage(HandEvent event) throws RemoteException {
        client.updateHand(event);
    }

    @Override
    public void receiveMessage(TileEvent event) throws RemoteException {
        client.updateBoard(event);
    }


//
//    private void inputLoop() throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String cmd;
//        while (!Objects.equals(cmd = br.readLine(), "end")) {
//            try{
//                Command command = commandInterpreter.interpret(cmd);
//                server.command(command);
//
//            }
//            catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//        }
//    }




    private void inputLoop(boolean fromConsole) throws IOException {
        String cmd = "";

        if (fromConsole) {
            while (cmd != null && !cmd.equals("end")) {
                try {
                    cmd = client.getView().askInput("Command: ");
                    Command command = commandInterpreter.interpret(cmd);
                    server.command(command);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    cmd = "";
                }
            }
        }
        else {
            InputStream commandFile = getClass().getClassLoader().getResourceAsStream("commands_output.txt");
            assert commandFile != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(commandFile))) {
                while ((cmd = br.readLine()) != null && !cmd.equals("end")) {
                    try {
                        Command command = commandInterpreter.interpret(cmd);
                        server.command(command);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }


        System.out.println("Fine input.");
    }

}
