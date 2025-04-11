package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.DefendFromSmallCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.Optional;

public class DefendingFromSmall extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player) {
        ObjectMapper mapper = new ObjectMapper();
        IntegerPair batteryComp;

        try {
            JsonNode root = mapper.readTree(json);

            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }

            String title = root.get("title").asText();
            if (!"Defending from Shots".equals(title)) {
                throw new IllegalArgumentException("Unexpected action type: " + title);
            }

            JsonNode batteryNode = root.get("BatteryComp");

            if (batteryNode == null || !batteryNode.has("x") || !batteryNode.has("y")) {
                throw new InvalidInput("Shield or BatteryCompo data missing or malformed.");
            }

            batteryComp = new IntegerPair(batteryNode.get("x").asInt(), batteryNode.get("y").asInt());

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }

        Card card = player.getCurrentCard();
        return new DefendFromSmallCommand(card, batteryComp);
    }
}
