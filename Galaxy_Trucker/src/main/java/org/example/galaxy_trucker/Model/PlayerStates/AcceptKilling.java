package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;

public class AcceptKilling extends PlayerState{


    public AcceptKilling() {

    }

    @Override
    public Command PlayerAction(String json, Player player, Optional<Card> card) {
        return null;
    }
}
