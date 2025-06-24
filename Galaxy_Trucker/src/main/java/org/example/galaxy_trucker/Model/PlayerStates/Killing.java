package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.KillCommand;
import org.example.galaxy_trucker.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.KillCrewAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.View.ClientModel.States.KillingClient;

import java.util.ArrayList;

public class Killing extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"kill".equals(title)) {
//            throw new InvalidInput("Unexpected action type: " + title);
//        }
//
//        ArrayList<IntegerPair> coordinates = new ArrayList<>();
//        coordinates = JsonHelper.readIntegerPairs(root, "coordinates");
//
//        Card card = player.getCurrentCard();
//        return new KillCommand(card, coordinates);
//    }

    @Override
    public boolean allows(KillCommand command){
        return true;
    }

    @Override
    public boolean allows(KillCrewAction action) {
        return true;
    }

    @Override // dovrei magari prima controllar che sia possibile uccidere quel numero di persone senno il player perde sksk
    public Command createDefaultCommand(String gameId, Player player) {

        ///  controlla di avere abbastanza umani altrimenti fai perdere il giocatore

        int lv= player.getCommonBoard().getLevel();
        PlayerBoard board=player.getmyPlayerBoard();
        int punishment = player.getCurrentCard().getDefaultPunishment();

        int i=0;
        int j=0;
        ArrayList<IntegerPair> coords =new ArrayList<>();

        ArrayList< HousingUnit> housingUnits = board.getHousingUnits();
        while (i<punishment){
            if(housingUnits.get(j).isPurpleAlien()){
                coords.add(new IntegerPair(housingUnits.get(j).getX(),housingUnits.get(j).getY()));
                i++;
            }
            else if(housingUnits.get(j).isBrownAlien()){
                coords.add(new IntegerPair(housingUnits.get(j).getX(),housingUnits.get(j).getY()));
                i++;
            }
            else{
                for( int z=0;z<housingUnits.get(j).getNumHumans() && i<punishment;z++){
                    coords.add(new IntegerPair(housingUnits.get(j).getX(),housingUnits.get(j).getY()));
                    i++;
                }
            }
            j++;
        }
        return new KillCommand(coords,gameId,player.GetID(),lv,"KillCommand","placeholder");
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new KillingClient());
    }
}
