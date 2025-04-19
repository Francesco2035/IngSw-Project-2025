package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Model.GameLists;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements  Runnable{

    private static GamesHandler gamesHandler;

    public TCPServer(GamesHandler gamesHandler) {
        this.gamesHandler = gamesHandler;
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




        // loop forever accepting...
        while (true) {
            Socket clientSocket = null;
            try {
                assert serverSocket != null;
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accepted");
            MultiClientHandler clientHandler = new MultiClientHandler(clientSocket, gamesHandler);
            Thread t = new Thread(clientHandler);
            t.start();
        }
    }
}
