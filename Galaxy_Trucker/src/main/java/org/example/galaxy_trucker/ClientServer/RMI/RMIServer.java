package org.example.galaxy_trucker.ClientServer.RMI;

import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Controller.*;
import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.Controller.Messages.ConnectionRefusedEvent;
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
    private ConcurrentHashMap<String, VirtualView> tokenMap;
    private final HashMap<ClientInterface, String> clients;
    private  ArrayList<String> DisconnectedClients = new ArrayList<>();
    private final HashMap<ClientInterface, Integer> attempts = new HashMap<>();
    private ArrayList<ClientInterface> lobby = new ArrayList<>();
    private HashMap<String, LobbyEvent> lobbyEvents;
    private HashMap<String, ClientInterface> pending = new HashMap<>();
    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(8);
    private HashMap<ClientInterface, ExecutorService> pingLobby = new HashMap<>();

    public RMIServer(GamesHandler gamesHandler, ConcurrentHashMap<String, VirtualView> tokenMap, ArrayList<String> DisconnectedClients) throws RemoteException {
        this.tokenMap = tokenMap;
        gh = gamesHandler;
        clients = new HashMap<>();
        this.DisconnectedClients = DisconnectedClients;

        //ping thread
        Thread pingThread = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Map<ClientInterface, String> clientsSnapshot;
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

                        //System.out.println("Ping successful for client: " + client);

                        executor.shutdown();

                    } catch (TimeoutException e) {

                        String token = clientsSnapshot.get(client);
                        if (token != null) {

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
                    }
                    catch (ExecutionException | InterruptedException e) {
                    String token = clientsSnapshot.get(client);
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
        lobbyEvents.put("EMPTY CREATE NEW GAME", new LobbyEvent("EMPTY CREATE NEW GAME", -1,null, -1));


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
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<Void> future = executor.submit(() -> {
                        try {
                            for (LobbyEvent event : lobbyEvents.values()) {
                                    cmd.getClient().receiveMessage(event);
                            }
                        } catch (RemoteException e) {
                            lobby.remove(cmd.getClient());
                            throw new ExecutionException(e);
                        }
                        return null;
                    });

                    try {
                        future.get(1500, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        lobby.remove(cmd.getClient());
                    }
                    executor.shutdown();

                }
            }).start();

            ScheduledExecutorService pingExecutor = Executors.newSingleThreadScheduledExecutor();

            pingExecutor.scheduleAtFixedRate(() -> {
                try {
                    cmd.getClient().receivePing();
                    System.out.println("PING");
                } catch (RemoteException e) {
                    System.out.println("Client disconnected: " + cmd.getClient());
                    lobby.remove(cmd.getClient());
                    pingExecutor.shutdown();
                }
            }, 0, 3, TimeUnit.SECONDS);
            pingLobby.put(cmd.getClient(), pingExecutor);

        }
        else if (cmd.getTitle().equals("Login")){
            System.out.println("LOGIN");
            LoginCommand lcmd = (LoginCommand) cmd;
            ExecutorService ex = pingLobby.remove(cmd.getClient());
            if (ex != null && !ex.isTerminated()) {
                ex.shutdown();
            }

            lobby.remove(cmd.getClient());
            UUID token  = null;
            VirtualView vv = new VirtualView(cmd.getPlayerId(), cmd.getGameId(), cmd.getClient(), null);
            String shortToken = "";
            synchronized (tokenMap){
                do{
                    token = UUID.randomUUID();
                    shortToken = token.toString().substring(0,8);
                }while(tokenMap.containsKey(shortToken));
                tokenMap.put(shortToken, vv);
                System.out.println("Generated token "+shortToken+" for "+cmd.getPlayerId());
            }
            synchronized (pending){
                pending.put(shortToken, cmd.getClient());
            }
            vv.setToken(shortToken);
            gh.enqueuePlayerInit(cmd,vv);

        }
        else if (cmd.getTitle().equals("Reconnect")){
            System.out.println("RECONNECT:");
            String token = cmd.getToken();
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

                asyncExecutor.submit(temp::reconnect);
                reconnectPlayer(token);

            }
            else {
                cmd.getClient().receiveMessage(new ConnectionRefusedEvent("Reconnection not possible: " + token));
            }
        }
        else{
            gh.receive(cmd);
        }
    }


    public void run(){
        try {
            this.StartServer();
        } catch (Exception e) {
            System.err.println("Error starting Rmi Server: "+ e.getMessage());
        }
    }


    @Override
    public void sendEvent(LobbyEvent event) {

        System.out.println("Sending event: " + event);
        lobbyEvents.remove("EMPTY CREATE NEW GAME");
        lobbyEvents.remove(event.getGameId());
        lobbyEvents.put(event.getGameId(), event);
        ArrayList<ClientInterface> toRemove = new ArrayList<>();

            for (ClientInterface client : lobby) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<Void> future = executor.submit(() -> {
                    try {
                        client.receiveMessage(event);
                    } catch (RemoteException e) {
                        toRemove.add(client);
                        throw new ExecutionException(e);
                    }
                    return null;
                });

                try{
                    future.get(3000, TimeUnit.MILLISECONDS);

                    //System.out.println("Ping successful for client: " + client);

                    executor.shutdown();
                }
                catch(InterruptedException | ExecutionException | TimeoutException e){
                    toRemove.add(client);
                }

            }

            lobby.removeAll(toRemove);

    }

    @Override
    public void updateLobby(LobbyEvent event) {
    }

    @Override
    public void quitPlayer(QuitCommand event) {
        synchronized (clients){
            clients.remove(event.getClient());
        }
    }

    public void addPending(String token){
        if (pending.containsKey(token)){
            System.out.println("removing pending ...");
            ClientInterface client;
            synchronized (pending){
                client = pending.remove(token);
            }
            try{
                client.receiveToken(token);
            } catch (RemoteException e) {
                System.out.println("cannot send token: "+token);
            }

            synchronized (clients) {
                clients.put(client, token);
            }
        }

    }

    public void reconnectPlayer(String token) throws RemoteException {
        asyncExecutor.submit(() -> {
            try {
                gh.PlayerReconnected(token);;
                System.out.println("Client reconnected: " + token);
            } catch (Exception e) {
                System.out.println("cannot reconnect player: "+token);
            }
        });
    }
}
