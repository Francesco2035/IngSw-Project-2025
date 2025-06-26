package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

public class SewerPipes extends Component {


    public SewerPipes() {}


    @Override
    public void rotate(Boolean direction) {}

    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        tile.sendUpdates(null,0, false, false, 0);

    }

    @Override
    public void remove(PlayerBoard playerBoard) {
        tile.sendUpdates();

    }

    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        return new SewerPipes();
    }


}

