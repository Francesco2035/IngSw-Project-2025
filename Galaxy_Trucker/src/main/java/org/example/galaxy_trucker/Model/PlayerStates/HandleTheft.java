package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.GiveAttackCommand;
import org.example.galaxy_trucker.Commands.Theft;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.UseEnergyAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.Model.Tiles.Storage;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;
import org.example.galaxy_trucker.View.ClientModel.States.HandleTheftClient;

import java.io.IOException;
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



        return new Command() {
            @Override
            public void execute(Player player) throws IOException {
                ArrayList<HousingUnit> units = player.getmyPlayerBoard().getHousingUnits();
//                for (HousingUnit unit : units) {
//                    if(unit.getNumHumans() == 0 && !unit.isPurpleAlien() && !unit.isBrownAlien()) {
//                        player.getmyPlayerBoard().performAction(unit, new AddCrewAction(2, false, false, player.getmyPlayerBoard()), player.getPlayerState());
//                    }
//                }
//                player.SetReady(true);
                PlayerBoard board = player.getmyPlayerBoard();
                HashMap<Integer, ArrayList<IntegerPair>> cargoH = board.getStoredGoods();
                Card card = player.getCurrentCard();
                int req = card.getDefaultPunishment();
                //int i =0;
                int j =0;
                int z=0;
                int a=0;
                int b=0;
                IntegerPair coord=new IntegerPair(0,0);


//                while() {
//                    if (cargoH.isEmpty()){
//                        ArrayList<PowerCenter> powerCenters= board.getPowerCenters();
//                        if(board.getEnergy()==0){
//                            return;
//                        }
//                        if(powerCenters.get(a).getType()>0){
//                            coord.setValue(powerCenters.get(a).getX(),powerCenters.get(a).getY());
//                            board.performAction(powerCenters.get(a),new UseEnergyAction(board),new ConsumingEnergy());
//                            z--;
//                        }
//                    }
////                        for (int z=0; (z<powerCenters.get(j).getType() )&&i<p;z++){
////                            coords.add(new IntegerPair(powerCenters.get(j).getX(),powerCenters.get(j).getY()));
////                            i++;
////                        }
//                        //ruba energie
//                    }
//                    else {
//                        // prende la coordinata del primo elemeto di max valore
//                        int maxValue = cargoH.keySet().iterator().next();
//                        IntegerPair coord = cargoH.get(maxValue).getFirst();// cargoH è sempre aggiornata no?
//                        int index=0;
//                        ArrayList<Storage> storages = board.getStorages();
//                        int i=storages.indexOf(board.getTile(coord.getFirst(),coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
//                        Storage currStorage=storages.get(i);
//                        for(int j=0;j<currStorage.getType();j++){
//                            if(currStorage.getValue(j)==maxValue){
//                                index=j;
//                            }
//                    }
//                }
            }
        };

//        int lv= player.getCommonBoard().getLevel();
//
//
//
//
//        if (cargoH.isEmpty()){
//            ///  chiama con coord null e index 0 tanto si andrà in consume energy
//            return  new Theft(0,null,gameId,player.GetID(),lv,"TheftCommand","placeholder");
//        }
//        else {
//            // prende la coordinata del primo elemeto di max valore
//            int maxValue = cargoH.keySet().iterator().next();
//            IntegerPair coord = cargoH.get(maxValue).getFirst();// cargoH è sempre aggiornata no?
//
//            //prendo a che indice del
//            int index=0;
//            ArrayList<Storage> storages = board.getStorages();
//            int i=storages.indexOf(board.getTile(coord.getFirst(),coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
//            Storage currStorage=storages.get(i);
//            for(int j=0;j<currStorage.getType();j++){
//                if(currStorage.getValue(j)==maxValue){
//                    index=j;
//                }
//            }
//            return  new Theft(index,coord,gameId,player.GetID(),lv,"TheftCommand","placeholder");
        //}
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new HandleTheftClient());
    }
}
