package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Boards.Actions.AddCrewAction;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;

public class AddCrewState extends PlayerState {


    private Player player;
    private String json;
    private int humans;


    public AddCrewState() {

    }

    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card){
        return null;
    }

    @Override
    public boolean allows(AddCrewAction action) {
        return true;
    }
}
