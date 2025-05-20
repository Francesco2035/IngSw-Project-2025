package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;
import org.example.galaxy_trucker.View.ClientModel.States.WaitingClient;

public class Waiting extends PlayerState{

@Override
    public Command createDefaultCommand(String gameId,Player player) { // facendo così non dovrebbe agire nel caso il thread continui a chiamare
        player.SetHasActed(true);
        return null;
    }

    @Override
    public void shouldAct(Player player) {
        player.SetHasActed(true);
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new WaitingClient());
    }
}
