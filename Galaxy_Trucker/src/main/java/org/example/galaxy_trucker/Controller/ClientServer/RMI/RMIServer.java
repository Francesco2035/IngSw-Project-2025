package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.ClientServer.Client;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Controller;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class RMIServer extends UnicastRemoteObject implements ServerInterface, Runnable {


    GamesHandler gh;
    private ConcurrentHashMap<UUID, VirtualView> tokenMap;
    private HashMap<ClientInterface, UUID> clients;
    //ArrayList<ClientInterface> clients;

    public RMIServer(GamesHandler gamesHandler, ConcurrentHashMap<UUID, VirtualView> tokenMap) throws RemoteException {
        this.tokenMap = tokenMap;
        gh = gamesHandler;
        clients = new HashMap<>();
        Thread pingThread = new Thread(() -> {
            System.out.println("Ping Thread started");
            ArrayList<ClientInterface> toRemove = new ArrayList<>();
            while(true) {
                for (ClientInterface client : clients.keySet()) {
                    try {
                        client.receivePing();
                    } catch (RemoteException e) {
                        System.out.println("Client disconnesso: " + client);
                        UUID token = clients.get(client);
                        if (token != null) {
                            VirtualView vv = tokenMap.get(token);
                            if (vv != null) vv.setDisconnected(true);
                            handleDisconnection(client);
                        }
                        toRemove.add(client);
                    } catch (Exception ex) {
                        System.out.println("Errore generico con il client: " + client);
                        ex.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (ClientInterface c : toRemove) {
                    clients.remove(c);
                }
            }


        });

        pingThread.start();
    }


    @Override
    public void StartServer() throws RemoteException {
        System.setProperty("java.rmi.server.hostname",Settings.SERVER_NAME);
        Registry registry = LocateRegistry.createRegistry(Settings.RMI_PORT);
        try {
            registry.bind("CommandReader", this);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("RMI Server Started!");
    }




    @Override
    public void command(Command cmd) throws RemoteException{
        if (cmd.getTitle().equals("Login")){
            UUID token  = null;
            VirtualView vv = new VirtualView(cmd.getPlayerId(), cmd.getGameId(), cmd.getClient(), null);
            synchronized (tokenMap){
                do{
                    token = UUID.randomUUID();
                }while(tokenMap.containsKey(token));
                clients.put(cmd.getClient(), token);
                tokenMap.put(token, vv);
                System.out.println(token.toString());
            }
            gh.initPlayer(cmd, vv);
        }
        gh.receive(cmd);
        System.out.println(cmd);
    }


    public void run(){
        try {
            this.StartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleDisconnection(ClientInterface client){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (tokenMap.get(clients.get(client)).getDisconnected()) {
                tokenMap.remove(clients.get(client));
                clients.remove(client);
                System.out.println("Client removed " + client);
                // rimozione da gamecontroller
            }

        });
        thread.start();
    }

}
