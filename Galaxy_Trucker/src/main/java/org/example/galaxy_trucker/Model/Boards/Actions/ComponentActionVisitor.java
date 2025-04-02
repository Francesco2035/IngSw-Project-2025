package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.*;

public abstract class ComponentActionVisitor {

    public void visit(HotWaterHeater cannon, PlayerStates State) {
    }

    public void visit(Storage storage, PlayerStates State) {
    }

    public void visit(HousingUnit housing, PlayerStates State) {
    }

    public void visit(PowerCenter powerCenter, PlayerStates State) {
    }

    public void visit(PlasmaDrill plasmaDrill, PlayerStates State) {

    }

}
