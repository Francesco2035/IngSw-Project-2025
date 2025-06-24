package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.DefendFromLargeCommand;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.DefendingFromLargeClient;

public class DefendingFromLarge extends PlayerState{
//    @Override
//    public Command PlayerAction(String json, Player player) {
//
//        IntegerPair plasmaDrill;
//        IntegerPair batteryComp;
//        JsonNode root = JsonHelper.parseJson(json);
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Defending From Large".equals(title)) {
//            throw new IllegalArgumentException("Unexpected action type: " + title);
//        }
//        root = JsonHelper.getNode(root, "BatteryComp");
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        batteryComp = new IntegerPair(x, y);
//        root = JsonHelper.getNode(root, "plasmaDrill");
//        x = JsonHelper.readInt(root, "x");
//        y = JsonHelper.readInt(root, "y");
//        plasmaDrill = new IntegerPair(x, y);
//
//        Card card = player.getCurrentCard();
//        return new DefendFromLargeCommand(card,plasmaDrill,batteryComp);
//
//    }
    @Override
    public boolean allows(DefendFromLargeCommand command) {
        return true;
    }


    @Override
    public boolean allows(UseEnergyAction action) {
        return true;
    }

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv = player.getCommonBoard().getLevel();
        return new DefendFromLargeCommand(null,null,gameId,player.GetID(),lv,"DefendingFromLargeCommand","placeholder"); /// devo mettere il token
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new DefendingFromLargeClient());
    }
}
