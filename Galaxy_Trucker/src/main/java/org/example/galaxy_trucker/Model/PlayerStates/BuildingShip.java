package org.example.galaxy_trucker.Model.PlayerStates;

import org.example.galaxy_trucker.Commands.BuildingCommand;
import org.example.galaxy_trucker.Commands.FinishBuildingCommand;
import org.example.galaxy_trucker.Commands.ReadyCommand;

public class BuildingShip extends PlayerState {


    @Override
    public boolean allows(BuildingCommand command) {
        return true;
    }

    @Override
    public boolean allows(ReadyCommand command) {
        return true;
    }

    @Override
    public boolean allows(FinishBuildingCommand command){return true;}
}
