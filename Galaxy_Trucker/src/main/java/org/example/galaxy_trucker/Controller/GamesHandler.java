package org.example.galaxy_trucker.Controller;

import javafx.util.Pair;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Controller.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GamesHandler {

    private final HashMap<UUID, String> tokenToGame = new HashMap<>();
    private final HashMap<String, GameController> gameControllerMap;
    private final BlockingQueue<Pair<Command, VirtualView>> pendingLogins;

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

        System.out.println("Received: " + command.getTitle() + " " + command.getClass().getSimpleName());
        String title = command.getTitle();
        String gameId = command.getGameId();
        if ("Quit".equals(title)) {
            if (gameControllerMap.containsKey(gameId)) {
                gameControllerMap.get(gameId).removePlayer(UUID.fromString(command.getToken()));
            } else {
                throw new InvalidInput("GameId doesn't exist: " + gameId);
            }
        } else {
            if (gameControllerMap.containsKey(gameId)) {
                gameControllerMap.get(gameId).addCommand(command);
            } else {
                throw new InvalidInput("GameId doesn't exist: " + gameId);
            }
        }

    }


    public void removeGame(String gameId) {
        System.out.println("Removing game: " + gameId);
        for (UUID token : tokenToGame.keySet()) {
            if (tokenToGame.get(token).equals(gameId)) {
                tokenToGame.remove(token);
            }
        }
        gameControllerMap.remove(gameId);
    }

    public void initPlayer(Command command, VirtualView virtualView) {
        try {
            String gameID = command.getGameId();
            if (gameControllerMap.containsKey(gameID) && gameControllerMap.get(gameID).isStarted()) {
                throw new InvalidInput("Game already started: " + gameID);
            }
            String playerID = command.getPlayerId();
            int lvl = command.getLv();

            Player temp = new Player();
            temp.setId(playerID);
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
                    gameControllerMap.putIfAbsent(gameID, new GameController(gameID, curGame, this));
                    gameControllerMap.get(curGame.getGameID()).NewPlayer(temp, virtualView, virtualView.getToken());
                }

            }

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }

    public synchronized HashMap<String, GameController> getGameControllerMap() {
        return gameControllerMap;
    }

    public void PlayerDisconnected(UUID token) {


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

    public void PlayerReconnected(UUID token) {
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

}
