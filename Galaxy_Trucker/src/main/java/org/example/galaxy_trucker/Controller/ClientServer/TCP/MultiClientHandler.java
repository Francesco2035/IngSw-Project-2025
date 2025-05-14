package org.example.galaxy_trucker.Controller.ClientServer.TCP;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.chart.ScatterChart;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.CommandInterpreter;
import org.example.galaxy_trucker.Controller.DisconnectedClient;
import org.example.galaxy_trucker.Controller.GameController;
import org.example.galaxy_trucker.Controller.GamesHandler;
import org.example.galaxy_trucker.Controller.VirtualView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MultiClientHandler implements Runnable {

    private Socket clientSocket;
    private GamesHandler gameHandler;
    private ConcurrentHashMap<UUID, VirtualView> tokenMap;
    private ArrayList<UUID> disconnectedClients;
    private int attempts = 3;
    private UUID Token;

    private long lastPingTime;

    public MultiClientHandler(Socket clientSocket, GamesHandler gameHandler, ConcurrentHashMap<UUID, VirtualView> tokenMap, ArrayList<UUID> disconnectedClients) {
        this.clientSocket = clientSocket;
        this.gameHandler = gameHandler;
        this.tokenMap = tokenMap;
        this.disconnectedClients = disconnectedClients;
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

            clientSocket.setSoTimeout(3000);
            ObjectMapper objectMapper = new ObjectMapper();
            String s;

            while (attempts > 0) {
                try {
                    s = in.readLine();
                    if (s == null) {
                        System.out.println("Client closed the connection.");
                        break;
                    }
                    System.out.println(s);
                    if (s.equals("ping")) {
                        attempts = 3;
                        out.println("pong");
                    } else {
                        attempts = 3;
                        System.out.println("Received: " + s);

                        Command command = objectMapper.readValue(s, Command.class);
                        System.out.println("Deserialized command: " + command.getTitle());

                        if (command.getTitle().equals("Login")) {
                            UUID token;
                            VirtualView vv = new VirtualView(command.getPlayerId(), command.getGameId(), command.getClient(), out);
                            synchronized (tokenMap) {
                                do {
                                    token = UUID.randomUUID();
                                } while (tokenMap.containsKey(token));
                                Token = token;
                                vv.setToken(token);
                                tokenMap.put(token, vv);
                            }
                            gameHandler.enqueuePlayerInit(command, vv);
                            out.println("Token: " + token.toString());
                        }
                        else if (command.getTitle().equals("Reconnect")) {
                            System.out.println("Reconnecting...");
                            Token = UUID.fromString(command.getToken());

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


}
