package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.AcceptCommand;
import org.example.galaxy_trucker.Messages.PhaseEvent;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.AcceptClient;

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
    @Override
    public boolean allows(AcceptCommand command) {
        return true;
    }


    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new AcceptCommand(gameId,player.GetID(),lv,"AcceptCommand",false,"placeholder"); /// devo mettere il token
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new AcceptClient());
    }
}


