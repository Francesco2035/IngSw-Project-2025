package org.example.galaxy_trucker.ClientServer.TCP;

import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TCPServer implements  Runnable {

    private static GamesHandler gamesHandler;
    private ConcurrentHashMap<String, VirtualView> tokenMap;
    private ArrayList<String> DisconnectedClients;
    private HashMap<MultiClientHandler,Thread> multiClientThreads;

    public TCPServer(GamesHandler gamesHandler, ConcurrentHashMap<String, VirtualView> tokenMap, ArrayList<String> DisconnectedClients) throws RemoteException {
        this.gamesHandler = gamesHandler;
        this.tokenMap = tokenMap;
        this.DisconnectedClients = DisconnectedClients;
        multiClientThreads = new HashMap<>();
    }

    public void run() {

        System.out.println("TCP Server Started!");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(Settings.TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Listening on port " + Settings.TCP_PORT + "...");


        while (true) {
            Socket clientSocket = null;
            try {
                assert serverSocket != null;
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accepted");
            MultiClientHandler clientHandler = new MultiClientHandler(clientSocket, gamesHandler, tokenMap, DisconnectedClients, this);
            gamesHandler.setListeners(clientHandler);
            Thread t = new Thread(clientHandler);
            t.start();
            synchronized (multiClientThreads) {
                multiClientThreads.put(clientHandler, t);
            }
        }
    }

    public void removeMC(MultiClientHandler clientHandler) {
//        synchronized (multiClientThreads) {
//            Thread t = multiClientThreads.remove(clientHandler);
//            try {
//                t.interrupt();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }





}
