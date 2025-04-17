package org.example.galaxy_trucker.Controller;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameHandler {

    private final GameLists gameList;
    private final ConcurrentHashMap<String, Controller> controllerMap;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Controller>> gameMap;
    private final ArrayList<Command> commandList = new ArrayList<>();  // Lista di comandi

    public GameHandler() {
        this.gameList = new GameLists();
        this.controllerMap = new ConcurrentHashMap<>();
        this.gameMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Controller>> getGameMap() {
        return gameMap;
    }

    public void setGameMap(String gameId, Player player, Controller controller) {
        gameMap.computeIfAbsent(gameId, k -> new ConcurrentHashMap<>())
                .put(player.GetID(), controller);
    }

    public void receive(Command command) {
        System.out.println("Received: " + command.getTitle() + " " + command.getClass().getSimpleName());
        synchronized (commandList) {
            commandList.add(command);
        }
        applyCommands();
    }

    public void applyCommands() {
        synchronized (commandList) {
            while (!commandList.isEmpty()) {
                Command command = commandList.remove(0);
                System.out.println("Applying: " + command.getTitle());
                String title = command.getTitle();

                if (!"Login".equals(title)) {
                    String gameId = command.getGameId();
                    String playerId = command.getPlayerId();

                    Controller controller = gameMap.getOrDefault(gameId, new ConcurrentHashMap<>()).get(playerId);
                    if (controller != null) {
                        controller.action(command, this);
                    } else {
                        System.out.println("Command ignored temporarily: " + title + " for " + gameId + "/" + playerId);
                    }
                } else {
                    initPlayer(command);
                }
            }
        }
    }

    public synchronized void initPlayer(Command command) {
        try {
            String gameID = command.getGameId();
            String playerID = command.getPlayerId();
            int lvl = command.getLv();

            Player temp = new Player();
            temp.setId(playerID);
            temp.setState(new BaseState());

            try {
                System.out.println(this);
                System.out.println(gameList.CreateNewGame(gameID, temp, lvl));
                gameMap.putIfAbsent(gameID, new ConcurrentHashMap<>());
            } catch (IllegalArgumentException e) {
                try {
                    Game existingGame = gameList.getGames().stream()
                            .filter(g -> g.getGameID().equals(gameID))
                            .findFirst()
                            .orElseThrow();
                    gameList.JoinGame(existingGame, temp);
                } catch (Exception ex) {
                    System.out.println("Join error: " + ex.getMessage());
                }
            }

            try {
                Game currentGame = gameList.getGames().stream()
                        .filter(g -> g.getGameID().equals(gameID))
                        .findFirst()
                        .orElseThrow();

                Player joinedPlayer = currentGame.getPlayers().values().stream()
                        .filter(p -> p.GetID().equals(playerID))
                        .findFirst()
                        .orElseThrow();

                setGameMap(gameID, joinedPlayer, new LoginController(joinedPlayer, gameID));
            } catch (Exception ex) {
                System.out.println("Controller init error: " + ex.getMessage());
            }

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }

    public synchronized void changeState(String gameId) {
        Game curGame = gameList.getGames().stream()
                .filter(g -> g.getGameID().equals(gameId))
                .findFirst()
                .orElseThrow();

        long readyCount = curGame.getPlayers().values().stream()
                .filter(Player::GetReady)
                .count();

        if (readyCount == curGame.getPlayers().size()) {
            synchronized (curGame) {
                gameMap.getOrDefault(gameId, new ConcurrentHashMap<>())
                        .values()
                        .forEach(controller -> controller.nextState(this));

                curGame.getPlayers().values().forEach(player -> player.SetReady(false));
            }
        }
    }
}
