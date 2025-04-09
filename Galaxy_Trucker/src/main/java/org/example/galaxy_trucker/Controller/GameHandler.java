package org.example.galaxy_trucker.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Game;
import org.example.galaxy_trucker.Model.GameLists;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GameHandler {


    //    String json;
    private int count = 0;
    private GameLists gameList;
    private HashMap<String, Controller> ControllerMap;


    public GameHandler(GameLists gameList) {
        this.gameList = gameList;
        this.ControllerMap = new HashMap<>();
    }


    public HashMap<String, Controller> getControllerMap() {
        return ControllerMap;
    }


    public void setControllerMap(HashMap<String, Controller> controllerMap) {
        ControllerMap = controllerMap;
    }


    public void initPlayer(String json) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }
            String title = root.get("title").asText();
            if (!"login".equals(title)) {
                throw new IllegalArgumentException("Unexpected action type: " + title);
            }
            String gameID = root.get("gameID").asText();
            String playerID = root.get("playerID").asText();
            int lvl = root.get("lvl").asInt();

            try {
                gameList.CreateNewGame(gameID, playerID, lvl);
            } catch (IllegalArgumentException e) {
                gameList.JoinGame(gameList.getGames().indexOf(gameList.getGames().stream().filter(g -> g.getGameID().equals(gameID)).findFirst().orElseThrow()), playerID);
            }

            ControllerMap.put(playerID, new LoginController(gameList.getGames().stream().filter(g -> g.getGameID().equals(gameID)).findFirst().orElseThrow(), playerID));

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
    }


    public void changeState(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }
            String title = root.get("title").asText();
            if (!"ready".equals(title)) {
                throw new IllegalArgumentException("Unexpected action type: " + title);
            }

            boolean readyState = root.get("readyState").asBoolean();

            Game curGame = gameList.getGames().get(gameList.getGames().indexOf(gameList.getGames().stream().filter(g -> g.getGameID().equals(root.get("gameID").asText())).findFirst().orElseThrow()));
            curGame.getPlayers().get(root.get("playerID").asText()).SetReady(readyState);

            for (Player player : curGame.getPlayers().values()) {
                if(player.GetReady()) count++;
//                else count--;
            }
            if(count==curGame.getPlayers().size()) {
                for (Controller controller : ControllerMap.values()) {
                    controller.nextState(this);
                }
                count = 0;
                for (Player player : curGame.getPlayers().values()) {
                    player.SetReady(false);
                }
            }
        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input #2");
        }
    }
}