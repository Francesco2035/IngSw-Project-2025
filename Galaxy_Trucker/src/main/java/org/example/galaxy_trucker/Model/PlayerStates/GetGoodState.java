package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.Actions.AddGoodAction;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;

public class GetGoodState extends PlayerState {


    public GetGoodState() {

    }

    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card){
        return null;
    }

    @Override
    public boolean allows(GetGoodAction action) {
        return true;
    }

    @Override
    public boolean allows(AddGoodAction action) {
        return true;
    }

}




















//
//Command command = getPlayerState.PlayerAction(json)
//
//
//Controller ->   ControllerPrep.........-> ControllerVerifca -> ControllerVolo-> ControllerCarta ->ControllerVolo->ControllerCarta
