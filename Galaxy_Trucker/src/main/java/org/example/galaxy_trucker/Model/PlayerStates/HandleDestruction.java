package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.SelectChunkCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

public class HandleDestruction extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Choose chunk".equals(title)) {
//            throw new InvalidInput("Unexpected title: " + title);
//        }
//
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        IntegerPair chunk = new IntegerPair(x, y);
//
//        return new SelectChunkCommand(player, chunk);
//    }
}
