package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Controller.Commands.Command;
import org.example.galaxy_trucker.Model.Player;

public class HandleDestruction extends PlayerState{
    @Override
    public Command PlayerAction(String json, Player player) {
        return null;
        //command che chiama keepGoing
    }
}
