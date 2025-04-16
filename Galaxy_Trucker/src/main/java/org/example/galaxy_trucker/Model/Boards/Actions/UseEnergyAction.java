package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.PowerCenter;

public class UseEnergyAction extends ComponentAction {
    private PlayerBoard playerBoard;
    public UseEnergyAction(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(PowerCenter powerCenter, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("illegal state");
        }
        powerCenter.useEnergy();
        playerBoard.setEnergy(-1);
    }

}
