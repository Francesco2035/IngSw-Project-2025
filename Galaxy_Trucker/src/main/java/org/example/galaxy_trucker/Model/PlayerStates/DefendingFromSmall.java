package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.DefendFromLargeCommand;
import org.example.galaxy_trucker.Commands.DefendFromSmallCommand;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

public class DefendingFromSmall extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        IntegerPair batteryComp;
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Defending From Small".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        root = JsonHelper.getNode(root, "BatteryComp");
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        batteryComp = new IntegerPair(x, y);
//
//
//        Card card = player.getCurrentCard();
//        return new DefendFromSmallCommand(card, batteryComp);
//    }
    @Override
    public boolean allows(DefendFromSmallCommand command) {
        return true;
    }
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCurrentCard().getLevel();
        return new DefendFromSmallCommand(null,gameId,player.GetID(),lv,"DefendingFromSmallCommand","placeholder"); /// devo mettere il token
    }
}
