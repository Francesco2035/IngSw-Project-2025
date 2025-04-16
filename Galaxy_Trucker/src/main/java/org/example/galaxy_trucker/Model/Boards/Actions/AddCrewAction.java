package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.HousingUnit;

public class AddCrewAction extends ComponentAction {

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
    public void visit(HousingUnit housing, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("illegal state");
        }
        if ((playerBoard.getPurpleAlien() && purpleAlien) || (playerBoard.getBrownAlien() && brownAlien)) {
            throw new InvalidInput("An alien of the same type is already present in the board");
        }

        if (purpleAlien && brownAlien){
            throw new InvalidInput("Is possible to add only one type of alien");
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
