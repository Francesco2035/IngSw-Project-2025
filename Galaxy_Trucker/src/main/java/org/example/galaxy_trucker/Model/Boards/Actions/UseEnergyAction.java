package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;

public class UseEnergyAction extends ComponentActionVisitor {
    private PlayerBoard playerBoard;
    UseEnergyAction(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(PowerCenter powerCenter, PlayerStates State) {
        powerCenter.useEnergy();
        playerBoard.setEnergy(-1);
    }
}
