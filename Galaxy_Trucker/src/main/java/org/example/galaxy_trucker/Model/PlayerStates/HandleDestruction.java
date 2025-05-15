package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Commands.SelectChunkCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

import java.util.ArrayList;

public class HandleDestruction extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        if (!"Choose chunk".equals(title)) {
//            throw new InvalidInput("Unexpected title: " + title);
//        }
//
//        int x = JsonHelper.readInt(root, "x");
//        int y = JsonHelper.readInt(root, "y");
//        IntegerPair chunk = new IntegerPair(x, y);
//
//        return new SelectChunkCommand(player, chunk);
//    }

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();
        /// prendi i tronconi e scegli il primo non ricordo come se fa
        PlayerBoard board=player.getmyPlayerBoard();
        ArrayList<HousingUnit> houses=board.getHousingUnits();

        int i=0;
        IntegerPair coord= null;

            for (HousingUnit housingUnit : houses) {
                if(housingUnit.getNumHumans()!=0&&housingUnit.isPurpleAlien()&&housingUnit.isBrownAlien()){

                    coord = new IntegerPair(housingUnit.getX(),housingUnit.getY());
                    break;
                }
            }
            if (coord==null){

                //devo mettere una return particolare :()()()
                /// non hai case con umani hai perso brutta merda
            }


            //board.choosePlayerBoard(coord);

        return new SelectChunkCommand(coord,gameId, player.GetID(),lv,"SelectChunkCommand","placeholder");
    }


}
