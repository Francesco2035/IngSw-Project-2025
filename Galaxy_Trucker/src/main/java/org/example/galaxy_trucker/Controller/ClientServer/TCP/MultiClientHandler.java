package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import org.example.galaxy_trucker.Controller.ClientServer.Settings;
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

    public MultiClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        clientLoop();
    }

    private void clientLoop() {
        
        String ClientId = "";
        
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

        try {
            ClientId = in.readLine();
                System.out.println(ClientId + " Joined");
                out.println("Hello "+ ClientId);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((s = in.readLine()) != null) {
                System.out.println(ClientId + " says: " + s);
                out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
