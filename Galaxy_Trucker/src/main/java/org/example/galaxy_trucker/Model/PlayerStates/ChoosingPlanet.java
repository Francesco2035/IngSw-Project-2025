package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.ChoosingPlanetsCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.ChoosingPlanetClient;

public class ChoosingPlanet extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(node, "title");
//        if (!"Choosing Planets".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        int planet = JsonHelper.readInt(node, "planet");
//
//        Card card = player.getCurrentCard();
//        return new ChosingPlanetsCommand(card, planet);
//
//    }
    @Override
    public boolean allows(ChoosingPlanetsCommand command) {
        return true;
    }

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new ChoosingPlanetsCommand(-1,gameId,player.GetID(),lv,"ChoosingPlanetsCommand","placeholder"); /// devo mettere il token
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ChoosingPlanetClient());
    }
}

