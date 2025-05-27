package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Commands.DebugShip;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Controller.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;
import org.example.galaxy_trucker.View.ClientModel.States.ReadCardClient;

public class ReadCardState extends PlayerState {
    @Override
    public boolean allows(LoginCommand command){
        return true;
    }

    @Override
    public boolean allows(ReadyCommand command){
        return true;
    }

    @Override
    public boolean allows(DebugShip command){return true;}

    @Override
    public Command createDefaultCommand(String gameId, Player player) {
        //tecnicamente potremmo aspettare una decina di secondi, anche se in realtà potrebbero decidere gli altri di aspettare il bro
        int lv= player.getCommonBoard().getLevel();
        return new ReadyCommand(gameId,player.GetID(),lv,"Ready",true,"placeholder");
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new ReadCardClient());
    }
}
