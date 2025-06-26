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
