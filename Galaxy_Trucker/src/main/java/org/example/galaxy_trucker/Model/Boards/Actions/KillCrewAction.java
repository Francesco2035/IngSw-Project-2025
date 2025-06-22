package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

public class KillCrewAction extends ComponentAction {
    private PlayerBoard playerBoard;

    public KillCrewAction(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(HousingUnit housing, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        int typeKill = housing.kill();
        if (typeKill == 2){
            playerBoard.setNumHumans(-1);
        }
        else if (typeKill == 1){
            playerBoard.setPurpleAlien(false);
        }
        else{
            playerBoard.setBrownAlien(false);
        }
    }


}
