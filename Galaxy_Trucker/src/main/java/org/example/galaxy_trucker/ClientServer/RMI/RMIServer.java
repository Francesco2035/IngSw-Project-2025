package org.example.galaxy_trucker.ClientServer.RMI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.*;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;




public class RMIServer extends UnicastRemoteObject implements ServerInterface, Runnable , GhListener {


    GamesHandler gh;
    private ConcurrentHashMap<UUID, VirtualView> tokenMap;
    private final HashMap<ClientInterface, UUID> clients;
    private  ArrayList<UUID> DisconnectedClients = new ArrayList<>();
    private final HashMap<ClientInterface, Integer> attempts = new HashMap<>();
    private ArrayList<ClientInterface> lobby = new ArrayList<>();
    private HashMap<String, LobbyEvent> lobbyEvents;

    public RMIServer(GamesHandler gamesHandler, ConcurrentHashMap<UUID, VirtualView> tokenMap, ArrayList<UUID> DisconnectedClients) throws RemoteException {
        this.tokenMap = tokenMap;
        gh = gamesHandler;
        clients = new HashMap<>();
        this.DisconnectedClients = DisconnectedClients;

        //se lato client termino il programma, il server lancia remoteException, se semplicemente spengo il wifi del client, il server si blocca su quella chiamata del  receive ping, pertanto devo usare il timeout

        Thread pingThread = new Thread(() -> {
            //System.out.println("Ping Thread started");

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Map<ClientInterface, UUID> clientsSnapshot;
                synchronized (clients) {
                    clientsSnapshot = new HashMap<>(clients);
                }

                for (ClientInterface client : clientsSnapshot.keySet()) {
                    try {


                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Future<Void> future = executor.submit(() -> {
                            try {
                                client.receivePing();
                            } catch (RemoteException e) {
                                throw new ExecutionException(e);
                            }
                            return null;
                        });


                        future.get(1500, TimeUnit.MILLISECONDS);

                        System.out.println("Ping successful for client: " + client);

                        executor.shutdown();

                    } catch (TimeoutException e) {
//                        System.out.println("Ping timed out for client: " + client);
//                        attempts.putIfAbsent(client, 3);
//                        int currentAttempts = attempts.get(client);
//                        attempts.put(client, currentAttempts - 1);
                        //if (attempts.get(client) <= 0) {
                            UUID token = clientsSnapshot.get(client);
                            if (token != null) {
                                //attempts.remove(client);
                                VirtualView vv = tokenMap.get(token);


                                if (vv != null && !vv.getDisconnected()) {
                                    vv.setDisconnected(true);
                                    vv.setClient(null);

                                    System.out.println("Client disconnected: " + client);
                                        synchronized (DisconnectedClients) {
                                            DisconnectedClients.add(token);
                                        }
                                        synchronized (clients) {
                                            clients.remove(client);
                                        }
                                    gh.PlayerDisconnected(token);

                                }
                            }
                        //}
                    }
                    catch (ExecutionException | InterruptedException e) {
                        UUID token = clientsSnapshot.get(client);
                        if (token != null ) {

                            VirtualView vv = tokenMap.get(token);
                            //attempts.remove(client);

                            if (vv != null && !vv.getDisconnected()) {
                                vv.setDisconnected(true);
                                vv.setClient(null);
                                System.out.println("Client disconnected: " + client);

                                synchronized (DisconnectedClients) {
                                    DisconnectedClients.add(token);
                                }
                                synchronized (clients) {
                                    clients.remove(client);
                                }
                                gh.PlayerDisconnected(token);
                            }
                        }

                }


             }}
            });

        pingThread.start();
        lobbyEvents = new HashMap<>();
        lobbyEvents.put("EMPTY CREATE NEW GAME", new LobbyEvent("EMPTY CREATE NEW GAME", -1,null));


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
    public void receivePong() throws RemoteException {

    }


    @Override
    public void command(Command cmd) throws RemoteException{
        if (cmd.getTitle().equals("Lobby")){
            System.out.println("LOBBY");
            lobby.add(cmd.getClient());
            new Thread(()->{
                synchronized (lobbyEvents) {
                    for (LobbyEvent event : lobbyEvents.values()) {
                        try {
                            cmd.getClient().receiveMessage(event);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            lobby.remove(cmd.getClient());
                        }
                    }
                }
            }).start();
        }
        else if (cmd.getTitle().equals("Login")){
            System.out.println("LOGIN");

            lobby.remove(cmd.getClient());
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
            cmd.getClient().receiveToken(token.toString());
            vv.setToken(token);
            gh.enqueuePlayerInit(cmd,vv);
        }
        else if (cmd.getTitle().equals("Reconnect")){
            System.out.println("RECONNECT");

            UUID token = UUID.fromString(cmd.getToken());
            System.out.println(cmd.getToken() + " " + cmd.getClient());
            if (tokenMap.containsKey(token) && DisconnectedClients.contains(token)) {

                VirtualView temp = tokenMap.get(token);
                temp.setClient(cmd.getClient());
                temp.setDisconnected(false);

                synchronized (DisconnectedClients) {
                    DisconnectedClients.remove(token);
                }
                synchronized (clients) {
                    clients.put(cmd.getClient(), token);
                }


                new Thread(temp::reconnect).start();
                gh.PlayerReconnected(token);

                System.out.println("Client reconnected: " + token);
            }
            else {
                System.out.println("Reconnection not possible: " + token);
            }
        }
        else{
            System.out.println("TO_GH");

            gh.receive(cmd);
        }
        System.out.println(cmd);
    }


    public void run(){
        try {
            this.StartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendEvent(LobbyEvent event) {
        System.out.println("Sending event: " + event);
        lobbyEvents.remove("EMPTY CREATE NEW GAME");
        lobbyEvents.remove(event.getGameId());
        lobbyEvents.put(event.getGameId(), event);
        try{
            for (ClientInterface client: lobby){
                client.receiveMessage(event);
            }
        }
        catch (RemoteException e){
            e.printStackTrace();
        }

    }
}
