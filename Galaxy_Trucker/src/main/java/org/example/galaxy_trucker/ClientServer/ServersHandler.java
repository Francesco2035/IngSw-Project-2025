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


        GamesHandler gameHandler = new GamesHandler();

        ArrayList<String> DisconnectedClients = new ArrayList<>();

        //start thread server tcp
        ConcurrentHashMap<String, VirtualView> tokenMap = new ConcurrentHashMap<String, VirtualView>();
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
