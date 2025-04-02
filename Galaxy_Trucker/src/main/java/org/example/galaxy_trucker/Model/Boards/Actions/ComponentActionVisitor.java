package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HotWaterHeater;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;
import org.example.galaxy_trucker.Model.Tiles.Storage;

public abstract class ComponentActionVisitor {

    public void visit(HotWaterHeater cannon, PlayerStates State) {
    }

    public void visit(Storage storage, PlayerStates State) {
    }

    public void visit(HousingUnit housing, PlayerStates State) {
    }


    public void visit(PowerCenter powerCenter, PlayerStates State) {
    }

}
