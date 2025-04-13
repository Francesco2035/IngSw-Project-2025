package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Controller.Commands.InsertTileCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Tile;

import java.util.Optional;

public class BuildingShip extends PlayerState {



    @Override
    public Command PlayerAction(String json, Player player){
        JsonNode root = JsonHelper.parseJson(json);
        String title = JsonHelper.getRequiredText(root, "title");
        switch (title){
            case "InsertTile": {
                int x = root.get("x").asInt();
                int y = root.get("y").asInt();
                int rotation = root.get("rotation").asInt();
                return new InsertTileCommand(player, x, y, rotation, title,-1);
            }
            case "ToBuffer" :{
                int rotation = root.get("rotation").asInt();
                return new InsertTileCommand(player,-1,-1, rotation, title,-1);
            }
            case "FromBuffer", "Pick Tile":{
                int position = root.get("position").asInt();
                return new InsertTileCommand(player,0,-1, -1, title, position);
            }
            case "Discard":{
                return new InsertTileCommand(player,0,-1, -1, title,-1);
            }
            default:throw new InvalidInput("Title is missing in the JSON input or invalid Command");
        }

    }



}
