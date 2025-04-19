package org.example.galaxy_trucker.Model.PlayerStates;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.galaxy_trucker.Commands.LoginCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Commands.Command;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;


public class BaseState extends PlayerState {

    @Override
    public boolean allows(LoginCommand command){
        return true;
    }

    @Override
    public boolean allows(ReadyCommand command){
        return true;
    }

}
