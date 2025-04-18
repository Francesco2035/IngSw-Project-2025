package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Controller.GameHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiClientHandler implements Runnable {

    private Socket clientSocket;
    private GameHandler gameHandler;

    private long lastPingTime;

    public MultiClientHandler(Socket clientSocket, GameHandler gameHandler) {
        this.clientSocket = clientSocket;
        this.gameHandler = gameHandler;
    }

    @Override
    public void run() {
        clientLoop();
    }

    private void clientLoop() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String s;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while ((s = in.readLine()) != null) {

                if(s.equals("ping")){
                    lastPingTime = System.currentTimeMillis();
                    out.println("pong");
                    //System.out.println("pong");
                }
                else{
                    System.out.println("Received: " + s);

                    Command command = objectMapper.readValue(s, Command.class);

                    System.out.println("Deserialized command: " + command.getTitle());
                    gameHandler.receive(command);
                }

                if (System.currentTimeMillis() - lastPingTime > 15000) {
                    System.out.println("Timeout client: " + clientSocket.getInetAddress());
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

}
