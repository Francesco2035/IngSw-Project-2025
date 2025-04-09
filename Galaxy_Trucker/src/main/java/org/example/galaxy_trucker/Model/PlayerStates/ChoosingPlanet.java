package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.ChosingPlanetsCommand;
import org.example.galaxy_trucker.Controller.Commands.ChosingPlanetsCommand;
import org.example.galaxy_trucker.Controller.Commands.KillCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ChoosingPlanet extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player) {
        ObjectMapper mapper = new ObjectMapper();
        int planet;
        try {
            JsonNode root = mapper.readTree(json);

            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }

            String title = root.get("title").asText();
            if (!"Choosing Planets".equals(title)) {
                throw new IllegalArgumentException("Unexpected action type: " + title);
            }

            planet = root.get("planet").asInt();

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
        Card card = player.getCurrentCard();
        return new ChosingPlanetsCommand(card, planet);

    }
}
