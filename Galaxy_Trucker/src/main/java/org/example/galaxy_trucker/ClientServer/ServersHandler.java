package org.example.galaxy_trucker.ClientServer;

import org.example.galaxy_trucker.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.ClientServer.TCP.TCPServer;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServersHandler implements Runnable {



//    public void main(String[] args){
//        ServersHandler handler = new ServersHandler();
//        handler.run();
//    }

    public void run(){

        //TODO:non so se si riesce a trovare l'ip di zero tier in modo dinamico
        //Settings.setIp(NetworkUtils.getLocalIPAddress());
        //System.setProperty("java.rmi.server.hostname", Settings.SERVER_NAME);

        //System.out.println("Setting up RMI registry, ip: " + Settings.SERVER_NAME);

        GamesHandler gameHandler = new GamesHandler();

        ArrayList<UUID> DisconnectedClients = new ArrayList<>();

        //start thread server tcp
        ConcurrentHashMap<UUID, VirtualView> tokenMap = new ConcurrentHashMap<UUID, VirtualView>();
        TCPServer TCP = null;
        try {
            TCP = new TCPServer(gameHandler, tokenMap, DisconnectedClients);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Thread ThreadTCP = new Thread(TCP);
        ThreadTCP.start();

        //start thread server rmi
        RMIServer RMI = null;
        try {
            RMI = new RMIServer(gameHandler, tokenMap, DisconnectedClients);
            gameHandler.setRmiServer(RMI);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Thread ThreadRMI = new Thread(RMI);
        gameHandler.setListeners(RMI);
        ThreadRMI.start();

    }

}
