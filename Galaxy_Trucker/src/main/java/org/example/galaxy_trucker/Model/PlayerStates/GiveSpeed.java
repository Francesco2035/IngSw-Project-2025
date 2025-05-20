package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Commands.GiveSpeedCommand;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;
import org.example.galaxy_trucker.View.ClientModel.States.GiveSpeedClient;

import java.util.ArrayList;

public class GiveSpeed  extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"give_speed".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        ArrayList<IntegerPair> coordsArray =JsonHelper.readUniqueIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new GiveSpeedCommand(card, coordsArray, player);
//    }

    @Override
    public boolean allows(GetEnginePower action){
        return true;
    }

    @Override
    public boolean allows(GiveSpeedCommand command){
        return true;
    }

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        return new GiveSpeedCommand(null,gameId,player.GetID(),lv,"GiveSpeedCommand","placeholder"); /// devo mettere il token
    }


    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new GiveSpeedClient());
    }
}
