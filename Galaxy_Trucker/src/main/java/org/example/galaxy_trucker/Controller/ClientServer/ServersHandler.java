package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.Controller.ClientServer.TCP.TCPServer;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;
import org.example.galaxy_trucker.Model.GameLists;

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

        //start thread server tcp
        ConcurrentHashMap<UUID, VirtualView> tokenMap = new ConcurrentHashMap<UUID, VirtualView>();
        TCPServer TCP = new TCPServer(gameHandler, tokenMap);
        Thread ThreadTCP = new Thread(TCP);
        ThreadTCP.start();

        //start thread server rmi
        RMIServer RMI = null;
        try {
            RMI = new RMIServer(gameHandler, tokenMap);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Thread ThreadRMI = new Thread(RMI);
        ThreadRMI.start();

    }

}
