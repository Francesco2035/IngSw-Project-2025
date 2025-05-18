package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.DebugShip;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;

//TODO: da aggiungere quit
public class BaseState extends PlayerState {

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
    public Command createDefaultCommand(String gameId,Player player) {
        //tecnicamente potremmo aspettare una decina di secondi, anche se in realtà potrebbero decidere gli altri di aspettare il bro
        int lv= player.getCommonBoard().getLevel();
        return new ReadyCommand(gameId,player.GetID(),lv,"Ready",true,"placeholder");
    }
}
