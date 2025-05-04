package org.example.galaxy_trucker.Controller.ClientServer.RMI;

import javafx.scene.chart.ScatterChart;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.*;
import org.example.galaxy_trucker.Controller.ClientServer.Settings;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;




public class RMIServer extends UnicastRemoteObject implements ServerInterface, Runnable {


    GamesHandler gh;
    private ConcurrentHashMap<UUID, VirtualView> tokenMap;
    private final HashMap<ClientInterface, UUID> clients;
    private  ArrayList<UUID> DisconnectedClients = new ArrayList<>();
    private final HashMap<ClientInterface, Integer> attempts = new HashMap<>();

    public RMIServer(GamesHandler gamesHandler, ConcurrentHashMap<UUID, VirtualView> tokenMap, ArrayList<UUID> DisconnectedClients) throws RemoteException {
        this.tokenMap = tokenMap;
        gh = gamesHandler;
        clients = new HashMap<>();
        this.DisconnectedClients = DisconnectedClients;

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
            cmd.getClient().receiveToken(token.toString());
            vv.setToken(token);
            gh.enqueuePlayerInit(cmd,vv);
        }
        else if (cmd.getTitle().equals("Reconnect")){
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


}
