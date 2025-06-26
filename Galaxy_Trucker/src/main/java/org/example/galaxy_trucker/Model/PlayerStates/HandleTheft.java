package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.Theft;
import org.example.galaxy_trucker.Commands.TheftCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.Tiles.Storage;
import org.example.galaxy_trucker.View.ClientModel.States.HandleTheftClient;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Collections.max;

/**
 * The HandleTheft class is a concrete implementation of the PlayerState
 * class, responsible for handling specific behaviors and states associated
 * with theft-related actions in the game. This state governs the player's
 * ability to perform theft operations and manage relevant game logic during
 * such interactions.
 */
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





    /**
     * Creates a default command for handling theft during the game.
     * It determines the best possible target based on maximum value in cargo
     * and creates a Theft command for the current player.
     *
     * @param gameId the unique identifier of the game instance
     * @param player the player for whom the command is being created
     * @return a newly created Theft command configured with the appropriate parameters
     */
    @Override
    public  Command createDefaultCommand(String gameId, Player player) { // se nono qui e non in consume energy ho sicuramente il cargo non vuoto
        PlayerBoard board =player.getmyPlayerBoard();
        int lv = player.getCommonBoard().getLevel();
        HashMap<Integer, ArrayList<IntegerPair>> cargoH = board.getStoredGoods();

        IntegerPair coord = null;
        int index = 0;
        boolean found = false;

        // prende la coordinata del primo elemeto di max valore
        int maxValue = max(cargoH.keySet());
        coord = cargoH.get(maxValue).getFirst();// cargoH è sempre aggiornata no?

        ArrayList<Storage> storages = board.getStorages();
        int i=storages.indexOf(board.getTile(coord.getFirst(),coord.getSecond()).getComponent()); //per prendere l'iesimo elemento devo prima prenderne l'indice da storgaes fando indexof elemet e poi get i, non mi basta usare il primo perche il primo è component mentre preso dalla get lo considero come storage
        Storage currStorage=storages.get(i);
        for(int j=0;j<currStorage.getType() && !found;j++) {
            if (currStorage.getValue(j) == maxValue) {
                index = j;
                found = true;
            }

        }
        return  new Theft(index,coord,gameId,player.GetID(),lv,"HandleTheft","boh");
    }

    /**
     * Converts the current state into a client-phase representation.
     * This method encapsulates the game state into a {@link PhaseEvent} object
     * with the associated {@link HandleTheftClient}, which represents the player's
     * state during the scenario of handling theft.
     *
     * @return a {@link PhaseEvent} object containing the {@link HandleTheftClient} state,
     *         which encapsulates the behavior and commands available during the theft-handling phase.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new HandleTheftClient());
    }

//
//    @Override
//    public Command createDefaultCommand(String gameId, Player player) {
//
//
//
//        return new Command() {
//            @Override
//            public void execute(Player player) throws IOException {
//                ArrayList<HousingUnit> units = player.getmyPlayerBoard().getHousingUnits();
////                for (HousingUnit unit : units) {
////                    if(unit.getNumHumans() == 0 && !unit.isPurpleAlien() && !unit.isBrownAlien()) {
////                        player.getmyPlayerBoard().performAction(unit, new AddCrewAction(2, false, false, player.getmyPlayerBoard()), player.getPlayerState());
////                    }
////                }
////                player.SetReady(true);
//                PlayerBoard board = player.getmyPlayerBoard();
//                HashMap<Integer, ArrayList<IntegerPair>> cargoH = board.getStoredGoods();
//                Card card = player.getCurrentCard();
//                int req = card.getDefaultPunishment();
//                //int i =0;
//                int j =0;
//                int z=0;
//                int a=0;
//                int b=0;
//                IntegerPair coord=new IntegerPair(0,0);
//
//
//                while() { // manca la condizione per finire sta merda diocanaglia
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

//                        //ruba energie
//                    }
//                    else {
//                        // prende la coordinata del primo elemeto di max valore
//                        int maxValue = cargoH.keySet().iterator().next();
//                        coord = cargoH.get(maxValue).getFirst();// cargoH è sempre aggiornata no?
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
//            }
//        };

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
 //   }


    /**
     * Determines if the specified TheftCommand is allowed.
     *
     * @param command the TheftCommand to validate
     * @return true if the command is permitted, otherwise false
     */
    @Override
    public boolean allows(TheftCommand command){
        return true;
    }

    /**
     * Determines whether the given GetGoodAction is allowed in the current state.
     *
     * @param action the GetGoodAction to be evaluated
     * @return true if the action is allowed, otherwise false
     */
    @Override
    public boolean allows(GetGoodAction action) {
        return true;
    }




}
