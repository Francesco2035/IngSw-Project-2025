package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.ChoosingPlanetsCommand;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.ConsumeEnergyCommand;
import org.example.galaxy_trucker.Exceptions.ImpossibleActionException;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;

import java.util.ArrayList;

public class ConsumingEnergy extends PlayerState {

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

        if(board.getEnergy()<p){
            //madona come sia sucesso non lo so dovrebbe essere la piu impossibile delle cose
            throw new ImpossibleActionException("non ci sono abbasatanza energie da rubare");
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
            }
        }

        return new ConsumeEnergyCommand(coords,gameId,player.GetID(),lv,"ConsumeEnrgyCommand","placeholder"); /// devo mettere il token
    }
}
