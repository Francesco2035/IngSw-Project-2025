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

public class GamesHandler {

//    private final GameLists gameList;

    //private final ArrayList<Command> commandList = new ArrayList<>();  // Lista di comandi
    private final HashMap<String, GameController> gameControllerMap;

    public GamesHandler() {
//        this.gameList = new GameLists();
        this.gameControllerMap = new HashMap<>();
    }
//
//    public ConcurrentHashMap<String, ConcurrentHashMap<String, Controller>> getGameMap() {
//        return gameMap;
//    }


    public void receive(Command command) {

        System.out.println("Received: " + command.getTitle() + " " + command.getClass().getSimpleName());
        String title = command.getTitle();
        String gameId = command.getGameId();
        if("Quit".equals(title)) {
            if (gameControllerMap.containsKey(gameId)) {
                gameControllerMap.get(gameId).removePlayer(command.getPlayerId());
            }
            else{
                throw new InvalidInput("GameId doesn't exist: " + gameId);
            }
        }
        else{
            if (gameControllerMap.containsKey(gameId)) {
                gameControllerMap.get(gameId).addCommand(command);
            }
            else{
                throw new InvalidInput("GameId doesn't exist: " + gameId);
            }
        }

    }


    public synchronized void removeGame(String gameId) {
        gameControllerMap.remove(gameId);
    }

    public synchronized void initPlayer(Command command, VirtualView virtualView) {
        try {
            String gameID = command.getGameId();
            if(gameControllerMap.containsKey(gameID) && gameControllerMap.get(gameID).isStarted()) {
                throw new InvalidInput("Game already started: " + gameID);
            }
            String playerID = command.getPlayerId();
            int lvl = command.getLv();

            Player temp = new Player();
            temp.setId(playerID);
            temp.setState(new BaseState());

            if(gameControllerMap.keySet().contains(gameID)){
                gameControllerMap.get(gameID).NewPlayer(temp, virtualView);
            }
            else{
                Game curGame = new Game(lvl, gameID);
                gameControllerMap.putIfAbsent(gameID, new GameController(gameID, curGame, this));
                gameControllerMap.get(curGame.getGameID()).NewPlayer(temp, virtualView);
            }

//            try {
//                System.out.println(this);
//                Game curGame = gameList.CreateNewGame(gameID, temp, lvl);
//                System.out.println(curGame);
//                gameControllerMap.putIfAbsent(gameID, new GameController(gameID, curGame));
//                gameControllerMap.get(curGame.getGameID()).NewPlayer(temp);
//            } catch (IllegalArgumentException e) {
//                try {
//                    Game existingGame = gameList.getGames().stream()
//                            .filter(g -> g.getGameID().equals(gameID))
//                            .findFirst()
//                            .orElseThrow();
//                    gameList.JoinGame(existingGame, temp);
//                    gameControllerMap.get(existingGame.getGameID()).NewPlayer(temp);
//                } catch (Exception ex) {
//                    System.out.println("Join error: " + ex.getMessage());
//                }
//            }
//
        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }
}
