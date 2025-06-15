package org.example.galaxy_trucker.Controller;

import javafx.util.Pair;
import org.example.galaxy_trucker.ClientServer.RMI.RMIServer;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Listeners.GhListener;
import org.example.galaxy_trucker.Controller.Listeners.LobbyListener;
import org.example.galaxy_trucker.Controller.Messages.ConnectionRefusedEvent;
import org.example.galaxy_trucker.Controller.Messages.LobbyEvent;
import org.example.galaxy_trucker.Controller.Messages.ReconnectedEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GamesHandler implements LobbyListener {

    private final HashMap<String, String> tokenToGame = new HashMap<>();
    private final HashMap<String, GameController> gameControllerMap;
    private final BlockingQueue<Pair<Command, VirtualView>> pendingLogins;
    private ArrayList<GhListener> listeners = new ArrayList<>();
    private RMIServer rmi;

    public void setRmiServer(RMIServer rmi) {
        this.rmi = rmi;
    }

    public void setListeners(GhListener listener) {
        this.listeners.add(listener);
    }

    public GamesHandler() {
        this.gameControllerMap = new HashMap<>();
        this.pendingLogins = new LinkedBlockingQueue<>();


        Thread loginWorker = new Thread(this::processPendingLogins);
        loginWorker.setDaemon(true);
        loginWorker.start();
    }

    public void enqueuePlayerInit(Command command, VirtualView virtualView) {
        pendingLogins.offer(new Pair<>(command, virtualView));
    }

    private void processPendingLogins() {
        while (true) {
            try {

                Pair<Command, VirtualView> entry = pendingLogins.take();

                System.out.println(entry.getKey().playerId);

                initPlayer(entry.getKey(), entry.getValue());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.out.println("Error while initializing player: " + e.getMessage());
            }
        }
    }


    public void receive(Command command) {

        try{
            System.out.println("Received: " + command.getTitle() + " " + command.getClass().getSimpleName());
            String title = command.getTitle();
            String gameId = command.getGameId();
            if ("Quit".equals(title)) {
                if (gameControllerMap.containsKey(gameId)) {
                    gameControllerMap.get(gameId).removePlayer(command.getToken(), command);

                } else {
                    System.out.println("No player found for token: " + command.getToken());
                    //throw new InvalidInput("GameId doesn't exist: " + gameId);
                }
            } else {
                if (gameControllerMap.containsKey(gameId)) {
                    gameControllerMap.get(gameId).addCommand(command);
                } else {
                    //throw new InvalidInput("GameId doesn't exist: " + gameId);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void removeGame(String gameId) {
        System.out.println("Removing game: " + gameId);
        String toRemove = null;
        for (String token : tokenToGame.keySet()) {
            if (tokenToGame.get(token).equals(gameId)) {
                toRemove = token;
            }
        }
        tokenToGame.remove(toRemove);
        gameControllerMap.remove(gameId);
    }

    public void initPlayer(Command command, VirtualView virtualView) {
        System.out.println("initplayer");
        try {
            String gameID = command.getGameId();
            String check = "";
            if (gameControllerMap.containsKey(gameID)) {
                check = gameControllerMap.get(gameID).check(command);

            }

            if (!check.equals("")){
                virtualView.sendEvent(new ConnectionRefusedEvent(check));
            }

            else {
                String playerID = command.getPlayerId();
                int lvl = command.getLv();

                Player temp = new Player();
                temp.setId(playerID);
                temp.setPhaseListener(virtualView);
                temp.setState(new BaseState());
                synchronized (tokenToGame) {
                    tokenToGame.putIfAbsent(virtualView.getToken(), gameID);
                }

                if (gameControllerMap.containsKey(gameID)) {
                    System.out.println("Game exists: " + gameID);
                        gameControllerMap.get(gameID).NewPlayer(temp, virtualView, virtualView.getToken());
                } else {
                    System.out.println("Game doesn't exist: " + gameID);
                    Game curGame = new Game(lvl, gameID);

                    synchronized (gameControllerMap) {
                        GameController gameController = new GameController(gameID, curGame, this,command.getLv(), command.getMaxPlayers());
                        gameController.setLobbyListener(this);
                        gameControllerMap.putIfAbsent(gameID, gameController);
                        gameControllerMap.get(curGame.getGameID()).NewPlayer(temp, virtualView, virtualView.getToken());
                    }
                    System.out.println("Pending?: ");
                    rmi.addPending(virtualView.getToken());

                }

            }

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }

    public synchronized HashMap<String, GameController> getGameControllerMap() {
        return gameControllerMap;
    }

    public void PlayerDisconnected(String token) {

        String game;

        synchronized (tokenToGame) {
            game = tokenToGame.get(token);
        }
        if (game == null) {
            throw new InvalidInput("game null: ");
        }

        synchronized (gameControllerMap) {
            gameControllerMap.get(game).stopPlayer(token);
//            if (gameControllerMap.get(tokenToGame.get(token)).getNumPlayer() == 0) {
//                removeGame(tokenToGame.get(token));
//            }
        }

    }

    public void PlayerReconnected(String token) {
        String game;


        synchronized (tokenToGame) {
            game = tokenToGame.get(token);
        }
        if (game == null) {
            throw new InvalidInput("game null: ");
        }

        synchronized (gameControllerMap) {
            gameControllerMap.get(game).startPlayer(token);
        }
    }

    @Override
    public void sendEvent(LobbyEvent event) {
        for (GhListener listener : listeners) {
            listener.sendEvent(event);
        }
    }

    public ArrayList<GhListener> getListeners() {
        return listeners;
    }
}
