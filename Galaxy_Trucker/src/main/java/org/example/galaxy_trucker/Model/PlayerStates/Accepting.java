package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.AcceptCommand;
import org.example.galaxy_trucker.Controller.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Accepting extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card) {
        ObjectMapper mapper = new ObjectMapper();
        boolean accepting;
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

            accepting = root.get("accepting").asBoolean();


        } catch (IOException e) {
            throw new InvalidInput("Malformed JSON input");
        }

        return new AcceptCommand(card.get(), accepting);


    }
}
