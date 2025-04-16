package org.example.galaxy_trucker.Controller.ClientServer;

import org.example.galaxy_trucker.Controller.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.Controller.ClientServer.TCP.TCPServer;
import org.example.galaxy_trucker.Controller.GameHandler;
import org.example.galaxy_trucker.Model.GameLists;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServersHandler implements Runnable {

    private ArrayList<String> cmdQueue;


    public static void main(String[] args){
        ServersHandler handler = new ServersHandler();
        handler.run();
    }

    public void run(){

        cmdQueue = new ArrayList<>();
        GameLists gameLists = new GameLists();
        GameHandler gameHandler = new GameHandler(gameLists);

        //start thread server tcp
        TCPServer TCP = new TCPServer(cmdQueue);
        Thread ThreadTCP = new Thread(TCP);
        ThreadTCP.start();

        //start thread server rmi
        RMIServer RMI = null;
        try {
            RMI = new RMIServer(cmdQueue);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Thread ThreadRMI = new Thread(RMI);
        ThreadRMI.start();

        while(true){

            synchronized (cmdQueue){
                if(!cmdQueue.isEmpty())
                    gameHandler.Receive(cmdQueue.removeFirst());
                    //System.out.println(cmdQueue.removeFirst());
            }

        }

    }

}
