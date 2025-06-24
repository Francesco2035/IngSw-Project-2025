package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.DefendFromSmallCommand;
import org.example.galaxy_trucker.Messages.PhaseEvent;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.DefendingFromSmallClient;

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
        int lv= player.getCommonBoard().getLevel();
        return new DefendFromSmallCommand(null,gameId,player.GetID(),lv,"DefendingFromSmallCommand","placeholder"); /// devo mettere il token
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new DefendingFromSmallClient());
    }
}
