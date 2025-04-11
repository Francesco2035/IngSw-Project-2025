package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Controller.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Controller.Commands.KillCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ConsumingEnergy extends PlayerState{

    @Override
    public Command PlayerAction(String json, Player player) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<IntegerPair> coordinates = new ArrayList<>();
        //se vuole consumare più energie passa la stessa coordinata più volte
        try {
            JsonNode root = mapper.readTree(json);

            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }

            String title = root.get("title").asText();
            if (!"Consume Energy".equals(title)) {
                throw new IllegalArgumentException("Unexpected action type: " + title);
            }

            JsonNode coordsArray = root.get("coordinates");
            for (JsonNode node : coordsArray) {

                int x = node.get("x").asInt();
                int y = node.get("y").asInt();
                coordinates.add(new IntegerPair(x, y));
            }

        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }
        Card card = player.getCurrentCard();
        return new ConsumeEnergyCommand(card, coordinates);
    }



    @Override
    public boolean allows(UseEnergyAction action){
        return true;
    }
}
