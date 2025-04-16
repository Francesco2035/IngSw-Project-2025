package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GameHandler;
import org.example.galaxy_trucker.Model.GAGen;
import org.example.galaxy_trucker.Model.GameLists;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    private static GameLists gameLists;
    private static GameHandler gameHandler;

    public static void main(String[] args) {

        System.out.println("Server Started!");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(Settings.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Listening on port " + Settings.PORT + "...");


        gameHandler = new GameHandler();


        while (true) {
            Socket clientSocket = null;
            try {
                assert serverSocket != null;
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accepted");
            MultiClientHandler clientHandler = new MultiClientHandler(clientSocket, gameHandler);
            Thread t = new Thread(clientHandler);
            t.start();
        }
    }
}
