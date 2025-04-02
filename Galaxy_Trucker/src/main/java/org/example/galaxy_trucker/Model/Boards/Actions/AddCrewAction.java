package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

public class AddCrewAction extends ComponentActionVisitor {

    private int humans;
    private boolean purpleAlien;
    private boolean brownAlien;
    private PlayerBoard playerBoard;


    public AddCrewAction(int humans, boolean purpleAlien, boolean brownAlien, PlayerBoard playerBoard) {
        this.humans = humans;
        this.purpleAlien = purpleAlien;
        this.brownAlien = brownAlien;
        this.playerBoard = playerBoard;
    }

    @Override
    public void visit(HousingUnit housing, PlayerStates State) {
        if (!State.equals(PlayerStates.PopulateHousingUnits)){
            throw new IllegalStateException("invalid state");
        }
        housing.addCrew(humans, purpleAlien, brownAlien);
        playerBoard.setNumHumans(humans);
        if (purpleAlien){
            playerBoard.setPurpleAlien(true);
        }
        if (brownAlien){
            playerBoard.setBrownAlien(true);
        }

    }

}
