package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Player;

import java.util.Optional;

public class BaseState extends PlayerState {
    @Override
    public Command PlayerAction(String json, Player player) {
        //pesca carta, solo il primo può farlo,
        //abbandona corsa
        return null;
    }
}
