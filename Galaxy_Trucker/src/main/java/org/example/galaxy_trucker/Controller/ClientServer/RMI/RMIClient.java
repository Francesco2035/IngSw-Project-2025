package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
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

    public RMIClient() throws RemoteException{
        me =  new Player();
        myGame = null;
    }

    @Override
    public void StartClient() throws IOException, NotBoundException {

        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME, Settings.RMI_PORT);

        this.server = (ServerInterface) registry.lookup("CommandReader");
//        this.server.login(this);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Insert player ID: ");
        String playerId = br.readLine();

        System.out.println("Insert game ID: ");
        String gameId = br.readLine();
        System.out.println("Insert game level: ");
        String level = br.readLine();

        String fullCommand = "Login " + playerId + " " + gameId + " " + level;

        commandInterpreter = new CommandInterpreter(playerId, gameId);
        Command loginCommand = commandInterpreter.interpret(fullCommand);

        server.command(loginCommand);

        this.inputLoop(br.readLine().equals("true"));
    }

    private void inputLoop() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String cmd;
        while (!Objects.equals(cmd = br.readLine(), "end")) {
            try{
                Command command = commandInterpreter.interpret(cmd);
                server.command(command);

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }







private void inputLoop(boolean fromConsole) throws IOException {
    BufferedReader br;

    if (fromConsole) {
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Inserisci comandi JSON:");
    } else {
        InputStream commandFile = getClass().getClassLoader().getResourceAsStream("building_commands_inline.json");
        if (commandFile == null) {
            throw new FileNotFoundException("File building_commands_inline.json non trovato nel classpath.");
        }
        br = new BufferedReader(new InputStreamReader(commandFile));
        System.out.println("Lettura dei comandi dal file building_commands_inline.json");
    }

    String cmd;
    while ((cmd = br.readLine()) != null) {
        if (cmd.equals("[") || cmd.equals("]")) continue;
//        System.out.println("Comando ricevuto: " + cmd);
        Command command = commandInterpreter.interpret(cmd);
        server.command(command);
    }

    System.out.println("Fine input.");
}







public static void main(String[] args) throws RemoteException, NotBoundException {
        try {
            new RMIClient().StartClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
