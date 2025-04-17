package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler {

    private int count = 0;
    private GameLists gameList;
    private HashMap<String, Controller> controllerMap;
    private HashMap<String, HashMap<String, Controller>> gameMap;

    // Use BlockingQueue for thread-safe command handling
    private BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();

    public GameHandler() {
        this.gameList = new GameLists();
        this.controllerMap = new HashMap<>();
        this.gameMap = new HashMap<>();
        startApplyLoop();
    }

    public GameLists getGameList() {
        return gameList;
    }

    public HashMap<String, HashMap<String, Controller>> getGameMap() {
        return gameMap;
    }

    public void setGameMap(String gameId, Player player, Controller controller) {
        gameMap.get(gameId).put(player.GetID(), controller);
    }

    // Method to receive commands and add them to the queue
    public void receive(Command command) {
        System.out.println("Received: " + command.getTitle() + " " + command.getClass().getSimpleName());
        commandQueue.offer(command);
    }

    // Method that continuously pulls commands and applies them
    public synchronized void startApplyLoop() {
        System.out.println("Starting Apply Loop");
        Thread applyThread = new Thread(() -> {
            while (true) {
                try {
                    Command command = commandQueue.take(); // Blocco finché non c'è un comando
                    System.out.println("Received: " + command.getTitle());
                    String title = command.getTitle();

                    if (!"Login".equals(title)) {
                        String gameId = command.getGameId();
                        String playerId = command.getPlayerId();
                        System.out.println(gameId + " - " + playerId);
                        if (!gameMap.containsKey(gameId) || !gameMap.get(gameId).containsKey(playerId)) {
                            System.out.println("Command ignored temporarily: " + title + " for " + gameId + "/" + playerId);
                            continue;
                        }
                        else {
                            gameMap.get(gameId).get(playerId).action(command, this);
                        }
                    } else {
                        initPlayer(command);
                    }
                } catch (InterruptedException e) {
                    System.err.println("Apply thread interrupted");
                    break;
                }
            }
        });

        applyThread.setDaemon(true);
        applyThread.start();
    }

    // Method to initialize a player
    public void initPlayer(Command command) {
        try {
            String gameID = command.getGameId();
            String playerID = command.getPlayerId();
            int lvl = command.getLv();
            Player temp = new Player();
            temp.setId(playerID);
            temp.setState(new BaseState());

            try {
                gameList.CreateNewGame(gameID, temp, lvl);
                gameMap.put(gameID, new HashMap<>());
            } catch (IllegalArgumentException e) {
                try {
                    gameList.JoinGame(gameList.getGames().get(gameList.getGames().indexOf(
                            gameList.getGames().stream().filter(g -> g.getGameID().equals(gameID)).findFirst().orElseThrow())), temp);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            try {
                gameMap.get(gameID).put(playerID, new LoginController(gameList.getGames().stream().filter(g -> g.getGameID().equals(gameID)).findFirst().orElseThrow()
                        .getPlayers().values().stream().filter(p -> p.GetID().equals(playerID)).findFirst().orElseThrow(), gameID));
            }
            catch (Exception ex){
                System.out.println(ex);
            }

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }

    // Method to check if all players are ready and change the game state if they are
    public void changeState(String gameId) {
        Game curGame = gameList.getGames().get(gameList.getGames().indexOf(gameList.getGames().stream().filter(g -> g.getGameID().equals(gameId)).findFirst().orElseThrow()));

        count = 0;
        for (Player player : curGame.getPlayers().values()) {
            if (player.GetReady()) count++;
        }

        if (count == curGame.getPlayers().size()) {
            for (Controller controller : gameMap.get(gameId).values()) {
                controller.nextState(this);
            }
            for (Player player : curGame.getPlayers().values()) {
                player.SetReady(false);
            }
        }
    }
}
