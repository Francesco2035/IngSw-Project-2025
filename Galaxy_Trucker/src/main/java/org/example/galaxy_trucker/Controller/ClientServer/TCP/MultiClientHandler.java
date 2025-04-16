package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
import org.example.galaxy_trucker.Controller.GameHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class MultiClientHandler implements Runnable {

    private Socket clientSocket;
    private GameHandler gameHandler;

    public MultiClientHandler(Socket clientSocket, GameHandler gameHandler) {
        this.clientSocket = clientSocket;
        this.gameHandler = gameHandler;
    }

    @Override
    public void run() {
        clientLoop();
    }

    private void clientLoop() {
        
        //String ClientId = "";
        
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // waits for data and reads it in until connection dies
        // readLine() blocks until the server receives
        // a new line from client

        String s = "";


//        try {
////            ClientId = in.readLine();
////              System.out.println(ClientId + " Joined");
////            out.println("Hello "+ ClientId);
//
//            assert in != null;
//            s = in.readLine();
//            gameHandler.Receive(s);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        try {
            while (true) { //(s = in.readLine()) != null
                System.out.println(s);
                s = in.readLine();
                gameHandler.Receive(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
