package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

public class KillCrewAction extends ComponentActionVisitor {
    private PlayerBoard playerBoard;

    public KillCrewAction(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(HousingUnit housing, PlayerStates State) {
        if (!(State.equals(PlayerStates.Killing)||State.equals(PlayerStates.AcceptKilling))){
            throw new IllegalStateException("invalid state");
        }
        if (housing.kill() == 2){
            playerBoard.setNumHumans(-1);
        }
        else if (housing.kill() == 1){
            playerBoard.setPurpleAlien(false);
        }
        else{
            playerBoard.setBrownAlien(false);
        }
    }


}
