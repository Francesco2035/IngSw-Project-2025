package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Controller.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

public class AlienAddons extends Component{


//purple=true, brown=false

    public AlienAddons() {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void rotate(Boolean direction){}

    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {

        playerBoard.getAlienAddons().add(this);
        tile.sendUpdates(null, 0,false, false, 0);

    }

    @Override
    public void remove(PlayerBoard playerBoard) {

        playerBoard.getAlienAddons().remove(this);
        tile.sendUpdates(new RemoveTileEvent());
    }

    @Override
    public Component clone(PlayerBoard clonedPlayerBoard) {
        AlienAddons clone = new AlienAddons();
        clone.type = type;
        return clone;
    }


}
