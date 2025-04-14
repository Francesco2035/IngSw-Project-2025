package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;


public class BaseState extends PlayerState {
    @Override
    public Command PlayerAction(String json, Player player) {
        JsonNode node = JsonHelper.parseJson(json);
        String title = JsonHelper.getRequiredText(node, "title");
        switch (title){
            case "Quit": {
                return () -> player.getCommonBoard().abandonRace(player);
            }
            case "Ready": {
                boolean ready = JsonHelper.readBoolean(node, "ready");
                return () -> player.SetReady(ready);
            }
            default: {
                throw new InvalidInput("invalid Title");
            }

        }
    }
}
