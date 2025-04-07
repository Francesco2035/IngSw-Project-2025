package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.*;

public abstract class ComponentAction {

    public void visit(HotWaterHeater cannon, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    public void visit(Storage storage, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    public void visit(HousingUnit housing, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    public void visit(PowerCenter powerCenter, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }

    public void visit(PlasmaDrill plasmaDrill, PlayerState state) {
        throw new InvalidInput("Invalid input for the specific action");
    }



}
