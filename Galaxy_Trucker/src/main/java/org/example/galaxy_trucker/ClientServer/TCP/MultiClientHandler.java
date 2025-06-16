package org.example.galaxy_trucker.ClientServer.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.LoadListener;
import javafx.scene.chart.ScatterChart;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Commands.QuitCommand;
import org.example.galaxy_trucker.Controller.DisconnectedClient;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MultiClientHandler implements Runnable, GhListener {

    private Socket clientSocket;
    private GamesHandler gameHandler;
    private ConcurrentHashMap<String, VirtualView> tokenMap;
    private ArrayList<String> disconnectedClients;
    private int attempts = 3;
    private String Token;
    private HashMap<String, LobbyEvent> lobbyEvents = new HashMap<>();
    TCPServer TCP ;

    private long lastPingTime;

    public MultiClientHandler(Socket clientSocket, GamesHandler gameHandler, ConcurrentHashMap<String, VirtualView> tokenMap, ArrayList<String> disconnectedClients, TCPServer TCP) {
        this.clientSocket = clientSocket;
        this.gameHandler = gameHandler;
        this.tokenMap = tokenMap;
        this.disconnectedClients = disconnectedClients;
        this.TCP = TCP;
        lobbyEvents = new HashMap<>();
        lobbyEvents.put("EMPTY CREATE NEW GAME", new LobbyEvent("EMPTY CREATE NEW GAME", -1,null, -1));
    }

    @Override
    public void run() {
        System.out.println("MultiClientHandler started");
        clientLoop();
    }

    private void clientLoop() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            clientSocket.setSoTimeout(10000);
            ObjectMapper objectMapper = new ObjectMapper();
            String s;

            while (attempts > 0) {
                try {
                    s = in.readLine();
                    if (s == null) {
                        System.out.println("Client closed the connection.");
                        break;
                    }
                    //System.out.println(s);
                    if (s.equals("ping")) {
                        attempts = 3;
                        out.println("pong");
                    } else {
                        attempts = 3;
                        //System.out.println("Received: " + s);

                        Command command = objectMapper.readValue(s, Command.class);
                        System.out.println("Deserialized command: " + command.getTitle());

                        if (command.getTitle().equals("Lobby")){
                            System.out.println("Lobby received");
                            PrintWriter finalOut = out;
                            new Thread(()->{
                                synchronized (lobbyEvents) {
                                    for (LobbyEvent event : lobbyEvents.values()) {
                                        ObjectMapper objectMapper1 = new ObjectMapper();
                                        try {
                                            System.out.println("BROOO " + event + " "+ event.getGameId());
                                            finalOut.println(objectMapper1.writeValueAsString(event));
                                        } catch (JsonProcessingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).start();
                        }

                        else if (command.getTitle().equals("Login")) {
                            UUID token;
                            String shortToken = "";
                            VirtualView vv = new VirtualView(command.getPlayerId(), command.getGameId(), command.getClient(), out);
                            synchronized (tokenMap) {
                                do {
                                    token = UUID.randomUUID();
                                    shortToken = token.toString().substring(0,8);
                                } while (tokenMap.containsKey(shortToken));
                                Token = shortToken;
                                vv.setToken(shortToken);
                                tokenMap.put(shortToken, vv);
                            }
                            gameHandler.enqueuePlayerInit(command, vv);
                            out.println("Token: " + shortToken);
                        }


                        else if (command.getTitle().equals("Reconnect")) {
                            System.out.println("Reconnecting...");
                            Token = command.getToken();

                            if (Token != null && disconnectedClients.contains(Token)) {
                                synchronized (tokenMap) {
                                    VirtualView vv = tokenMap.get(Token);
                                    vv.setDisconnected(false);
                                    vv.setPrintWriter(out);
                                    vv.reconnect();
                                }
                                synchronized (disconnectedClients) {
                                    disconnectedClients.remove(Token);
                                }
                                gameHandler.PlayerReconnected(Token);
                            } else {
                                System.out.println("Illegal token");
                                attempts = -1;
                            }
                        } else {
                            gameHandler.receive(command);
                        }
                    }

                } catch (SocketTimeoutException e) {
                    attempts--;
                    System.out.println("Socket timed out, attempts: " + attempts);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Token != null) {
                    synchronized (tokenMap) {
                        VirtualView vv = tokenMap.get(Token);
                        if (vv != null) {
                            vv.setDisconnected(true);
                            vv.setPrintWriter(null);
                        }
                    }
                    synchronized (disconnectedClients) {
                        gameHandler.PlayerDisconnected(Token);
                        if (!disconnectedClients.contains(Token)) {
                            disconnectedClients.add(Token);
                        }
                    }
                }
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }




    @Override
    public void sendEvent(LobbyEvent event) {
        System.out.println(event + " " + event.getGameId());
        lobbyEvents.remove("EMPTY CREATE NEW GAME");
        lobbyEvents.remove(event.getGameId());
        lobbyEvents.put(event.getGameId(), event);
        try{
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ObjectMapper objectMapper = new ObjectMapper();
            out.println(objectMapper.writeValueAsString(event));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void quitPlayer(QuitCommand event) {
        TCP.removeMC(this);
    }
}
