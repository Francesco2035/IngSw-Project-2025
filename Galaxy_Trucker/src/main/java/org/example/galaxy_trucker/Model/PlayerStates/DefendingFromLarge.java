package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.DefendFromLargeCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class DefendingFromLarge extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player) {

        IntegerPair plasmaDrill;
        IntegerPair batteryComp;
        JsonNode root = JsonHelper.parseJson(json);
        String title = JsonHelper.getRequiredText(root, "title");
        if (!"Defending From Large".equals(title)) {
            throw new IllegalArgumentException("Unexpected action type: " + title);
        }
        root = JsonHelper.getNode(root, "BatteryComp");
        int x = JsonHelper.readInt(root, "x");
        int y = JsonHelper.readInt(root, "y");
        batteryComp = new IntegerPair(x, y);
        root = JsonHelper.getNode(root, "plasmaDrill");
        x = JsonHelper.readInt(root, "x");
        y = JsonHelper.readInt(root, "y");
        plasmaDrill = new IntegerPair(x, y);

        Card card = player.getCurrentCard();
        return new DefendFromLargeCommand(card,plasmaDrill,batteryComp);

    }
}
