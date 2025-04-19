package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galaxy_trucker.Commands.AcceptCommand;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

public class Accepting extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        ObjectMapper mapper = new ObjectMapper();
//        boolean accepting;
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Accepting".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        accepting = JsonHelper.readBoolean(root, "accepting");
//
//        Card card = player.getCurrentCard();
//        return new AcceptCommand(card, accepting);
//
//
//    }
}
