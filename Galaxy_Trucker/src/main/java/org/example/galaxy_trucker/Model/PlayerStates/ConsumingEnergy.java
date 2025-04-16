package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

import java.util.ArrayList;

public class ConsumingEnergy extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Consume Energy".equals(title)) {
//            throw new InvalidInput("Unexpected action type: " + title);
//        }
//
//        ArrayList<IntegerPair> coordinates = JsonHelper.readIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new ConsumeEnergyCommand(card, coordinates);
//    }

    @Override
    public boolean allows(UseEnergyAction action) {
        return true;
    }
}
