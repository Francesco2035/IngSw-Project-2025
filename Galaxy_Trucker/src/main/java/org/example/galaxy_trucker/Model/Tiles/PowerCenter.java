package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
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
        tile.sendUpdates(null,0, false, false, type);

    }

    @Override
    public void remove(@NotNull PlayerBoard playerBoard) {
        playerBoard.setEnergy(-type);
        playerBoard.getPowerCenters().remove(this);
        tile.sendUpdates(new RemoveTileEvent());

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
        tile.sendUpdates(null,0, false, false, type);

    }


    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        PowerCenter clone = new PowerCenter();
        clone.type = this.type;
        return clone;
    }

}






