package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.RemoveTileCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

public class CheckValidity extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = root.path("title").asText();
//        if (!"RemoveTile".equals(title)) {
//            throw new InvalidInput("Unexpected action type: " + title);
//        }
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        IntegerPair coordinate = new IntegerPair(x, y);
//        return new RemoveTileCommand(player, coordinate);
//    }
}
