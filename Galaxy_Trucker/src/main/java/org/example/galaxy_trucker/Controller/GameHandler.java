package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.BaseState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GameHandler {


    //    String json;
    private int count = 0;
    private GameLists gameList;
    private HashMap<String, Controller> ControllerMap; //problema
    //hashamp gameid - hasmp
    private HashMap<String, HashMap<String, Controller>> gameMap;

    public GameHandler(GameLists gameList) {
        this.gameList = gameList;
        this.ControllerMap = new HashMap<>();
        this.gameMap = new HashMap<>();
    }


    public HashMap<String, HashMap<String, Controller>> getGameMap() {
        return gameMap;
    }

    public void setGameMap(String gameId, Player player, Controller controller) {
        gameMap.get(gameId).put(player.GetID(), controller);
    }

    public void Receive(String json){
        JsonNode node = JsonHelper.parseJson(json);
        String title = JsonHelper.getRequiredText(node, "title");
        if (!"login".equals(title)) {
            String gameId = JsonHelper.getRequiredText(node, "gameID");
            String playerId = JsonHelper.getRequiredText(node, "playerID");
            gameMap.get(gameId).get(playerId).action(json, this);
        }
        else {
            initPlayer(json);
        }
    }


    public void initPlayer(String json) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = JsonHelper.parseJson(json);
            String gameID = root.get("gameID").asText();
            String playerID = root.get("playerID").asText();
            int lvl = root.get("lvl").asInt();
            Player temp = new Player();
            temp.setId(playerID);
            temp.setState(new BaseState());

            try {
                gameList.CreateNewGame(gameID, temp, lvl);
                gameMap.put(gameID, new HashMap<>());
            } catch (IllegalArgumentException e) {
                gameList.JoinGame(gameList.getGames().get(gameList.getGames().indexOf(
                        gameList.getGames().stream().filter(g -> g.getGameID().equals(gameID)).findFirst().orElseThrow())), temp);
            }

            gameMap.get(gameID).put(playerID, new LoginController(gameList.getGames().stream().filter(g -> g.getGameID().equals(gameID)).findFirst().orElseThrow()
                    .getPlayers().values().stream().filter(p -> p.GetID().equals(playerID)).findFirst().orElseThrow(), gameID));

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }


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