package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Controller.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import com.fasterxml.jackson.databind.JsonNode;

public class HandleCargo extends PlayerState {

    @Override
    public Command PlayerAction(String json, Player player) {
        JsonNode root = JsonHelper.parseJson(json);

        String title = JsonHelper.getRequiredText(root, "title");

        switch (title) {
            case "GetFromRewards": {
                int position = JsonHelper.readInt(root, "position");
                return new HandleCargoCommand(title, position, new IntegerPair(-1, -1), player);
            }
            case "GetFromStorage":
            case "PutInStorage": {
                int position = JsonHelper.readInt(root, "position");
                int x = JsonHelper.readInt(root, "x");
                int y = JsonHelper.readInt(root, "y");
                return new HandleCargoCommand(title, position, new IntegerPair(x, y), player);
            }
            case "Finish": {
                return new HandleCargoCommand(title, -1, new IntegerPair(-1, -1), player);
            }
            default: {
                throw new InvalidInput("Title is missing in the JSON input or invalid Command");
            }
        }
    }
}
