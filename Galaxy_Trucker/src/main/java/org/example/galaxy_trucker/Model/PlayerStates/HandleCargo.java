package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Controller.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Controller.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HandleCargo extends PlayerState {
    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card) {
        ObjectMapper mapper = new ObjectMapper();
        IntegerPair coordinate;
        Goods good;
        int position;

        try {
            JsonNode root = mapper.readTree(json);

            if (!root.has("title")) {
                throw new InvalidInput("Title is missing in the JSON input");
            }

            String title = root.get("title").asText();
            switch (title) {
                case "GetFromRewards": {
                    position = root.get("position").asInt();
                    return new HandleCargoCommand(title, position, new IntegerPair(-1, -1), player);
                }
                case "GetFromStorage", "PutInStorage": {
                    position = root.get("position").asInt();
                    int x = root.get("x").asInt();
                    int y = root.get("y").asInt();
                    return new HandleCargoCommand(title, position, new IntegerPair(x, y), player);
                }
                case "Finish": {
                    return new HandleCargoCommand(title, -1, new IntegerPair(-1, -1), player);
                }

                default: {
                    throw new InvalidInput("Title is missing in the JSON input or invalid Command");
                }

            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
