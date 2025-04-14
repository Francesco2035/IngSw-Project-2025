package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Controller.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.Controller.Commands.Theft;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

public class HandleTheft extends PlayerState {
    @Override
    public Command PlayerAction(String json, Player player) {

        JsonNode root = JsonHelper.parseJson(json);

        String title = JsonHelper.getRequiredText(root, "title");
        switch (title) {
            case "GetFromStorage": {
                int position = JsonHelper.readInt(root, "position");
                int x = JsonHelper.readInt(root, "x");
                int y = JsonHelper.readInt(root, "y");
                return new Theft(position, new IntegerPair(x, y), player);
            }
            default:{
                throw new InvalidInput("Title is missing in the JSON input or invalid Command");
            }
        }
    }

    @Override
    public boolean allows(GetGoodAction action) {
        return true;
    }
}
