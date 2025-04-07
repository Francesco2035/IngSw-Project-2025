package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.PlayerStatesss;
import org.jetbrains.annotations.NotNull;

public class PowerCenter extends Component{


    public PowerCenter() {}

    @Override
    public void rotate(Boolean direction) {}

    @Override
    public boolean controlValidity(@NotNull PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void insert(@NotNull PlayerBoard playerBoard, int x, int y) {
        playerBoard.setEnergy(type);
        playerBoard.getPowerCenters().add(this);
    }

    @Override
    public void remove(@NotNull PlayerBoard playerBoard) {
        playerBoard.setEnergy(-type);
        playerBoard.getPowerCenters().remove(this);
    }


    @Override
    public void accept(@NotNull ComponentAction visitor, @NotNull PlayerState State) {
        visitor.visit(this, State);
    }


    public void useEnergy() {
        if(this.type == 0) {
            throw new InvalidInput("cannot exceed 0 energy");
        }
        this.type = this.type-1;
    }


    @Override
    public Component clone(){
        PowerCenter clone = new PowerCenter();
        clone.type = this.type;
        return clone;
    }

}






