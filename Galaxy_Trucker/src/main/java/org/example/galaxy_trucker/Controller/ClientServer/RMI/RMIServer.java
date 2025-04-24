package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class RMIServer extends UnicastRemoteObject implements ServerInterface, Runnable {



    Player CurrentPlayer;

    GamesHandler gh;

    ArrayList<ClientInterface> clients;

    public RMIServer(GamesHandler gamesHandler) throws RemoteException {

        gh = gamesHandler;
        clients = new ArrayList<>();
    }



    @Override
    public void StartServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(Settings.RMI_PORT);
        try {
            System.setProperty("java.rmi.server.hostname",Settings.SERVER_NAME);
            registry.bind("CommandReader", this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("RMI Server Started!");
    }

//    @Override
//    public void login(ClientInterface client) throws RemoteException{
//        clients.add(client);
//    }
//
//
    @Override
    public void command(Command cmd) throws RemoteException{
        if (cmd.getTitle().equals("Login")){
            gh.initPlayer(cmd, new VirtualView(cmd.getPlayerId(), cmd.getGameId(), cmd.getClient(), null));
        }
        gh.receive(cmd);
        System.out.println(cmd);

    }
//
//
//
//    @Override
//    public void JoinGame(ClientInterface joiner, String playerName, String GameName) throws RemoteException{
//
//        if(!clients.contains(joiner)){
//            throw new IllegalArgumentException("Client not found");
//        }
//
//        Game joining = gh.getGameList().getGames().stream().filter(g -> g.getID().equals(GameName)).findFirst().orElseThrow();
//
//
//        joiner.setPlayerId(playerName);
//
//        try{
//            gh.getGameList().JoinGame(joining, joiner.getPlayer());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        joiner.setGame(joining);
//
//    }
//
//
//
//    @Override
//    public void CreateGame(ClientInterface joiner, String playerName, String GameName, int lv) throws RemoteException{
//
//        for(Game  g : gh.getGameList().getGames()){
//            if(g.getID().equals(GameName)){
//                throw new RuntimeException("Game with this ID already exists");
//            }
//        }
//
//        joiner.setPlayerId(playerName);
//
//        try {
//            Game g = gh.getGameList().CreateNewGame(GameName, joiner.getPlayer(), lv);
//            joiner.setGame(g);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }




    public void run(){
        try {
            this.StartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
