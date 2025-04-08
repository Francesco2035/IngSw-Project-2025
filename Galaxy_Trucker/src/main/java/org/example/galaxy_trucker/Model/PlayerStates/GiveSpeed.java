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
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GiveSpeed  extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<IntegerPair> coordinates = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(json);

            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }

            String title = root.get("title").asText();
            if (!"give_attack".equals(title)) {
                throw new IllegalArgumentException("Unexpected action type: " + title);
            }

            JsonNode coordsArray = root.get("coordinates");
            Set<IntegerPair> uniqueCoordinates = new HashSet<>();
            for (JsonNode node : coordsArray) {
                int x = node.get("x").asInt();
                int y = node.get("y").asInt();
                IntegerPair pair = new IntegerPair(x, y);

                if (!uniqueCoordinates.add(pair)) {
                    throw new InvalidInput("Duplicate coordinate found: (" + x + ", " + y + ")");
                }

                coordinates.add(pair);
            }
        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }

        System.out.println(coordinates);


        return new GiveSpeedCommand(card.get(), coordinates, player);
    }

    @Override
    public boolean allows(GetEnginePower action){
        return true;
    }

}
