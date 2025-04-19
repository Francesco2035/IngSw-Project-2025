package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.AddCrewCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

public class AddCrewState extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player){
//
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"kill".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        IntegerPair coordsArray = new IntegerPair(x, y);
//
//        int numHumans = JsonHelper.readInt(root, "numHumans");
//        boolean purpleAlien = JsonHelper.readBoolean(root, "purpleAlien");
//        boolean brownAlien = JsonHelper.readBoolean(root, "brownAlien");
//
//        return new AddCrewCommand(numHumans,purpleAlien,brownAlien,player,coordsArray);
//    }
//
//    @Override
//    public boolean allows(AddCrewAction action) {
//        return true;
//    }
}
