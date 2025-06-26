package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.HandleCargoCommand;
import org.example.galaxy_trucker.ClientServer.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.HandleCargoClient;

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


    @Override
    public boolean allows(HandleCargoCommand command) {
        return true;
    }

    @Override
    public boolean allows(AddGoodAction action){
        return true;
    }

    @Override
    public boolean allows(GetGoodAction action) {
        return true;
    }

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        int lv= player.getCommonBoard().getLevel();


    /// il default è il basta gestione cargo anche se non dovrebbero esistere casi in cui capita da disconnessi
      return new HandleCargoCommand(0,null,0,null ,
              gameId, player.GetID(), lv,"FinishCargo","placeholder");
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new HandleCargoClient());
    }

}
