package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GameHandler;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class ServerActions extends UnicastRemoteObject implements ServerInterface {



    Player CurrentPlayer;

    GameHandler gh;

    ArrayList<ClientInterface> clients;

    public ServerActions() throws RemoteException {

        gh = new GameHandler();
        clients = new ArrayList<>();
    }



    @Override
    public void StartServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(Settings.PORT);
        try {
            System.setProperty("java.rmi.server.hostname",Settings.SERVER_NAME);
            registry.bind("CommandReader", this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Server ready");
    }

    @Override
    public void login(ClientInterface client) throws RemoteException{
        clients.add(client);
    }


    @Override
    public void command(Command cmd) throws RemoteException{
        gh.receive(cmd);
        System.out.println(cmd);

    }



    @Override
    public void JoinGame(ClientInterface joiner, String playerName, String GameName) throws RemoteException{

        if(!clients.contains(joiner)){
            throw new IllegalArgumentException("Client not found");
        }

        Game joining = gh.getGameList().getGames().stream().filter(g -> g.getID().equals(GameName)).findFirst().orElseThrow();


        joiner.setPlayerId(playerName);

        try{
            gh.getGameList().JoinGame(joining, joiner.getPlayer());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        joiner.setGame(joining);

    }



    @Override
    public void CreateGame(ClientInterface joiner, String playerName, String GameName, int lv) throws RemoteException{

        for(Game  g : gh.getGameList().getGames()){
            if(g.getID().equals(GameName)){
                throw new RuntimeException("Game with this ID already exists");
            }
        }

        joiner.setPlayerId(playerName);

        try {
            Game g = gh.getGameList().CreateNewGame(GameName, joiner.getPlayer(), lv);
            joiner.setGame(g);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }




    public static void main(String[] args){
        try {
            new ServerActions().StartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
