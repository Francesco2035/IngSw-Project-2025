package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.DefendFromSmallCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.Optional;

public class DefendingFromSmall extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player) {
        IntegerPair batteryComp;
        JsonNode root = JsonHelper.parseJson(json);
        String title = JsonHelper.getRequiredText(root, "title");
        if (!"Defending From Small".equals(title)) {
            throw new IllegalArgumentException("Unexpected action type: " + title);
        }
        root = JsonHelper.getNode(root, "BatteryComp");
        int x = JsonHelper.readInt(root, "x");
        int y = JsonHelper.readInt(root, "y");
        batteryComp = new IntegerPair(x, y);


        Card card = player.getCurrentCard();
        return new DefendFromSmallCommand(card, batteryComp);
    }
}
