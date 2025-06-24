package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.*;
import org.example.galaxy_trucker.Messages.PhaseEvent;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.View.ClientModel.States.BaseStateClient;

//TODO: da aggiungere quit
public class BaseState extends PlayerState {


    public BaseState(){

    }

    @Override
    public boolean allows(LoginCommand command){
        return true;
    }

    @Override
    public boolean allows(ReadyCommand command){
        return true;
    }

    @Override
    public boolean allows(QuitCommand command){
        return true;
    }

    @Override
    public boolean allows(DebugShip command){return true;}

    @Override
    public Command createDefaultCommand(String gameId,Player player) {
        //tecnicamente potremmo aspettare una decina di secondi, anche se in realtà potrebbero decidere gli altri di aspettare il bro
        int lv= player.getCommonBoard().getLevel();
        return new ReadyCommand(gameId,player.GetID(),lv,"Ready",true,"placeholder");
    }

    @Override
    public PhaseEvent toClientState() {
        return new PhaseEvent(new BaseStateClient());
    }
}
