package org.example.galaxy_trucker.ClientServer.TCP;

import org.example.galaxy_trucker.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TCPServer implements  Runnable{

    private static GamesHandler gamesHandler;
    private ConcurrentHashMap<UUID, VirtualView> tokenMap;
    private ArrayList<UUID> DisconnectedClients;

    public TCPServer(GamesHandler gamesHandler, ConcurrentHashMap<UUID, VirtualView> tokenMap, ArrayList<UUID> DisconnectedClients) throws RemoteException {
        this.gamesHandler = gamesHandler;
        this.tokenMap = tokenMap;
        this.DisconnectedClients = DisconnectedClients;
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
            MultiClientHandler clientHandler = new MultiClientHandler(clientSocket, gamesHandler, tokenMap, DisconnectedClients);
            gamesHandler.setListeners(clientHandler);
            Thread t = new Thread(clientHandler);
            t.start();
        }
    }
}
