package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Commands.Theft;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Storage;

import java.util.ArrayList;
import java.util.HashMap;

public class HandleTheft extends PlayerState {
//    @Override
//    public Command PlayerAction(String json, Player player) {
//
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//        switch (title) {
//            case "GetFromStorage": {
//                int position = JsonHelper.readInt(root, "position");
//                int x = JsonHelper.readInt(root, "x");
//                int y = JsonHelper.readInt(root, "y");
//                return new Theft(position, new IntegerPair(x, y), player);
//            }
//            default:{
//                throw new InvalidInput("Title is missing in the JSON input or invalid Command");
//            }
//        }
//    }

    @Override
    public boolean allows(GetGoodAction action) {
        return true;
    }
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCurrentCard().getLevel();


        PlayerBoard board= player.getmyPlayerBoard();
        HashMap<Integer, ArrayList<IntegerPair>> cargoH= board.getStoredGoods();

        if (cargoH.isEmpty()){
            ///  chiama con coord null e index 0 tanto si andrà in consume energy
            return  new Theft(0,null,gameId,player.GetID(),lv,"TheftCommand","placeholder");
        }
        else {
            // prende la coordinata del primo elemeto di max valore
            int maxValue = cargoH.keySet().iterator().next();
            IntegerPair coord = cargoH.get(maxValue).getFirst();// cargoH è sempre aggiornata no?

            //prendo a che indice del
            int index=0;
            ArrayList<Storage> storages = board.getStorages();
            int i=storages.indexOf(board.getTile(coord.getFirst(),coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
            Storage currStorage=storages.get(i);
            for(int j=0;j<currStorage.getType();j++){
                if(currStorage.getValue(j)==maxValue){
                    index=j;
                }
            }
            return  new Theft(index,coord,gameId,player.GetID(),lv,"TheftCommand","placeholder");
        }
    }
}
