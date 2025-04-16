package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Model.GameLists;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class RMIServer extends UnicastRemoteObject implements ServerInterface, Runnable {

    GameLists gh;

    ArrayList<ClientInterface> clients;

    ArrayList<String> cmdQueue;

    public RMIServer(ArrayList<String> cmds) throws RemoteException {
        cmdQueue = cmds;
        gh = new GameLists();
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



    @Override
    public void append(String cmd) throws RemoteException{
        synchronized (cmdQueue){
            cmdQueue.add(cmd);
        }
//        System.out.println("RMI: "+ cmd);
    }



//    @Override
//    public void JoinGame(ClientInterface joiner, String playerName, String GameName) throws RemoteException{
//
//        if(!clients.contains(joiner)){
//            throw new IllegalArgumentException("Client not found");
//        }
//
//        Game joining = gh.getGames().stream().filter(g -> g.getID().equals(GameName)).findFirst().orElseThrow();
//
//
//        joiner.setPlayerId(playerName);
//
//        try{
//            gh.JoinGame(joining, joiner.getPlayer());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        joiner.setGame(joining);
//
//    }



//    @Override
//    public void CreateGame(ClientInterface joiner, String playerName, String GameName, int lv) throws RemoteException{
//
//        for(Game  g : gh.getGames()){
//            if(g.getID().equals(GameName)){
//                throw new RuntimeException("Game with this ID already exists");
//            }
//        }
//
//        joiner.setPlayerId(playerName);
//
//        try {
//            Game g = gh.CreateNewGame(GameName, joiner.getPlayer(), lv);
//            joiner.setGame(g);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }


    //in caso di errori, aggiungere alle VM options:
    // --add-opens org.example.galaxy_trucker/org.example.galaxy_trucker.Controller.RMI=java.rmi

    public void run(){
        try {
            this.StartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
