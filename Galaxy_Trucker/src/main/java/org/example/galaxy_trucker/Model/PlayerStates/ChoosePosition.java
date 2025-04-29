package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.FinishBuildingCommand;

public class ChoosePosition extends PlayerState {

    @Override
    public boolean allows(FinishBuildingCommand command){return true;}

}
