package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.View.ClientModel.States.ConsumingEnergyClient;

import java.util.ArrayList;

public class ConsumingEnergy extends PlayerState {

    @Override
    public boolean allows(ConsumeEnergyCommand command){
        return true;
    }
//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Consume Energy".equals(title)) {
//            throw new InvalidInput("Unexpected action type: " + title);
//        }
//
//        ArrayList<IntegerPair> coordinates = JsonHelper.readIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new ConsumeEnergyCommand(card, coordinates);
//    }

    @Override
    public boolean allows(UseEnergyAction action) {
        return true;
    }

    @Override /// potrebbe esserci un problema se
    public Command createDefaultCommand(String gameId, Player player) { // questo stato dovrebbe accadere se e solo se non hai cargo da farti rubare quind ti ruibano le energie
        int lv= player.getCommonBoard().getLevel();

        Card card = player.getCurrentCard();
        PlayerBoard board= player.getmyPlayerBoard();
        int p= card.getDefaultPunishment();
        ArrayList<IntegerPair> coords = new ArrayList<>();

        if(board.getEnergy()<p){ ///  possibile e il player sceglie più cannoni doppi ch energie che possiede e poi si disconnette
            //ritorno coordinatevuote cossicché dia errore nella carta e io possa poi tornare in choose cannon e non scegliere nulla che va sicuro bene;
            return new ConsumeEnergyCommand(coords,gameId,player.GetID(),lv,"ConsumeEnrgyCommand","placeholder");
            //throw new ImpossibleActionException("non ci sono abbasatanza energie da rubare");
        }
        else{
            int i=0;
            int j=0;
            ArrayList<PowerCenter> powerCenters= board.getPowerCenters();
            while (i<p){
                for (int z=0; (z<powerCenters.get(j).getType() )&&i<p;z++){
                    coords.add(new IntegerPair(powerCenters.get(j).getX(),powerCenters.get(j).getY()));
                    i++;
                }
                j++;
            }
        }

        return new ConsumeEnergyCommand(coords,gameId,player.GetID(),lv,"ConsumeEnrgyCommand","placeholder"); /// devo mettere il token
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ConsumingEnergyClient());
    }
}
