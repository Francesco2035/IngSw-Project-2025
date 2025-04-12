package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Controller.Commands.GiveSpeedCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GiveSpeed  extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player) {
        JsonNode root = JsonHelper.parseJson(json);
        String title = JsonHelper.getRequiredText(root, "title");
        if (!"give_speed".equals(title)) {
            throw new IllegalArgumentException("Unexpected action type: " + title);
        }
        ArrayList<IntegerPair> coordsArray =JsonHelper.readUniqueIntegerPairs(root, "coordinates");

        Card card = player.getCurrentCard();
        return new GiveSpeedCommand(card, coordsArray, player);
    }

    @Override
    public boolean allows(GetEnginePower action){
        return true;
    }

}
