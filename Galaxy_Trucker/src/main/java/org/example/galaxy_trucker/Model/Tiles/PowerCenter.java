package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Exceptions.powerCenterEmptyException;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentActionVisitor;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates;

public class PowerCenter extends Component{


    public PowerCenter() {}

    @Override
    public void rotate(Boolean direction) {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void insert(PlayerBoard playerBoard) {
        playerBoard.getPowerCenters().add(this);
    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        playerBoard.getPowerCenters().remove(this);
    }


    @Override
    public void accept(ComponentActionVisitor visitor, PlayerStates State) {
        if (!State.equals(PlayerStates.UseEnergy)){
            throw new IllegalStateException("Player state is not UseEnergy state");
        }
        visitor.visit(this, State);
    }


    public void useEnergy() {
        if(this.type == 0) {
            throw new InvalidInput("cannot exceed 0 energy");
        }
        this.type = this.type-1;
    }

}






