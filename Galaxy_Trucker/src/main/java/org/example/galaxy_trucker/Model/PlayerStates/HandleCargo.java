package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.HandleCargoClient;

/**
 * The HandleCargo class represents a specific state of a player where they can perform cargo-related actions.
 * It extends the PlayerState class and defines the permissible actions and commands related to handling cargo.
 */
public class HandleCargo extends PlayerState {

//    @Override
//    public Command PlayerAction(String json, Player player) {
//        JsonNode root = JsonHelper.parseJson(json);
//
//        String title = JsonHelper.getRequiredText(root, "title");
//
//        switch (title) {
//            case "GetFromRewards": {
//                int position = JsonHelper.readInt(root, "position");
//                return new HandleCargoCommand(title, position, new IntegerPair(-1, -1), player);
//
//            }
//            case "GetFromStorage", "PutInStorage":{
//                int position = JsonHelper.readInt(root, "position");
//                int x = JsonHelper.readInt(root, "x");
//                int y = JsonHelper.readInt(root, "y");
//                return new HandleCargoCommand(title, position, new IntegerPair(x, y), player);
//
//            }
//            case "Finish": {
//                return new HandleCargoCommand(title, -1, new IntegerPair(-1, -1), player);
//            }
//            default: {
//                throw new InvalidInput("Title is missing in the JSON input or invalid Command");
//            }
//        }
//    }


    /**
     * Determines if the given HandleCargoCommand is allowed in the current state.
     *
     * @param command the HandleCargoCommand to be checked for permission
     * @return true if the command is allowed, false otherwise
     */
    @Override
    public boolean allows(HandleCargoCommand command) {
        return true;
    }

    /**
     * Determines whether the given AddGoodAction is allowed in the current player state.
     *
     * @param action the AddGoodAction to evaluate, representing an attempt to add a specific good
     *               to a player board or storage at designated coordinates
     * @return true if the current state allows the specified action, otherwise false
     */
    @Override
    public boolean allows(AddGoodAction action){
        return true;
    }

    /**
     * Determines whether the specified GetGoodAction is allowed in the current state.
     *
     * @param action the GetGoodAction to evaluate for permission
     * @return true if the action is allowed, false otherwise
     */
    @Override
    public boolean allows(GetGoodAction action) {
        return true;
    }

    /**
     * Creates a default command for the HandleCargo state.
     * This method generates a HandleCargoCommand with predefined parameters,
     * intended to handle the cargo-related actions for the specified player in the given game.
     *
     * @param gameId the unique identifier of the game
     * @param player the player for whom the default command is being created
     * @return a HandleCargoCommand configured with default parameters
     */
    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();


    /// il default è il basta gestione cargo anche se non dovrebbero esistere casi in cui capita da disconnessi
      return new HandleCargoCommand(0,null,0,null ,
              gameId, player.GetID(), lv,"FinishCargo","placeholder");
    }

    /**
     * Converts the current state of the player into a client-facing representation,
     * encapsulating the player's behavior and available actions in the "HandleCargo"
     * phase of the game.
     *
     * @return a PhaseEvent instance containing a HandleCargoClient object, which
     *         represents the client's view of the game in this specific state.
     */
    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new HandleCargoClient());
    }

}
