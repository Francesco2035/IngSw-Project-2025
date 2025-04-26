package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MultiClientHandler implements Runnable {

    private Socket clientSocket;
    private GamesHandler gameHandler;
    private ConcurrentHashMap<UUID, VirtualView> tokenMap;

    private long lastPingTime;

    public MultiClientHandler(Socket clientSocket, GamesHandler gameHandler, ConcurrentHashMap<UUID, VirtualView> tokenMap) {
        this.clientSocket = clientSocket;
        this.gameHandler = gameHandler;
        this.tokenMap = tokenMap;
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
                    if (command.getTitle().equals("Login")){
                        UUID token  = null;
                        VirtualView vv = new VirtualView(command.getPlayerId(), command.getGameId(), command.getClient(), new PrintWriter(clientSocket.getOutputStream(), true));
                        synchronized (tokenMap){
                            do{
                                token = UUID.randomUUID();
                            }while(tokenMap.containsKey(token));
                            tokenMap.put(token, vv);
                        }
                        gameHandler.initPlayer(command,vv);
                    }
                    else{
                        gameHandler.receive(command);

                    }
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
